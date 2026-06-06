package com.example.apppruebauno.data.model



import com.example.apppruebauno.data.model.FacturaSunat
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET

interface ApiService {

    @POST("api/facturas") // Ajusta a tu ruta de Cloud Run
    suspend fun enviarFactura(@Body factura: FacturaSunat): Response<Void>

    @GET("api/facturas")
    suspend fun obtenerFacturas(): Response<List<FacturaSunat>>
}