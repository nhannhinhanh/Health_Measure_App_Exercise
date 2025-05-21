package com.example.healthexercisetracker.utils

import android.os.SystemClock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerManager {

    private var startTime = 0L
    private var pausedTime = 0L
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> get() = _elapsedTime

    fun start() {
        startTime = SystemClock.elapsedRealtime()
        job = scope.launch {
            while (isActive) {
                _elapsedTime.value = SystemClock.elapsedRealtime() - startTime + pausedTime
                delay(1000)
            }
        }
    }

    fun pause() {
        pausedTime += SystemClock.elapsedRealtime() - startTime
        job?.cancel()
    }

    fun resume() {
        start()
    }

    fun stop(): Long {
        job?.cancel()
        return (_elapsedTime.value / 1000) // return in seconds
    }
}
