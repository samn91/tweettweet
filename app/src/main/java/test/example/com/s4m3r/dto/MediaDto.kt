package test.example.com.s4m3r.dto;
import com.google.gson.annotations.SerializedName

data class MediaDto(
        @SerializedName("media_url") val mediaUrl: String?
)