package com.devtides.retrofitproject.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiCallService {

    /* Single point of access to the API,
       there will be only one instance of the API service,
       That is SINGLETON !!!
    */

    private val BASE_URL = "https://us-central1-apis2-e78c3.cloudfunctions.net/"

    // Create Retrofit instance
    // and set the base URL
    // and the converter factory (Gson) for the API

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
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