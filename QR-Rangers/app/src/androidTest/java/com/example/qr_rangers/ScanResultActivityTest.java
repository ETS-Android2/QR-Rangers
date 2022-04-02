package com.example.qr_rangers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import android.content.Intent;
import android.os.Bundle;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;

public class ScanResultActivityTest {
    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), ScanResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("content", "hello");
        bundle.putString("totalScore", "0");
        intent.putExtras(bundle);
    }

    @Rule
    public ActivityScenarioRule<ScanResultActivity> rule =
            new ActivityScenarioRule<>(intent);

    @Test
    public void correctCurrentPtsTest(){

        onView(withId(R.id.newtotalscore)).check(matches(withText("0")));

    }

    @Test
    public void correctTotalPtsTest(){
        onView(withId(R.id.textViewScore)).check(matches(withText("3 pts.")));
    }

}
