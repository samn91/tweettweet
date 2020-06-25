package test.example.com.s4m3r;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowLooper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.android.MainThreadDisposable;
import test.example.com.s4m3r.dto.EntryDto;
import test.example.com.s4m3r.dto.MediaDto;
import test.example.com.s4m3r.dto.TweetDto;
import test.example.com.s4m3r.dto.UserDto;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Samer on 15/6/2020 2:07 PM.
 */

@RunWith(RobolectricTestRunner.class)
public class TweetsAdapterTest {

    private TweetsAdapter tweetsAdapter;

    @Before
    public void setUp() {
        tweetsAdapter = new TweetsAdapter();
        changeUserName("Samer");
        List<TweetDto> list = new ArrayList<>();
        list.add(new TweetDto("created", 0, "Test", new EntryDto(new ArrayList<MediaDto>()), 10, 5));
        tweetsAdapter.submitList(list);
//        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
//        lstView = (ListView) mainActivity.findViewById(R.id.recycler);//getting the list layout xml
//        ShadowLog.stream = System.out; //This is for printing log messages in console
    }

    private void changeUserName(String name) {
        tweetsAdapter.updateUser(new UserDto(0, name, "Samer Screem", "", "", null,""));
    }


    @Test
    public void createViewHolderTest() {
        ViewGroup viewGroup = new FrameLayout(RuntimeEnvironment.application);
        RecyclerView.ViewHolder viewHolder = tweetsAdapter.onCreateViewHolder(viewGroup, 0);
        Assert.assertNotNull(viewHolder.itemView);
    }

    @Test
    public void bindTest() {
        ViewGroup viewGroup = new FrameLayout(RuntimeEnvironment.application);
        RecyclerView.ViewHolder viewHolder = tweetsAdapter.onCreateViewHolder(viewGroup, 0);
        tweetsAdapter.onBindViewHolder(viewHolder, 0);

        String nameFromTextView = getStringFromViewHolderView(viewHolder, R.id.nameTextView);
        String retweetFromTextView = getStringFromViewHolderView(viewHolder, R.id.retweetsTextView);
        String likeFromTextView = getStringFromViewHolderView(viewHolder, R.id.likeTextView);

        Assert.assertEquals("Samer", nameFromTextView);
        Assert.assertEquals("10", retweetFromTextView);
        Assert.assertEquals("5", likeFromTextView);

    }

    private String getStringFromViewHolderView(RecyclerView.ViewHolder vh, int id) {
        return ((TextView) vh.itemView.findViewById(id)).getText().toString();
    }
}