package com.alazhartex.costing.client.remote

import com.alazhartex.costing.client.model.AnalyzeRequestDto
import com.alazhartex.costing.client.model.AnalyzeResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.delay

/**
 * Android client for the backend `/analyze` endpoint. Timeouts and retries are
 * configured at construction; callers degrade to the last cached insight on
 * failure (offline / timeout / non-2xx).
 */
class BenchmarkClient(
    private val http: HttpClient,
    private val baseUrl: String,
    private val retryDelayMs: Long = 500,
) {

    sealed interface Result {
        data class Success(val response: AnalyzeResponseDto) : Result
        data class Failure(val reason: Reason) : Result
    }

    enum class Reason { OFFLINE, TIMEOUT, HTTP_ERROR, UNKNOWN }

    suspend fun analyze(request: AnalyzeRequestDto): Result = withRetry(times = 1) {
        runCatching {
            val response = http.post("$baseUrl/analyze") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val body: AnalyzeResponseDto = response.body()
            Result.Success(body)
        }.getOrElse { throwable ->
            val reason = when {
                throwable is java.net.SocketTimeoutException -> Reason.TIMEOUT
                throwable is java.net.UnknownHostException ||
                    throwable is java.net.ConnectException -> Reason.OFFLINE
                else -> Reason.UNKNOWN
            }
            Result.Failure(reason)
        }
    }

    private suspend fun <T> withRetry(times: Int, block: suspend () -> T): T {
        var last: Result? = null
        repeat(times) { attempt ->
            val attemptResult = block()
            if (attemptResult !is Result.Failure) return attemptResult
            last = attemptResult
            if (attempt < times - 1) delay(retryDelayMs)
        }
        @Suppress("UNCHECKED_CAST")
        return last as T
    }
}
