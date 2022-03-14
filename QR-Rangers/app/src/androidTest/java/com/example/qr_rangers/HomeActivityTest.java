package com.example.qr_rangers;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * THIS TEST ASSUMES YOU HAVE DATA ON YOUR PHONE FOR THIS APP
 */
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

        solo.clickOnActionBarHomeButton();

        solo.clickOnText("Profile");

        solo.assertCurrentActivity("Wrong Activity",ProfileActivity.class);

        solo.clickOnView(solo.getView(R.id.viewgallerybutton));

        solo.assertCurrentActivity("Wrong Activity",QRListActivity.class);

        solo.clickOnActionBarHomeButton();
    }

    @Test
    public void searchMenu(){
        solo.assertCurrentActivity("Wrong Activity",HomeActivity.class);

        solo.clickOnActionBarHomeButton();

        solo.clickOnText("Search");

        solo.assertCurrentActivity("Wrong Activity",SearchActivity.class);
    }

    @Test
    public void galleryMenu(){
        solo.assertCurrentActivity("Wrong Activity",HomeActivity.class);

        solo.clickOnActionBarHomeButton();

        solo.clickOnText("Gallery");

        solo.assertCurrentActivity("Wrong Activity",QRListActivity.class);

        Search search = new Search();

        User test = search.FindUser("TestUser").get(0);

        if(test.getQRList().size() > 0){
            solo.clickOnText(Integer.toString(test.getScoreMax()) + " pts");

            solo.assertCurrentActivity("Wrong Activity",QRInfoActivity.class);
        }
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
