package com.example.habittracker.data.remote.utils

import android.util.Log
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> retryIO(
    times: Int = Int.MAX_VALUE,
    initialDelay: Long = 500, // Начальная задержка (миллисекунды)
    maxDelay: Long = 5000, // Максимальная задержка (миллисекунды)
    factor: Double = 2.0, // Множитель задержки
    block: suspend () -> T
): T {
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (e: HttpException) {
            Log.e("retryIO","Something went wrong")
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        } catch (e: IOException){
            Log.e("retryIO", "Couldn't reach server")
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
    }
    return block()
}