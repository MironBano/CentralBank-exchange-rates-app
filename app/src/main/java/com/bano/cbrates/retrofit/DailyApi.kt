package com.bano.cbrates.retrofit

import retrofit2.http.GET

interface DailyApi {
    @GET("daily_json.js")
    suspend fun getDaily(): Daily
}