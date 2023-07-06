package org.example.interview

import org.apache.hc.core5.http.HttpStatus
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
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

        logger.info("Activating PIN terminal with MAC address '$macAddress' for customer with ID '$customerId'")
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return when (val statusCode = response.statusCode()) {
            HttpStatus.SC_OK -> Status.Active
            HttpStatus.SC_NOT_FOUND -> {
                logger.error("PIN terminal with MAC address '$macAddress' could not be activated due to it not being registered")
                Status.Inactive
            }

            HttpStatus.SC_CONFLICT -> {
                logger.error("PIN terminal could not be activated due to a conflict with existing customer with ID '$customerId'")
                Status.Inactive
            }

            else -> {
                logger.fatal("Unhandled HTTP status code '${statusCode}'")
                error("Unexpected status code $statusCode")
            }
        }
    }

    companion object {
        private val logger: Logger = LogManager.getLogger(Program::class.java)
    }
}
