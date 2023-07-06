package org.example.interview.infrastructure

import org.apache.hc.core5.http.HttpStatus
import org.apache.logging.log4j.LogManager
import org.example.interview.Program
import org.example.interview.pin.PINTerminalRepository
import org.example.interview.pin.PINTerminalResult
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class PINTerminalRepositoryImpl(
    private val httpClient: HttpClient,
    private val baseUri: URI
) : PINTerminalRepository {
    override fun activate(customerId: String, macAddress: String): PINTerminalResult {
        val request = HttpRequest.newBuilder()
            .uri(baseUri.resolve("./activate"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("""{"customerId": "$customerId", "macAddress": "$macAddress"}"""))
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        return when (val statusCode = response.statusCode()) {
            HttpStatus.SC_OK -> PINTerminalResult.Activated
            HttpStatus.SC_NOT_FOUND -> {
                logger.error("PIN terminal with MAC address '$macAddress' could not be activated due to it not being registered")
                PINTerminalResult.NotFound
            }
            HttpStatus.SC_CONFLICT -> {
                logger.error("PIN terminal could not be activated due to a conflict with existing customer with ID '$customerId'")
                PINTerminalResult.Conflict
            }
            else -> {
                logger.fatal("Unhandled HTTP status code '${statusCode}'")
                error("Unexpected HTTP status code received")
            }
        }
    }

    companion object {
        private val logger = LogManager.getLogger(PINTerminalRepositoryImpl::class.java)
    }
}