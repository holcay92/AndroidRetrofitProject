package com.devtides.retrofitproject.model

import com.devtides.retrofitproject.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiCallService {

    /* Single point of access to the API,
       there will be only one instance of the API service,
       That is SINGLETON !!!
    */

    private val BASE_URL = "https://us-central1-apis-4674e.cloudfunctions.net/"

    // Create Retrofit instance
    // and set the base URL
    // and the converter factory (Gson) for the API

    val okhttp2Client = OkHttpClient.Builder()

    init{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        if(BuildConfig.DEBUG){
            okhttp2Client.addInterceptor(logging)
        }
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okhttp2Client.build())
        .build()
        .create(ApiCall::class.java)

    /*
    * since the api variable is private,
    * we create a function that can be called from anywhere
    * (component, system vs) to access the API
    * we simply call that function and pass in the parameters
    */
    fun getApiCall() = api.callGet()

}
