package com.alazhartex.costing.backend

import com.alazhartex.costing.backend.routes.analyzeRoute
import com.alazhartex.costing.backend.service.BenchmarkService
import com.alazhartex.costing.backend.service.RemoteBenchmarkService
import com.alazhartex.costing.backend.service.StubBenchmarkService
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

/**
 * Ktor entry point. Provider credentials are read from the environment only
 * (`BENCHMARK_PROVIDER_URL`, `BENCHMARK_API_KEY`) and never embedded in source,
 * logs, or responses. Without both variables the deterministic stub is used, so
 * the endpoint stays usable offline.
 */
fun main() {
    val port = System.getenv("BACKEND_PORT")?.toIntOrNull() ?: 8080
    val providerUrl = System.getenv("BENCHMARK_PROVIDER_URL")
    val providerKey = System.getenv("BENCHMARK_API_KEY")

    val benchmarkService: BenchmarkService =
        if (!providerUrl.isNullOrBlank() && !providerKey.isNullOrBlank()) {
            RemoteBenchmarkService(providerUrl = providerUrl, apiKey = providerKey)
        } else {
            StubBenchmarkService()
        }

    embeddedServer(Netty, port = port) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        routing {
            analyzeRoute(benchmarkService)
        }
    }.start(wait = true)
}
