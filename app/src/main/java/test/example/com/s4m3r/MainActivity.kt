package test.example.com.s4m3r

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TweetsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(TweetsViewModel::class.java)
        viewModel.init(SCREEN_NAME)
        if (savedInstanceState == null) {
            viewModel.loadInitTweets()
        } else {
            viewModel.loadSavedTweets()
        }
        setupList()
    }

    private fun setupList() {
        val tweetsAdapter = TweetsAdapter()
        recycler.adapter = tweetsAdapter
        setupScrollListener()
        swiperefresh.setOnRefreshListener {
            viewModel.loadInitTweets()
        }
        viewModel.getTweetsLiveData().observe(this, Observer {
            tweetsAdapter.updateUser(it.first)
            tweetsAdapter.submitList(it.second)
            swiperefresh.isRefreshing = false
        })
    }

    private fun setupScrollListener() {
        val layoutManager = recycler.layoutManager as LinearLayoutManager
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                viewModel.listScrolled(layoutManager.childCount, lastVisibleItem, layoutManager.itemCount)
            }
        })
    }

}
