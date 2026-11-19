package com.alazhartex.costing.backend

import com.alazhartex.costing.backend.routes.analyzeRoute
import com.alazhartex.costing.backend.service.StubBenchmarkService
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Ktor entry point. The benchmark provider key is read from the environment
 * (`BENCHMARK_API_KEY`) only; it is never embedded in source or responses.
 * When the key is absent, the stub service serves deterministic guidance.
 */
fun main() {
    val port = System.getenv("BACKEND_PORT")?.toIntOrNull() ?: 8080
    val providerKey = System.getenv("BENCHMARK_API_KEY")

    // TODO(backend): wire the real provider client once a benchmark provider is
    // chosen; keep the stub as the no-key fallback. Provider selection must
    // require providerKey != null.
    val benchmarkService = StubBenchmarkService()

    embeddedServer(Netty, port = port) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        analyzeRoute(benchmarkService)
    }.start(wait = true)
}
