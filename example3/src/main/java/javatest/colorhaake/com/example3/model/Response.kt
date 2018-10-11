package javatest.colorhaake.com.example3.model

import java.net.HttpURLConnection

data class Response<out T>(
        val total: Int,
        val totalHits: Int,
        val hits: List<T>,
        private val _metadata: Metadata?
) {
    val metadata: Metadata
        get() = _metadata ?: Metadata.defaultInstance()

    companion object {
        val defaultHttpExceptionInstance = Response<Any>(
                0,
                0,
                listOf(),
                Metadata(Metadata.TYPE_HTTP_EXCEPTION, HttpStatusCode.INVALID, "", "")
        )
    }
}

data class Metadata(
        private val _type: Int?,
        private val _httpStatusCode: Int?,
        private val _status: String?,
        private val _description: String?
) {

    val type: Int
        get() = _type ?: TYPE_INVALID

    val httpStatusCode
        get() = _httpStatusCode ?: HTTP_STATUS_CODE_INVALID

    val status: String
        get() = _status ?: ""

    val description: String
        get() = _description ?: ""

    val isNetworkUnavailable: Boolean
        get() = (type == TYPE_UNKNOWN_HOST_EXCEPTION
                || type == TYPE_CONNECT_EXCEPTION
                || type == TYPE_SOCKET_TIMEOUT_EXCEPTION
                || type == TYPE_SOCKET_EXCEPTION
                || type == TYPE_SSL_HANDSHAKE_EXCEPTION
                || type == TYPE_SSL_EXCEPTION
                || type == TYPE_IO_EXCEPTION)

    val isSuccess: Boolean
        get() = HttpURLConnection.HTTP_MULT_CHOICE > httpStatusCode
                && httpStatusCode >= HttpURLConnection.HTTP_OK

    val isNotSuccess: Boolean
        get() = !isSuccess

    companion object {

        const val TYPE_INVALID = -1
        const val TYPE_NORMAL = 0
        const val TYPE_UNKNOWN_HOST_EXCEPTION = 1
        const val TYPE_CONNECT_EXCEPTION = 2
        const val TYPE_SOCKET_TIMEOUT_EXCEPTION = 3
        const val TYPE_SOCKET_EXCEPTION = 4
        const val TYPE_SSL_HANDSHAKE_EXCEPTION = 5
        const val TYPE_SSL_EXCEPTION = 6
        const val TYPE_IO_EXCEPTION = 7
        const val TYPE_HTTP_EXCEPTION = 8
        const val TYPE_DO_NOT_KNOW_HOW_TO_HANDLE_EXCEPTION = 999

        const val HTTP_STATUS_CODE_INVALID = -1

        fun defaultInstance(): Metadata {
            return Metadata(TYPE_INVALID, HTTP_STATUS_CODE_INVALID, "", "")
        }
    }
}

object HttpStatusCode {
    const val INVALID = -1
}
