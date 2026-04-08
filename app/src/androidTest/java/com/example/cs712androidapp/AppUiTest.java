package com.example.cs712androidapp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AppUiTest {

    private UiDevice device;
    private static final int TIMEOUT = 5000;
    private final String packageName = "com.example.cs712androidapp";

    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.pressHome(); // Start from home screen (required by assignment)
    }

    @Test
    public void testLaunchAppAndCheckSecondActivity() {

        // Step 1: Launch app from home screen
        Context context = ApplicationProvider.getApplicationContext();

        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        assertNotNull("App launch intent is null", intent);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Step 2: Wait for app to open
        device.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT);

        // Step 3: Click "Start Activity Explicitly" button
        device.findObject(By.text("Start Activity Explicitly")).click();

        // Step 4: Wait for second activity to load
        device.waitForIdle();

        // Step 5: Verify content in second activity (title check - safest)
        boolean foundChallengeText =
                device.wait(Until.hasObject(
                        By.textContains("Mobile Software Engineering Challenges")), 5000);

        // Step 6: Assert result
        assertTrue("Challenge text was not found in second activity", foundChallengeText);
    }
}