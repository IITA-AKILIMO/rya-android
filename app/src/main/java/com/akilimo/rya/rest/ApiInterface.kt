package com.akilimo.rya.rest

//import retrofit2.converter.gson.GsonConverterFactory

import com.akilimo.rya.rest.request.GeneratePlot
import com.akilimo.rya.rest.request.RyaEstimate
import com.akilimo.rya.rest.request.RyaPlot
import com.akilimo.rya.rest.response.GeneratePlotResp
import com.akilimo.rya.rest.response.YieldEstimate
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit


interface ApiInterface {

    companion object {

        fun create(BASE_URL: String = "http://157.245.26.55:3000/api/"): ApiInterface {

            val builder = OkHttpClient.Builder()
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(120, TimeUnit.SECONDS)
//                .writeTimeout(120, TimeUnit.SECONDS)

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.networkInterceptors().add(httpLoggingInterceptor)
            val client = builder.build()

            val retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()


            return retrofit.create(ApiInterface::class.java)
        }
    }

    @POST("v1/rya/estimate")
    fun computeEstimate(@Body ryaEstimate: RyaEstimate): Call<YieldEstimate>

    @POST("v1/rya/read-plot")
    fun readPlot(@Body ryaPlot: RyaPlot): Call<ResponseBody>

    @POST("v1/rya/plot")
    fun generatePlots(@Body ryaPlot: GeneratePlot): Call<GeneratePlotResp>

}
