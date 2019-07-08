import com.google.gson.annotations.SerializedName

data class TweetDto(
        @SerializedName("created_at") val created_at: String,
        @SerializedName("id") val id: Long,
        @SerializedName("full_text") val text: String,
        @SerializedName("entities") val entities: EntryDto?,
        @SerializedName("retweet_count") val retweetCount: Int,
        @SerializedName("favorite_count") val favoriteCount: Int
)

