package javatest.colorhaake.com.example3.model

import com.google.gson.annotations.SerializedName

data class ImageData(
        @SerializedName("id") private val _id: Int?,

        @SerializedName("type") private val _type: String?,
        @SerializedName("tags")private  val _tags: String?,

        @SerializedName("previewURL") private val _previewUrl: String?,
        @SerializedName("previewWidth") private val _previewWidth: Int?,
        @SerializedName("previewHeight") private val _previewHeight: Int?,

        @SerializedName("imageURL") private val _imageUrl: String?,
        @SerializedName("imageWidth") private val _imageWidth: Int?,
        @SerializedName("imageHeight") private val _imageHeight: Int?,

        @SerializedName("views") private val _views: Int?,
        @SerializedName("downloads") private val _downloads: Int?,
        @SerializedName("likes") private val _likes: Int?,
        @SerializedName("comments") private val _comments: Int?,

        @SerializedName("user_id") private val _userId: Int?,
        @SerializedName("user") private val _userName: String?,
        @SerializedName("userImageURL") private val _userImageUrl: String?
) {
    val id: Int
        get() = _id ?: NOT_EXISTED_ID

    val type: String
        get() = _type ?: ""

    val tags: String
        get() = _tags ?: ""

    val previewUrl: String
        get() = _previewUrl ?: DEFAULT_IMAGE_URL

    val previewWidth: Int
        get() = _previewWidth ?: DEFAULT_SIZE

    val previewHeight: Int
        get() = _previewHeight ?: DEFAULT_SIZE

    val imageUrl: String
        get() = _imageUrl ?: DEFAULT_IMAGE_URL

    val imageWidth: Int
        get() = _imageWidth ?: DEFAULT_SIZE

    val imageHeight: Int
        get() = _imageHeight ?: DEFAULT_SIZE

    val views: Int
        get() = _views ?: 0

    val downloads: Int
        get() = _downloads ?: 0

    val likes: Int
        get() = _likes ?: 0

    val comments: Int
        get() = _comments ?: 0

    val userId: Int
        get() = _userId ?: NOT_EXISTED_ID

    val userName: String
        get() = _userName ?: ""

    val userImageUrl: String
        get() = _userImageUrl ?: ""
}

const val NOT_EXISTED_ID = -1
const val DEFAULT_IMAGE_URL = ""
const val DEFAULT_SIZE = 0
