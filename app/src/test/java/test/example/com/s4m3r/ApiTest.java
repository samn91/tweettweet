package test.example.com.s4m3r;

import android.text.TextUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import test.example.com.s4m3r.dto.TweetDto;
import test.example.com.s4m3r.dto.UserDto;

import static test.example.com.s4m3r.SecretKt.SCREEN_NAME;

/**
 * Created by Samer on 25/06/2020 06:58.
 */
@RunWith(RobolectricTestRunner.class)
public class ApiTest {

    ApiEndpointInterface mApi = null;
    String mScreenName = SCREEN_NAME;

    @Before
    public void setUp() {
        mApi = RetrofitHelper.INSTANCE.getApiInterface();
    }

    @Test
    public void getTweetsWithPageTest() {
        List<TweetDto> tweetDtos = mApi.getTweetsWithPage(mScreenName, 0, 1).blockingGet();

        Assert.assertEquals(1, tweetDtos.size());
        Assert.assertNotNull(tweetDtos.get(0).getText());
        Assert.assertFalse(TextUtils.isEmpty(tweetDtos.get(0).getText()));
    }

    @Test
    public void getTweetsWithMaxIdTest() {
        List<TweetDto> tweetDtosPage = mApi.getTweetsWithPage(mScreenName, 0, 1).blockingGet();
        TweetDto lastTweet = tweetDtosPage.get(0);

        List<TweetDto> tweetDtos = mApi.getTweetsWithMaxId(mScreenName, lastTweet.getId(), 1).blockingGet();

        Assert.assertEquals(1, tweetDtos.size());
        Assert.assertNotNull(tweetDtos.get(0).getText());
        Assert.assertFalse(TextUtils.isEmpty(tweetDtos.get(0).getText()));
    }

    @Test
    public void getUserInfoTest() {
        UserDto userDto = mApi.getUserInfo(mScreenName).blockingGet();

        Assert.assertNotNull(userDto);
        Assert.assertEquals(mScreenName, userDto.getScreen_name());
    }


}
