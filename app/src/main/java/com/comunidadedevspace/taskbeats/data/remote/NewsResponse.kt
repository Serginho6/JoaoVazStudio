package com.comunidadedevspace.taskbeats.data.remote

data class NewsResponse (
    val category: String,
    val data: List<NewsDto>

)

data class NewsDto (
    val content: String,
    val id: String,
    val imageUrl: String,
    val title: String,

)