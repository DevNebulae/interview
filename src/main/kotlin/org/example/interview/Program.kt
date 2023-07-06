package org.example.interview

import org.apache.hc.core5.http.HttpStatus
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class Program(private val repository: PINTerminalRepository) {
    fun enablePinTerminal(customerId: String, macAddress: String): Status {
        logger.info("Activating PIN terminal with MAC address '$macAddress' for customer with ID '$customerId'")
        return when (val statusCode = repository.activate(customerId, macAddress)) {
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
