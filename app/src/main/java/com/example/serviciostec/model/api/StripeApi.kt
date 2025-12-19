package com.example.serviciostec.model.api

import retrofit2.Call
import retrofit2.http.POST

data class PaymentResponse(val clientSecret: String)

interface StripeApi {
    @POST("api/checkout")
    fun getPaymentIntent(): Call<PaymentResponse>
}