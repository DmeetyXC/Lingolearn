package com.dmitriybakunovich.languagelearning.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TranslateResult(
    @SerializedName("translations")
    @Expose
    val translation: List<Translation>
)

data class Translation(
    @SerializedName("text")
    @Expose
    val text: String,
    @SerializedName("to")
    @Expose
    val to: String
)

data class TranslationData(
    @SerializedName("Text")
    val text: String
)