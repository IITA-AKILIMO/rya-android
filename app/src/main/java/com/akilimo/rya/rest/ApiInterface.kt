package com.akilimo.rya.rest

//import retrofit2.converter.gson.GsonConverterFactory

import com.akilimo.rya.data.Currency
import com.akilimo.rya.data.RyaEstimate
import com.akilimo.rya.data.RyaPlot
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiInterface {

    companion object {

        fun create(BASE_URL: String = "http://157.245.26.55:3000/api/"): ApiInterface {

            val builder = OkHttpClient.Builder()
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY;
            builder.networkInterceptors().add(httpLoggingInterceptor);
            val client = builder.build();

            val retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }


    @POST("v1/rya/read-plot")
    fun readPlots(@Body ryaPlot: RyaPlot): Call<ResponseBody>

    @POST("v1/rya/estimate")
    fun getEstimate(@Body ryaEstimate: RyaEstimate): Call<RyaEstimate>
}
