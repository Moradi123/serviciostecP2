package com.example.serviciostec

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST


data class PaymentResponse(val clientSecret: String)


interface StripeApi {
    @POST("api/checkout")
    fun getPaymentIntent(): Call<PaymentResponse>
}


class PagoActivity : AppCompatActivity() {

    private lateinit var paymentSheet: PaymentSheet
    private lateinit var btnPagar: Button
    private lateinit var txtEstado: TextView
    private var paymentIntentClientSecret: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)

        PaymentConfiguration.init(this, "pk_test_51Sg0dJLkju0GqPxR8BjcWY0yEkxihsqj6X0tXv1RFnD3BdTkjm4eByTfQVaQ8I6TI5fk64ILoOtqeCUpunf9iD6I00cIAf9jWj")

        btnPagar = findViewById(R.id.btn_pagar)
        txtEstado = findViewById(R.id.txt_estado)

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        obtenerDatosDelPago()

        btnPagar.setOnClickListener {
            presentarPago()
        }
    }

    private fun obtenerDatosDelPago() {
        txtEstado.text = "Conectando con servidor..."


        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8181/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(StripeApi::class.java)
        val call = service.getPaymentIntent()

        call.enqueue(object : Callback<PaymentResponse> {
            override fun onResponse(call: Call<PaymentResponse>, response: Response<PaymentResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    paymentIntentClientSecret = response.body()!!.clientSecret
                    txtEstado.text = "Listo para pagar"
                    btnPagar.isEnabled = true
                } else {
                    txtEstado.text = "Error servidor: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                txtEstado.text = "Fallo conexión: ${t.message}"
            }
        })
    }

    private fun presentarPago() {
        paymentIntentClientSecret?.let { secret ->
            paymentSheet.presentWithPaymentIntent(
                secret,
                PaymentSheet.Configuration(
                    merchantDisplayName = "Servicios Tec",
                )
            )
        }
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(this, "Pago cancelado", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(this, "Error: ${paymentSheetResult.error}", Toast.LENGTH_LONG).show()
                txtEstado.text = "Error al procesar"
            }
            is PaymentSheetResult.Completed -> {
                Toast.makeText(this, "¡Pago Exitoso!", Toast.LENGTH_LONG).show()
                txtEstado.text = "Pago completado"
            }
        }
    }
}