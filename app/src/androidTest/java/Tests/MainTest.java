package Tests;

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;

import eu.silvenia.bridgeballot.R;
import eu.silvenia.bridgeballot.activity.Main;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Jesse on 20-6-2015.
 */
public class MainTest extends ActivityInstrumentationTestCase2<Main> {

    private Main main;

    public MainTest() {
        super(Main.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(getInstrumentation());
        main = getActivity();
    }

    public void testLoginIncompleteCredentials(){
        onView(withId(R.id.userName)).perform(typeText("test123"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());
        onView(withText(R.string.alert_emptyfields)).check(matches(isDisplayed()));
    }



}