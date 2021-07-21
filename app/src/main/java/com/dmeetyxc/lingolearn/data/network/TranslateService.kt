package com.dmeetyxc.lingolearn.data.network

import com.dmeetyxc.lingolearn.data.entity.TranslateResult
import com.dmeetyxc.lingolearn.data.entity.TranslationData
import retrofit2.http.*

interface TranslateService {
    @Headers("Content-Type: application/json")
    @POST("/translate?api-version=3.0")
    suspend fun getTranslateText(
        @Header("Ocp-Apim-Subscription-Key") key: String,
        @Query("from") langFrom: String,
        @Query("to") langTo: String,
        @Body text: List<TranslationData>
    ): List<TranslateResult>
}