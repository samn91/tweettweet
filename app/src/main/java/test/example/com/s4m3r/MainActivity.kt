package test.example.com.s4m3r

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TweetsViewModel

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.account) {

            val editText = EditText(this).apply { setText(viewModel.getScreenName()) }
            AlertDialog.Builder(this)
                    .setTitle("Current Account:")
                    .setView(editText)
                    .setPositiveButton("ok") { _, _ ->
                        val newAccount = editText.text.toString()
                        viewModel.loadInitTweets(newAccount)
                    }
                    .show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(TweetsViewModel::class.java)
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
            supportActionBar!!.title = it.first.name
            tweetsAdapter.updateUser(it.first)
            tweetsAdapter.submitList(it.second)
            swiperefresh.isRefreshing = false
        })

        viewModel.getErrorLiveData().observe(this, Observer {
            Toast.makeText(applicationContext, getString(R.string.error), Toast.LENGTH_SHORT).show()
//            tweetsAdapter.submitList(it.second)
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
