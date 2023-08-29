package com.comunidadedevspace.taskbeats.data.remote

import com.google.gson.annotations.SerializedName

data class NewsResponse (
    val category: String,
    val data: List<NewsDto>

)

data class NewsDto (
    val content: String,
    val url: String,
    val imageUrl: String,
    val title: String,
)