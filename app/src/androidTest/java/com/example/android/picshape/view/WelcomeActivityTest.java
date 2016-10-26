package com.example.android.picshape.view;


import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.content.ContextCompat;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.android.picshape.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class WelcomeActivityTest {

    final private String LOG_TAG = "WELCOME ACTIVITY TEST";

    private static final int PERMISSIONS_DIALOG_DELAY = 1500;

    @Rule
    public IntentsTestRule<WelcomeActivity> mActivityTestRule = new IntentsTestRule<>(WelcomeActivity.class);


    @Test
    public void validateGalleryScenario() {


        String intentAction = Intent.ACTION_PICK;


        // Stub the Uri returned by the gallery intent.
        Uri uri = Uri.parse("uri_string");
        // Build a result to return when the activity is launched.
        Intent resultData = new Intent();
        resultData.setData(uri);

        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Stub out the Camera. When an intent is sent to the Camera, this tells Espresso to respond
        // with the ActivityResult we just created
        intending(hasAction(intentAction)).respondWith(result);

        // Specific for PicShape
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.select_btn), withText("Select a picture"),
                        withParent(allOf(withId(R.id.activity_welcome),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("Choose from Library"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatTextView.perform(click());

        // Check permission to access gallery
        allowPermissionsIfNeeded(Manifest.permission.READ_EXTERNAL_STORAGE);

        // We can also validate that an intent resolving to the "camera" activity has been sent out by our app
        intended(hasAction(intentAction));


        // ... additional test steps and validation ...
        ViewInteraction imageView = onView(
                allOf(withId(R.id.pic_imageView),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_welcome),
                                        3),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));
        Intents.assertNoUnverifiedIntents();

    }

    @Test
    public void validateCameraScenario() {


        // Create a bitmap we can use for our simulated camera image
        Bitmap icon = BitmapFactory.decodeResource(
                InstrumentationRegistry.getTargetContext().getResources(),
                R.mipmap.ic_launcher);

        // Build a result to return from the Camera app
        Intent resultData = new Intent();
        resultData.putExtra("data", icon);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Stub out the Camera. When an intent is sent to the Camera, this tells Espresso to respond
        // with the ActivityResult we just created
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

        // Specific for PicShape
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.select_btn), withText("Select a picture"),
                        withParent(allOf(withId(R.id.activity_welcome),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("Take Photo"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        allowPermissionsIfNeeded(Manifest.permission.CAMERA);

        // We can also validate that an intent resolving to the "camera" activity has been sent out by our app
        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));

        // ... additional test steps and validation ...
        ViewInteraction imageView = onView(
                allOf(withId(R.id.pic_imageView),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_welcome),
                                        3),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        Intents.assertNoUnverifiedIntents();

    }

    public static void allowPermissionsIfNeeded(String permissionNeeded) {
        try {
            Context context = InstrumentationRegistry.getTargetContext();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasNeededPermission(permissionNeeded)) {
                sleep(PERMISSIONS_DIALOG_DELAY);
                UiDevice device = UiDevice.getInstance(getInstrumentation());
                UiObject allowPermissions = device.findObject(new UiSelector().clickable(true).checkable(false).index(1));
                if (allowPermissions.exists()) {
                    allowPermissions.click();
                }
            }
        } catch (UiObjectNotFoundException e) {
            System.out.println("There is no permissions dialog to interact with");
        }
    }

    private static boolean hasNeededPermission(String permissionNeeded) {
        Context context = InstrumentationRegistry.getTargetContext();
        int permissionStatus = ContextCompat.checkSelfPermission(context, permissionNeeded);
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
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
