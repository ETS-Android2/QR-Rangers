package com.example.qr_rangers;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SearchActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<SearchActivity> rule =
            new ActivityTestRule<>(SearchActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void searchTest() throws Throwable {
        solo.assertCurrentActivity("Wrong Activity",SearchActivity.class);

        SearchView search = (SearchView) solo.getView(R.id.search);

        rule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                search.setQuery("TestUser", false);
            }
        });

        solo.waitForText("TestUser", 1, 10000);

        SearchActivity activity = (SearchActivity) solo.getCurrentActivity();

        final ListView userList = activity.search_list;

        String user = ((User) userList.getItemAtPosition(0)).getUsername();

        assertEquals("TestUser", user);

        solo.clickOnView(solo.getView(R.id.search_list));

        solo.assertCurrentActivity("Wrong Activity",ProfileActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
