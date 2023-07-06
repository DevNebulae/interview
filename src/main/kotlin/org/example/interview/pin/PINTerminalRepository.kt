package org.example.interview.pin

interface PINTerminalRepository {
    fun activate(customerId: String, macAddress: String): PINTerminalResult

}
