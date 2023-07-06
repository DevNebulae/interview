package org.example.interview

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.example.interview.pin.PINTerminalRepository
import org.example.interview.pin.PINTerminalResult

class Program(private val repository: PINTerminalRepository) {
    fun enablePinTerminal(customerId: String, macAddress: String): Status {
        logger.info("Activating PIN terminal with MAC address '$macAddress' for customer with ID '$customerId'")
        return when (repository.activate(customerId, macAddress)) {
            PINTerminalResult.Activated -> Status.Active
            PINTerminalResult.NotFound, PINTerminalResult.Conflict -> Status.Inactive
        }
    }

    companion object {
        private val logger: Logger = LogManager.getLogger(Program::class.java)
    }
}
