package com.alazhartex.costing.backend.service

import com.alazhartex.costing.backend.model.AnalyzeRequest
import com.alazhartex.costing.backend.model.AnalyzeResponse
import com.alazhartex.costing.backend.model.Citation
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

/**
 * Abstraction over the benchmark web-search provider. The real provider client
 * sits behind this interface so the endpoint can be swapped or stubbed without
 * touching the route. The provider API key is read from the server environment
 * and never leaves this process.
 */
interface BenchmarkService {
    suspend fun analyze(request: AnalyzeRequest): AnalyzeResponse
}

/**
 * Deterministic offline/stub implementation used when no provider is
 * configured. Returns structured guidance derived from the batch context so the
 * endpoint stays testable without network or secrets.
 */
class StubBenchmarkService : BenchmarkService {

    override suspend fun analyze(request: AnalyzeRequest): AnalyzeResponse {
        val second = request.pctSecond?.toDoubleOrNull()
        val notes = buildList {
            if (second != null && second >= SECOND_GRADE_ATTENTION_PCT) {
                add("Second-grade share is elevated; plan compensation pricing.")
                add("نسبة الدرجة الثانية مرتفعة؛ خطط لتسعير تعويضي.")
            } else {
                add("Second-grade share within typical wholesale tolerance.")
                add("نسبة الدرجة الثانية ضمن الحدود المقبولة للبيع بالجملة.")
            }
            add("Cross-check current market rates for this fabric before quoting.")
            add("راجع أسعار السوق الحالية لهذا القماش قبل التسعير.")
        }
        return AnalyzeResponse(
            batchId = request.batchId,
            summaryEn = "Benchmark guidance: review yield, waste and second-grade compensation for " +
                "${request.fabricNameEn.ifBlank { request.fabricNameAr }}.",
            summaryAr = "مؤشرات مرجعية: راجع المردود والهدر وتعويض الدرجة الثانية لـ " +
                "${request.fabricNameAr.ifBlank { request.fabricNameEn }}.",
            citations = emptyList(),
            benchmarkNotes = notes,
            stub = true,
        )
    }

    private companion object {
        const val SECOND_GRADE_ATTENTION_PCT = 15.0
    }
}

/** Payload sent to the configured search provider. */
@Serializable
private data class ProviderQuery(
    val query: String,
    val locale: String = "en",
)

/** Expected provider response contract. */
@Serializable
private data class ProviderAnswer(
    val summary: String,
    val sources: List<ProviderSource> = emptyList(),
)

@Serializable
private data class ProviderSource(val title: String, val url: String)

/**
 * Real provider client. Talks to a search/answer endpoint configured through
 * `BENCHMARK_PROVIDER_URL` with `BENCHMARK_API_KEY` as a bearer token. The key is
 * read from the environment and never logged or returned to the client.
 *
 * If the provider fails, times out, or returns an unexpected payload the caller
 * falls back to [StubBenchmarkService] so the app still shows usable guidance.
 */
class RemoteBenchmarkService(
    private val providerUrl: String,
    private val apiKey: String,
    private val fallback: BenchmarkService = StubBenchmarkService(),
    private val timeout: Duration = Duration.ofSeconds(12),
) : BenchmarkService {

    private val json = Json { ignoreUnknownKeys = true }
    private val client: HttpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(8))
        .build()

    override suspend fun analyze(request: AnalyzeRequest): AnalyzeResponse {
        val query = buildQuery(request)
        val answer = runCatching { askProvider(query) }.getOrNull()
            ?: return fallback.analyze(request)

        return AnalyzeResponse(
            batchId = request.batchId,
            summaryEn = answer.summary,
            summaryAr = answer.summary,
            citations = answer.sources.take(MAX_SOURCES).map { Citation(it.title, it.url) },
            benchmarkNotes = listOf(
                "Provider: ${providerUrl.substringBefore('?')}",
                "Review the cited sources before quoting.",
            ),
            stub = false,
        )
    }

    private fun buildQuery(request: AnalyzeRequest): String = buildString {
        append("textile fabric costing benchmark ")
        append(request.fabricNameEn.ifBlank { request.fabricNameAr })
        append(" shrinkage grade yield second grade compensation")
        request.pctSecond?.let { append(" second-grade share ").append(it).append("%") }
        request.costPerMeter?.let { append(" cost per meter ").append(it) }
    }

    private fun askProvider(query: String): ProviderAnswer {
        val body = json.encodeToString(ProviderQuery.serializer(), ProviderQuery(query))
        val httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(providerUrl))
            .timeout(timeout)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $apiKey")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build()
        val response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString())
        check(response.statusCode() in 200..299) { "provider status ${response.statusCode()}" }
        return json.decodeFromString(ProviderAnswer.serializer(), response.body())
    }

    private companion object {
        const val MAX_SOURCES = 6
    }
}
