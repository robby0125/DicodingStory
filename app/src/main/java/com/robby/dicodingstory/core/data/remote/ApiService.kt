package com.robby.dicodingstory.core.data.remote

import com.robby.dicodingstory.core.data.remote.response.AddStoryResponse
import com.robby.dicodingstory.core.data.remote.response.AllStoriesResponse
import com.robby.dicodingstory.core.data.remote.response.DetailStoryResponse
import com.robby.dicodingstory.core.data.remote.response.LoginResponse
import com.robby.dicodingstory.core.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("location") location: Int,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): AllStoriesResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: Double? = null,
        @Part("lon") longitude: Double? = null
    ): AddStoryResponse

    @Multipart
    @POST("stories/guest")
    suspend fun addStoryAsGuest(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: Double? = null,
        @Part("lon") longitude: Double? = null
    ): AddStoryResponse
}