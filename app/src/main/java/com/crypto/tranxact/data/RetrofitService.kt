package com.crypto.tranxact.data

import com.crypto.tranxact.data.models.Asset
import com.crypto.tranxact.data.models.Exchange
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface RetrofitService {

    @GET("v1/assets")
    suspend fun assets(): Response<List<Asset>>

    @GET("v1/exchanges")
    suspend fun exchanges(): Response<List<Exchange>>

    companion object {

        var retrofitService: RetrofitService? = null

        fun instance(): RetrofitService {
            if (retrofitService == null) {

                val apiKey = "INSERT API KEY"

                val httpClient = OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("X-CoinAPI-Key", apiKey)
                        chain.proceed(request.build())
                    }
                    .build()

                val retrofit = Retrofit.Builder().baseUrl("https://rest.coinapi.io/")
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                retrofitService = retrofit.create(RetrofitService::class.java)
            }

            return retrofitService!!
        }
    }
}