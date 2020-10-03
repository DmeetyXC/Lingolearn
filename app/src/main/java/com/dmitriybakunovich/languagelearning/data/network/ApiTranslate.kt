package com.dmitriybakunovich.languagelearning.data.network

import com.dmitriybakunovich.languagelearning.data.entity.TranslateResult
import com.dmitriybakunovich.languagelearning.data.entity.TranslationData
import retrofit2.http.*

interface ApiTranslate {
    @Headers("Content-Type: application/json")
    @POST("/translate?api-version=3.0")
    suspend fun getTranslateText(
        @Header("Ocp-Apim-Subscription-Key") key: String,
        @Query("from") langFrom: String,
        @Query("to") langTo: String,
        @Body text: List<TranslationData>
    ): List<TranslateResult>
}