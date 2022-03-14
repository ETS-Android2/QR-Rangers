package com.example.qr_rangers;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * THIS TEST ASSUMES TestUser DOESN'T EXIST, REMOVE FROM THE DATABASE ON SUBSEQUENT TESTS
 */
public class IntroActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<IntroActivity> rule =
            new ActivityTestRule<>(IntroActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void loginTest() {
        solo.assertCurrentActivity("Wrong Activity", IntroActivity.class);

        solo.clickOnView(solo.getView(R.id.create));

        solo.assertCurrentActivity("Wrong Activity", CreateAccountActivity.class);

        solo.enterText((EditText) solo.getView(R.id.editUsername), "TestUser");

        solo.clickOnView(solo.getView(R.id.buttonCreate));

        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        String welcome = ((TextView) solo.getView(R.id.welcome)).getText().toString();

        assertEquals("Welcome, TestUser!", welcome);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
