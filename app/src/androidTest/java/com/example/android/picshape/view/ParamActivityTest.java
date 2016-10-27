package com.example.android.picshape.view;

import android.graphics.Point;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.android.picshape.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 26/10/2016.
 */


@LargeTest
@RunWith(AndroidJUnit4.class)
public class ParamActivityTest {

    final private String LOG_TAG = "PARAM ACTIVITY TEST";

    @Before
    public void init(){
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Point[] coordinates = new Point[4];
        coordinates[0] = new Point(248, 1520);
        coordinates[1] = new Point(248, 929);
        coordinates[2] = new Point(796, 1520);
        coordinates[3] = new Point(796, 929);
        try {
            if (!uiDevice.isScreenOn()) {
                uiDevice.wakeUp();
                uiDevice.swipe(coordinates, 10);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Rule
    public IntentsTestRule<ParamActivity> mActivityTestRule = new IntentsTestRule<>(ParamActivity.class);


    @Test
    public void validateParametersScenario(){
        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spinner_mode),
                        withParent(allOf(withId(R.id.parameters_layout),
                                withParent(withId(R.id.activity_param)))),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("ellipse"), isDisplayed()));
        appCompatTextView2.perform(click());


        ViewInteraction textView = onView(
                allOf(withId(android.R.id.text1), withText("ellipse"),
                        childAtPosition(
                                allOf(withId(R.id.spinner_mode),
                                        childAtPosition(
                                                withId(R.id.parameters_layout),
                                                1)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("ellipse")));

        // Iteration

        onView(withId(R.id.iteration_editText)).check(matches(isDisplayed()));
        onView(withId(R.id.iteration_editText)).perform(replaceText("105"), closeSoftKeyboard());

        sleep(1000);

        onView(withId(R.id.iteration_editText)).check(matches(withText("105")));


        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.spinner_format),
                        withParent(allOf(withId(R.id.parameters_layout),
                                withParent(withId(R.id.activity_param)))),
                        isDisplayed()));
        appCompatSpinner2.perform(click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("GIF"), isDisplayed()));
        appCompatTextView3.perform(click());


        sleep(2000);

        ViewInteraction textView2 = onView(
                allOf(withId(android.R.id.text1), withText("GIF"),
                        childAtPosition(
                                allOf(withId(R.id.spinner_format),
                                        childAtPosition(
                                                withId(R.id.activity_param),
                                                6)),
                                0),
                        isDisplayed()));

        onView(withId(R.id.spinner_format)).check(matches(withSpinnerText(containsString("GIF"))));

    }


    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException("Cannot execute Thread.sleep()");
        }
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
