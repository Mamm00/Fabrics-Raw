package com.alazhartex.costing.backend.service

import com.alazhartex.costing.backend.model.AnalyzeRequest
import com.alazhartex.costing.backend.model.AnalyzeResponse
import com.alazhartex.costing.backend.model.Citation

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
 * Deterministic offline/stub implementation used when no provider key is
 * configured. Returns structured guidance derived from the batch context so the
 * endpoint stays testable without network or secrets.
 */
class StubBenchmarkService : BenchmarkService {

    override suspend fun analyze(request: AnalyzeRequest): AnalyzeResponse {
        val second = request.pctSecond?.let { runCatching { it.toDoubleOrNull() }.getOrNull() }
        val notes = buildList {
            if (second != null && second >= 15.0) {
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
            summaryEn = "Stub benchmark: review yield, waste and second-grade compensation for ${request.fabricNameEn}.",
            summaryAr = "مؤشر تجريبي: راجع المردود والهدر وتعويض الدرجة الثانية لـ ${request.fabricNameAr}.",
            citations = listOf(Citation("Textile wholesale costing guide", "https://example.invalid/benchmark")),
            benchmarkNotes = notes,
        )
    }
}
