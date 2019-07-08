import com.google.gson.annotations.SerializedName

data class EntryDto(
        @SerializedName("media") val media: List<MediaDto>
)