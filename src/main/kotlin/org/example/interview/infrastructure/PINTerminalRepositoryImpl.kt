package org.example.interview.infrastructure

import org.example.interview.PINTerminalRepository
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class PINTerminalRepositoryImpl(
    private val httpClient: HttpClient,
    private val baseUri: URI
) : PINTerminalRepository {
    override fun activate(customerId: String, macAddress: String): Int {
        val request = HttpRequest.newBuilder()
            .uri(baseUri.resolve("./activate"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("""{"customerId": "$customerId", "macAddress": "$macAddress"}"""))
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        return response.statusCode()
    }
}