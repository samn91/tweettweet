package test.example.com.s4m3r

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_tweet.view.*
import test.example.com.s4m3r.dto.TweetDto
import test.example.com.s4m3r.dto.UserDto

class TweetsAdapter : ListAdapter<TweetDto, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<TweetDto>() {
    override fun areItemsTheSame(oldItem: TweetDto, newItem: TweetDto): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TweetDto, newItem: TweetDto): Boolean = oldItem == newItem
}) {

    private var user: UserDto? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tweet, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tweetDto = getItem(position)

        holder.itemView.descriptionTextView.text = tweetDto.text
        holder.itemView.timeTextView.text = tweetDto.created_at.split(" +").firstOrNull()
        holder.itemView.nameTextView.text = user?.screen_name

        holder.itemView.retweetsTextView.text = tweetDto.retweetCount.toString()
        holder.itemView.likeTextView.text = tweetDto.favoriteCount.toString()

        val idelingDecrement = object : Callback {
            override fun onSuccess() {
                TweetsViewModel.idlingResource.decrement()
            }

            override fun onError(e: Exception?) {
                TweetsViewModel.idlingResource.decrement()
            }
        }

        tweetDto.entities?.media?.getOrNull(0)?.mediaUrl?.let {
            TweetsViewModel.idlingResource.increment()
            Picasso.get().load(it).into(holder.itemView.mediaImageView, idelingDecrement)
        } ?: holder.itemView.mediaImageView.setImageDrawable(null)

        user?.profile_image_url?.let {
            TweetsViewModel.idlingResource.increment()
            Picasso.get().load(it).into(holder.itemView.avatarImageView, idelingDecrement)
        }
    }

    fun updateUser(newUser: UserDto) {
        user = newUser
    }

}