package com.quangtrader.cryptoportfoliotracker.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

class BinanceWebSocketManager {
    private val client = OkHttpClient.Builder()
        .pingInterval(20, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private var webSocket: WebSocket? = null

    private var lastSymbols: List<String> = emptyList()
    private var isReconnecting = false

    fun connect(symbols: List<String>, onMessage: (String) -> Unit) {
        lastSymbols = symbols

        val streams = symbols.joinToString("/") { "${it.lowercase()}@ticker" }
        val url = "wss://stream.binance.com:9443/stream?streams=$streams"

        val request = Request.Builder().url(url).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(ws: WebSocket, response: Response) {
                Log.d("WS", "Connected → $symbols")
            }

            override fun onMessage(ws: WebSocket, text: String) {
                onMessage(text) // callback raw data
            }

            override fun onClosing(ws: WebSocket, code: Int, reason: String) {
                Log.d("WS", "Closing: $reason")
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                Log.d("WS", "Closed: $reason")
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                Log.e("WS", "Error: ${t.message}")
                attemptReconnect(onMessage)
            }
        })
    }

    // 🔄 Auto reconnect
    private fun attemptReconnect(onMessage: (String) -> Unit) {
        if (isReconnecting) return
        isReconnecting = true

        GlobalScope.launch(Dispatchers.IO) {
            delay(2000)
            Log.d("WS", "Reconnecting…")
            connect(lastSymbols, onMessage)
            isReconnecting = false
        }
    }

    fun close() {
        webSocket?.close(1000, "Manual close")
    }
    fun updateSymbols(symbols: List<String>, onMessage: (String) -> Unit) {
        close()
        connect(symbols, onMessage)
    }
}