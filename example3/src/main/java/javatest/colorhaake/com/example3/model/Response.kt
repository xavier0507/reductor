package javatest.colorhaake.com.example3.model

data class Response<out T>(
        val total: Int, val totalHits: Int, val hits: List<T>
)