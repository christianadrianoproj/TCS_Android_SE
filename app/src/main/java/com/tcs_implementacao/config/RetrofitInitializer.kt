package com.tcs_implementacao.config

import com.tcs_implementacao.api.IDataService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {

    private val retrofit: Retrofit =  Retrofit.Builder()
            .baseUrl("https://api-tcs.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val retrofitPython: Retrofit =  Retrofit.Builder()
        .baseUrl("https://api-tcs-opencv.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun variavelService() : IDataService {
        return retrofit.create(IDataService::class.java)
    }

    fun Service() : IDataService {
        return retrofit.create(IDataService::class.java)
    }

    fun ServicePython() : IDataService {
        return retrofitPython.create(IDataService::class.java)
    }

}