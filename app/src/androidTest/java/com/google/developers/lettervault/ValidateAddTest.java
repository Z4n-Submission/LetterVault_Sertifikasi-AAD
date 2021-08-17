package com.google.developers.lettervault;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.developers.lettervault.ui.home.HomeActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ValidateAddTest {
    @Before
    public void prepare(){
        ActivityScenario.launch(HomeActivity.class);
    }

    @Test
    public void addTest(){
        onView(withId(R.id.recent))
                .check(matches(isDisplayed()));
        onView(withId(R.id.lock))
                .check(matches(isDisplayed()));
        onView(withId(R.id.letterView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.action_add))
                .perform(ViewActions.click());
        onView(withId(R.id.subjectText))
                .check(matches(isDisplayed()));
        onView(withId(R.id.addMessageText))
                .check(matches(isDisplayed()));
        onView(withId(R.id.action_time))
                .check(matches(isDisplayed()));
        onView(withId(R.id.action_save))
                .check(matches(isDisplayed()));
    }
}

