package com.plcoding.bookpedia.core.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * A factory object for creating and configuring a Ktor [HttpClient].
 *
 * This singleton provides a centralized way to build an [HttpClient] with a
 * common configuration, ensuring consistency across the application. It sets up
 * JSON serialization, request timeouts, logging, and default request headers.
 */
object HttpClientFactory {

    /**
     * Creates a new [HttpClient] with a predefined configuration.
     *
     * @param engine The platform-specific [HttpClientEngine] (e.g., CIO, OkHttp, Darwin)
     *               that will be used to execute network requests.
     * @return A configured [HttpClient] instance.
     */
    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            /**
             * Installs the [ContentNegotiation] plugin to handle serialization and
             * deserialization of request/response bodies.
             */
            install(ContentNegotiation) {
                // Configures the client to use kotlinx.serialization for JSON.
                json(
                    json = Json {
                        // Allows the JSON parser to ignore keys present in the JSON response
                        // that are not defined in the corresponding data class. This prevents
                        // the app from crashing if the API adds new fields.
                        ignoreUnknownKeys = true
                    }
                )
            }

            /**
             * Installs the [HttpTimeout] plugin to configure timeout settings for requests.
             * This helps prevent the app from hanging indefinitely if the server is unresponsive.
             */
            install(HttpTimeout) {
                // Timeout for the underlying socket to be established (20 seconds).
                socketTimeoutMillis = 20_000L
                // Total timeout for the entire request, including all retries (20 seconds).
                requestTimeoutMillis = 20_000L
            }

            /**
             * Installs the [Logging] plugin to log HTTP requests and responses.
             * This is incredibly useful for debugging network issues.
             */
            install(Logging) {
                // Defines a simple logger that prints messages to the console.
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                // Sets the logging level to ALL to see the maximum amount of detail,
                // including headers, body, and status codes.
                level = LogLevel.ALL
            }

            /**
             * Configures default parameters for every request made by this client.
             */
            defaultRequest {
                // Sets the default 'Content-Type' header to 'application/json' for all requests.
                contentType(ContentType.Application.Json)
            }
        }
    }
}
