package com.alazhartex.costing.backend.routes

import com.alazhartex.costing.backend.model.AnalyzeRequest
import com.alazhartex.costing.backend.service.BenchmarkService
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.analyzeRoute(service: BenchmarkService) {
    post("/analyze") {
        val request = call.receive<AnalyzeRequest>()
        val response = service.analyze(request)
        call.respond(response)
    }
}
