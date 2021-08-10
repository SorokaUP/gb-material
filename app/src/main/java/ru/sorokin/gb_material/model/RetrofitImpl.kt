package ru.sorokin.gb_material.model

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.sorokin.gb_material.model.earth.EarthAPI
import ru.sorokin.gb_material.model.mars.MarsAPI
import ru.sorokin.gb_material.model.pod.PODAPI
import java.io.IOException

class RetrofitImpl {

    fun getMarsRetrofitImpl(): MarsAPI {
        return buildRetrofit().create(MarsAPI::class.java)
    }

    fun getEarthRetrofitImpl(): EarthAPI {
        return buildRetrofit().create(EarthAPI::class.java)
    }

    fun getPODRetrofitImpl(): PODAPI {
        return buildRetrofit().create(PODAPI::class.java)
    }

    private fun buildRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().setLenient().create())
        )
        .client(createOkHttpClient(OkInterceptor()))
        .build()

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }

    inner class OkInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }

    companion object {
        private const val BASE_URL = "https://api.nasa.gov/"
    }
}