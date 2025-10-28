package com.plcoding.bookpedia.core.data

import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

/**
 * Executes a network call safely and wraps the result in a custom [Result] object.
 * This function handles exceptions that may occur during the network request,
 * such as timeouts or connectivity issues, and maps them to specific [DataError.Remote] types.
 *
 * @param T The expected successful data type to be received from the HTTP response body.
 *          The `reified` keyword allows the type T to be accessed at runtime, which is
 *          necessary for deserializing the JSON response into the correct object.
 * @param execute A suspend lambda function that makes the actual HTTP request and
 *                returns an [HttpResponse].
 * @return A [Result] object, which is either [Result.Success] containing the data of type T,
 *         or [Result.Error] containing a [DataError.Remote] error.
 */
suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, DataError.Remote> {
    val response = try {
        // Attempt to execute the provided network call lambda.
        execute()
    } catch(e: SocketTimeoutException) {
        // If the request takes too long to complete, a SocketTimeoutException is thrown.
        // We catch it and return a specific REQUEST_TIMEOUT error.
        return Result.Error(DataError.Remote.REQUEST_TIMEOUT)
    } catch(e: UnresolvedAddressException) {
        // This exception is thrown when the device has no internet connection or the
        // server's address cannot be resolved. We map this to a NO_INTERNET error.
        return Result.Error(DataError.Remote.NO_INTERNET)
    } catch (e: Exception) {
        // This is a general catch-all for any other unexpected exceptions.
        // It ensures the coroutine is still active before returning an UNKNOWN error.
        coroutineContext.ensureActive()
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    // If the network call executes without throwing an exception,
    // the HttpResponse is passed to another function to handle the response based on its status code.
    return responseToResult(response)
}

/**
 * Converts an [HttpResponse] into a [Result] object based on the HTTP status code.
 *
 * @param T The expected successful data type. `reified` allows this type to be
 *          known at runtime for deserialization.
 * @param response The [HttpResponse] object received from the network call.
 * @return A [Result] object, either [Result.Success] with the deserialized body,
 *         or [Result.Error] with a specific [DataError.Remote] error.
 */
suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, DataError.Remote> {
    return when(response.status.value) {
        // Status codes in the 200-299 range indicate a successful request.
        in 200..299 -> {
            try {
                // Attempt to deserialize the response body into an object of type T.
                Result.Success(response.body<T>())
            } catch(e: NoTransformationFoundException) {
                // If Ktor cannot deserialize the body (e.g., JSON structure mismatch),
                // it throws this exception. We map it to a SERIALIZATION error.
                Result.Error(DataError.Remote.SERIALIZATION)
            }
        }
        // 408 indicates a request timeout.
        408 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)
        // 429 indicates that the client has sent too many requests in a given amount of time.
        429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
        // 500-599 status codes indicate a server-side error.
        in 500..599 -> Result.Error(DataError.Remote.SERVER)
        // For any other status codes not explicitly handled, return an UNKNOWN error.
        else -> Result.Error(DataError.Remote.UNKNOWN)
    }
}
