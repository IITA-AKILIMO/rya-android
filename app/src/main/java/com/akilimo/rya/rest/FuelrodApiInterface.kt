package com.akilimo.rya.rest

//import retrofit2.converter.gson.GsonConverterFactory

import com.akilimo.rya.rest.response.RemoteConfig
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import com.akilimo.rya.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor


interface FuelrodApiInterface {

    companion object {

        fun create(base_uri: String = "https://api.tsobu.co.ke/"): FuelrodApiInterface {

            val builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY;
                builder.networkInterceptors().add(httpLoggingInterceptor);
            }
            val client = builder.build()

            val retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl(base_uri)
                .build()

            return retrofit.create(FuelrodApiInterface::class.java)
        }
    }


    @GET("v1/remote-config/app-name/{app}")
    fun readConfig(@Path("app") app: String): Call<List<RemoteConfig>>
}
