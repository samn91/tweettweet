package test.example.com.s4m3r.dto;

import com.google.gson.annotations.SerializedName

data class EntryDto(
        @SerializedName("media") val media: List<MediaDto>
)