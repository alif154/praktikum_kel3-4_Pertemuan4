package com.app.praktikum_kel3.service.api

import com.app.praktikum_kel3.model.request.LoginRequest
import com.app.praktikum_kel3.model.request.RegisterRequest
import com.app.praktikum_kel3.model.response.LoginResponse
import com.app.praktikum_kel3.model.response.NotesResponse
import com.app.praktikum_kel3.model.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface `ApiService` mendefinisikan seluruh endpoint API
 * yang digunakan untuk berkomunikasi dengan backend server.
 *
 * Seluruh fungsi bersifat `suspend`, yang berarti harus dipanggil
 * di dalam coroutine untuk mendukung operasi asynchronous.
 */
interface ApiService {

    /**
     * Mengirimkan permintaan registrasi pengguna baru ke server.
     *
     * @param request Objek [RegisterRequest] yang berisi email, username, dan password.
     * @return Objek [Response]<[RegisterResponse]> berisi hasil registrasi dari server.
     */
    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    /**
     * Melakukan login dengan mengirimkan data kredensial pengguna ke server.
     *
     * @param request Objek [LoginRequest] berisi username dan password.
     * @return Objek [Response]<[LoginResponse]> yang berisi token autentikasi dan data pengguna.
     */
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    /**
     * Mengambil seluruh data catatan (notes) dari server.
     *
     * Endpoint ini tidak memerlukan parameter dan biasanya membutuhkan header Authorization.
     *
     * @return Objek [NotesResponse] yang berisi daftar catatan.
     */
    @GET("api/notes")
    suspend fun getAllNotes(): NotesResponse
}
