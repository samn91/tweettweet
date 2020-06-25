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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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
        imageView2.check(matches(hasDrawble()));
    }

    private void textViewNotEmptyInRecyclerView(int p) {
        onView(findViewInhericy(withId(R.id.recycler), withId(p), 0))
                .check(matches(not(withText(""))));
    }

    private static Matcher<View> hasDrawble() {

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

    private static Matcher<View> findViewInhericy(
            final Matcher<View> parentMatcher, final Matcher<View> childMatcher, final int position) {

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
                            && card.equals(((ViewGroup) parent).getChildAt(position));
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


//    private Matcher<View> withItemText(final String itemText) {
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public boolean matchesSafely(View item) {
//                return allOf(
//                        isDescendantOfA(isAssignableFrom(ListView.class)),
//                        withText(itemText)).matches(item);
//            }
//
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("is isDescendantOfA LV with text " + itemText);
//            }
//        };
//    }
}
