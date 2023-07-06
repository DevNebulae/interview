package org.example.interview

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class Program(private val httpClient: HttpClient, private val baseUri: URI) {
    fun enablePinTerminal(customerId: String, macAddress: String): Status {
        val request = HttpRequest.newBuilder()
            .uri(baseUri.resolve("./activate"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("""{"customerId": "$customerId", "macAddress": "$macAddress"}"""))
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return when (response.statusCode()) {
            200 -> Status.Active
            else -> Status.Inactive
        }
    }

}

fun main(args: Array<String>) {

}