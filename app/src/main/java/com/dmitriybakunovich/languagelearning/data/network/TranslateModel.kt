package com.dmitriybakunovich.languagelearning.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TranslateModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("text")
    @Expose
    val text: List<String>,
    @SerializedName("lang")
    @Expose
    val lang: String
)