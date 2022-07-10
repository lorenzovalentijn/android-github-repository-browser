
package com.lorenzovalentijn.github.repository.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("users/{username}/repos")
    suspend fun getRepositoriesForUser(
        @Path("username") user: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<ArrayList<GithubRepositoryModel>>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepository(
        @Path("owner") user: String,
        @Path("repo") repo: String
    ): Response<GithubRepositoryModel>

    @OptIn(ExperimentalSerializationApi::class)
    companion object {
        val githubService: GithubService by lazy {
            val json = Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder().addInterceptor(logger).build()

            val contentType = "application/json".toMediaType()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()

            retrofit.create(GithubService::class.java)
        }
    }
}
