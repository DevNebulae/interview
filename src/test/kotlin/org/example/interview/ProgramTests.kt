package org.example.interview

import com.github.tomakehurst.wiremock.client.WireMock.notFound
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.example.interview.infrastructure.PINTerminalRepositoryImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI
import java.net.http.HttpClient

@WireMockTest
class ProgramTests {
    @Test
    fun `Returns Status#Active when PIN terminal has been successfully activated`(
        wireMockRuntimeInfo: WireMockRuntimeInfo,
    ) {
        // Arrange
        stubFor(post("/activate").willReturn(ok()))

        val httpClient = HttpClient.newHttpClient()
        val baseUri = URI(wireMockRuntimeInfo.httpBaseUrl)
        val repository = PINTerminalRepositoryImpl(httpClient, baseUri)
        val program = Program(repository)

        // Act
        val result = program.enablePinTerminal("12345", "AA:BB:CC:DD:EE:FF")

        // Assert
        assertEquals(Status.Active, result)
    }

    @Test
    fun `Returns Status#Inactive when PIN Terminal could not be found`(
        wireMockRuntimeInfo: WireMockRuntimeInfo,
    ) {
        // Arrange
        stubFor(post("/activate").willReturn(notFound()))

        val httpClient = HttpClient.newHttpClient()
        val baseUri = URI(wireMockRuntimeInfo.httpBaseUrl)
        val repository = PINTerminalRepositoryImpl(httpClient, baseUri)
        val program = Program(repository)

        // Act
        val result = program.enablePinTerminal("12345", "AA:BB:CC:DD:EE:AA")

        // Assert
        assertEquals(Status.Inactive, result)
    }

    @Test
    fun `Returns Status#Inactive when PIN terminal is already attached to a different customer ID`(
        wireMockRuntimeInfo: WireMockRuntimeInfo,
    ) {
        // Arrange
        stubFor(post("/activate").willReturn(notFound()))

        val httpClient = HttpClient.newHttpClient()
        val baseUri = URI(wireMockRuntimeInfo.httpBaseUrl)
        val repository = PINTerminalRepositoryImpl(httpClient, baseUri)
        val program = Program(repository)

        // Act
        val result = program.enablePinTerminal("11111", "AA:BB:CC:DD:EE:FF")

        // Assert
        assertEquals(Status.Inactive, result)
    }
}