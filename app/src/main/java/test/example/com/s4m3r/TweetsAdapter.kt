package test.example.com.s4m3r

import TweetDto
import UserDto
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_tweet.view.*

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
        holder.itemView.timeTextView.text = tweetDto.created_at
        holder.itemView.nameTextView.text = user?.name

        val retweetText = if (tweetDto.retweet_count == 1)
            ("1 retweet")
        else
            ("${tweetDto?.retweet_count ?: 0} retweets")
        holder.itemView.retweetsTextView.text = retweetText

        Picasso.get().load(user?.profile_image_url).into(holder.itemView.imageView)
    }

    fun updateUser(newUser: UserDto) {
        user = newUser
    }

}