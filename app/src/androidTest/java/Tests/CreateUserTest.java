package Tests;

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;

import eu.silvenia.bridgeballot.R;
import eu.silvenia.bridgeballot.activity.CreateUser;

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
public class CreateUserTest extends ActivityInstrumentationTestCase2<CreateUser> {

    CreateUser createUser;

    public CreateUserTest() {
        super(CreateUser.class);
    }

    @Before
    public void setUp() throws Exception{
        super.setUp();
        injectInstrumentation(getInstrumentation());
        createUser = getActivity();
    }

    public void testCreateIncompleteFields(){
        onView(withId(R.id.create_userName)).perform(typeText("test234"), closeSoftKeyboard());
        onView(withId(R.id.button3)).perform(click());
        onView(withText(R.string.alert_emptyfields)).check(matches(isDisplayed()));
    }

    public void testCreatePasswordNotMatch(){
        onView(withId(R.id.create_userName)).perform(typeText("test123"), closeSoftKeyboard());
        onView(withId(R.id.create_password)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.create_confirmPassword)).perform(typeText("password1"), closeSoftKeyboard());
        onView(withId(R.id.button3)).perform(click());
        onView(withText(R.string.alert_message)).check(matches(isDisplayed()));
    }
}