package io.example.dn

import android.os.Handler
import android.os.Looper
import android.util.Log

class AnrMonitor(private val timeoutMs: Long = 5000) {
    private val mainHandler = Handler(Looper.getMainLooper())
    private val watchdogThread = Thread {
        while (!Thread.interrupted()) {
            val pingTask = PingTask()
            mainHandler.post(pingTask)
            try {
                Thread.sleep(timeoutMs)
                if (!pingTask.isPinged) {
                    // Main thread may be blocked
                    Log.e("AnrMonitor", "Possible ANR detected: Main thread blocked for more than $timeoutMs ms")
                    // Print main thread stack trace
                    for (thread in Thread.getAllStackTraces().keys) {
                        if (thread.name == "main") {
                            Log.e("AnrMonitor", "Main thread stack: ${thread.stackTrace.joinToString("\n")}")
                            break
                        }
                    }
                }
            } catch (e: InterruptedException) {
                break
            }
        }
    }
    
    private class PingTask : Runnable {
        @Volatile
        var isPinged = false
        
        override fun run() {
            isPinged = true
        }
    }
    
    fun start() {
        watchdogThread.start()
    }
    
    fun stop() {
        watchdogThread.interrupt()
    }
} 