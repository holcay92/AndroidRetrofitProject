package com.devtides.retrofitproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devtides.retrofitproject.model.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        GlobalScope.launch(Dispatchers.Main) {
            onError("Exception: ${throwable.localizedMessage}"+" error!!")
        }
    }

    val apiResponse = MutableLiveData<List<Item>>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun fetchData() {
        loading.value = true


        // enqueue() is an asynchronous call
        // enqueue() is a callback function
        // enqueue() is a function that will be called when the response is ready
        // main thread will not be blocked
        // enqueue() will be called on a background thread

        ApiCallService.getApiCall().enqueue(object : Callback<ApiCallResponse> {

            override fun onResponse( getApiCall: Call<ApiCallResponse>, response: Response<ApiCallResponse> ) {
                // response.body() is a function that will be called when the response is ready
                // response.body() is a callback function
                // response.body() is a function that will be called on a background thread
                // main thread will not be blocked
                //if response is successful then it will return the response body
                val body:ApiCallResponse? = response.body()
                apiResponse.value = body?.flatten()
                error.value = null
                loading.value = false
            }
            override fun onFailure(getApiCall: Call<ApiCallResponse>, t: Throwable) {
                onError(t.localizedMessage+"asd")
            }
        })
    }

    fun fetchDataSync(){
        loading.value = true

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = ApiCallService.getApiCall().execute()
              withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.flatten()
                        error.value = null
                        loading.value = false
                    } else {
                        onError("Error : ${response.message()}")
                    }
                }
        }
    }

    private fun onError(message: String) {
        error.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
