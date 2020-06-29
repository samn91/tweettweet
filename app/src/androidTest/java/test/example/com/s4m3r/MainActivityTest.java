package test.example.com.s4m3r;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(TweetsViewModel.Companion.getIdlingResource());
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(TweetsViewModel.Companion.getIdlingResource());
    }

    @Test
    public void onLoadTest() {
        textViewNotEmptyInRecyclerView(R.id.nameTextView);
        textViewNotEmptyInRecyclerView(R.id.timeTextView);
        textViewNotEmptyInRecyclerView(R.id.descriptionTextView);
        textViewNotEmptyInRecyclerView(R.id.likeTextView);
        ViewInteraction imageView2 = onView(
                allOf(
                        withId(R.id.avatarImageView),
                        withParent(
                                childAtPosition(//cardView
                                        childAtPosition(withId(R.id.recycler), 0),//constrantLayout
                                        0))));
        imageView2.check(matches(hasDrawable()));
    }

    @Test
    public void validateChangeAccountName() {
        String newScreenName = "Android";
        changeAccountName(newScreenName);
        onView(firstViewInRecycler(withId(R.id.recycler), withId(R.id.nameTextView)))
                .check(matches(withText(newScreenName)));
        onLoadTest();
    }

    @Test
    public void inValidateChangeAccountName() {
        String newScreenName = "Androidfwefwfgwdwqdqdwqfwqwfqwfwegw";
        changeAccountName(newScreenName);

        onView(withText(R.string.error))
                .inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        onView(firstViewInRecycler(withId(R.id.recycler), withId(R.id.nameTextView)))
                .check(matches(not(withText(newScreenName))));
    }


    private void changeAccountName(String newScreenName) {
        onView(withId(R.id.account)).perform(click());
        ViewInteraction viewInteraction = onView(withText(SecretKt.SCREEN_NAME));
        viewInteraction.perform(click(), replaceText(newScreenName));
        onView(withText(newScreenName)).perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
    }


    private void textViewNotEmptyInRecyclerView(int p) {
        onView(firstViewInRecycler(withId(R.id.recycler), withId(p)))
                .check(matches(not(withText(""))));
    }

    private static Matcher<View> hasDrawable() {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Has a drawable in ImageView");
            }

            @Override
            public boolean matchesSafely(View view) {
                return ((ImageView) view).getDrawable() != null;
            }
        };
    }

    private static Matcher<View> firstViewInRecycler(
            final Matcher<View> parentMatcher, final Matcher<View> childMatcher) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child " + childMatcher + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (childMatcher.matches(view)) {
                    ViewParent card = view.getParent().getParent();
                    ViewParent parent = card.getParent();
                    return parent instanceof ViewGroup && parentMatcher.matches(parent)
                            && card.equals(((ViewGroup) parent).getChildAt(0));
                }

                return false;
            }
        };
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
