package com.muhammad.nutribot.data.remote.network.network

import com.muhammad.nutribot.utils.Constants.FOOD_SEARCH_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory{
    fun createClient() : HttpClient {
        return HttpClient(Android){
            install(ContentNegotiation){
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(Logging){
                level = LogLevel.ALL
                logger = object : Logger{
                    override fun log(message: String) {
                        println(message)
                    }
                }
            }
            install(DefaultRequest){
                url(FOOD_SEARCH_BASE_URL)
                contentType(ContentType.Application.Json)
            }
        }
    }
}