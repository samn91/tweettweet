import com.google.gson.annotations.SerializedName

data class TweetDto(
        @SerializedName("created_at") val created_at: String,
        @SerializedName("id") val id: Long,
        @SerializedName("text") val text: String,
        @SerializedName("retweet_count") val retweet_count: Int,
        @SerializedName("favorite_count") val favorite_count: Int
)