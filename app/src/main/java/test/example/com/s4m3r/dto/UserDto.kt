import com.google.gson.annotations.SerializedName
data class UserDto (
		@SerializedName("id") val id : Int,
		@SerializedName("id_str") val id_str : Int,
		@SerializedName("name") val name : String,
		@SerializedName("screen_name") val screen_name : String,
		@SerializedName("description") val description : String,
		@SerializedName("created_at") val created_at : String,
		@SerializedName("profile_image_url") val profile_image_url : String,
		@SerializedName("profile_image_url_https") val profile_image_url_https : String
)