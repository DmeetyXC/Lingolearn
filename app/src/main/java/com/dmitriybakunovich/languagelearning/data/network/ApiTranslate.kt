package com.dmitriybakunovich.languagelearning.data.network

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiTranslate {
    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
    suspend fun getTranslateText(
        @Field("key") key: String,
        @Field("text") text: String,
        @Field("lang") lang: String
    ): TranslateModel
}