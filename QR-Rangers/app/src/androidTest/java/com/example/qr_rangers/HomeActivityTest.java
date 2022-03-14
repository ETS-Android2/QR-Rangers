package com.example.qr_rangers;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HomeActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<HomeActivity> rule =
            new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void profileMenu(){
        solo.assertCurrentActivity("Wrong Activity",HomeActivity.class);

        solo.clickOnView(solo.getView(R.id.home_drawer_menu));

        solo.clickOnView(solo.getView(R.id.hamburger_profile_button));

        solo.assertCurrentActivity("Wrong Activity",ProfileActivity.class);
    }

    @Test
    public void searchMenu(){
        solo.assertCurrentActivity("Wrong Activity",HomeActivity.class);

        solo.clickOnView(solo.getView(R.id.home_drawer_menu));

        solo.clickOnView(solo.getView(R.id.hamburger_search_button));

        solo.assertCurrentActivity("Wrong Activity",SearchActivity.class);
    }

    @Test
    public void galleryMenu(){
        solo.assertCurrentActivity("Wrong Activity",HomeActivity.class);

        solo.clickOnView(solo.getView(R.id.home_drawer_menu));

        solo.clickOnView(solo.getView(R.id.hamburger_gallery_button));

        solo.assertCurrentActivity("Wrong Activity",QRListActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
