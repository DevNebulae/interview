package org.example.interview

interface PINTerminalRepository {
    fun activate(customerId: String, macAddress: String): Int
}
