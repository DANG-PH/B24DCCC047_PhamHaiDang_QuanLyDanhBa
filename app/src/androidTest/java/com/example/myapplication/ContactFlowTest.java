package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.espresso.intent.Intents;
import java.io.File;
import java.io.FileOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ContactFlowTest {
    @Before public void seedContacts() {
        ContactRepository repo = ContactRepository.getInstance();
        for (Contact contact : repo.getContacts()) repo.delete(contact.getId());
        for (Contact contact : new ContactRepository().getContacts()) repo.add(contact);
    }
    private void screenshot(String name) throws Exception {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        // Let the frame and transient UI settle before capturing a real device screenshot.
        Thread.sleep(900);
        File dir = new File(InstrumentationRegistry.getInstrumentation().getTargetContext()
                .getExternalFilesDir(null), "screenshots");
        assertTrue(dir.isDirectory() || dir.mkdirs());
        Bitmap bitmap = InstrumentationRegistry.getInstrumentation().getUiAutomation().takeScreenshot();
        assertNotNull(bitmap);
        try (FileOutputStream output = new FileOutputStream(new File(dir, name + ".png"))) {
            assertTrue(bitmap.compress(Bitmap.CompressFormat.PNG, 100, output));
        }
        bitmap.recycle();
    }
    @Test public void loginRejectsBlankAndIncorrectCredentialsThenOpensList() throws Exception {
        try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class)) {
            screenshot("01_login");
            onView(withId(R.id.loginButton)).perform(scrollTo(), click());
            onView(withId(R.id.loginError)).check(matches(withText(R.string.login_empty)));
            onView(withId(R.id.usernameInput)).perform(replaceText("admin"));
            onView(withId(R.id.passwordInput)).perform(replaceText("wrong"), closeSoftKeyboard());
            onView(withId(R.id.loginButton)).perform(scrollTo(), click());
            onView(withId(R.id.loginError)).check(matches(withText(R.string.login_wrong)));
            screenshot("02_login_error");
            onView(withId(R.id.passwordInput)).perform(replaceText("123456"), closeSoftKeyboard());
            onView(withId(R.id.loginButton)).perform(scrollTo(), click());
            onView(withId(R.id.totalCount)).check(matches(withText("3 liên hệ")));
            onView(withId(R.id.favoriteCount)).check(matches(withText("1 yêu thích")));
            screenshot("03_contacts");
            assertEquals(androidx.lifecycle.Lifecycle.State.DESTROYED, scenario.getState());
        }
    }
    @Test public void addRejectsInvalidAndDuplicatePhoneThenSavesAllFields() throws Exception {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.addContactButton)).perform(click());
            onView(withId(R.id.saveContactButton)).perform(scrollTo(), click());
            onView(withId(R.id.nameInput)).check(matches(hasErrorText("Vui lòng nhập họ và tên")));
            onView(withId(R.id.nameInput)).perform(replaceText("Phạm Hải Đăng"), closeSoftKeyboard());
            onView(withId(R.id.phoneInput)).perform(replaceText("123"), closeSoftKeyboard());
            onView(withId(R.id.saveContactButton)).perform(scrollTo(), click());
            onView(withId(R.id.phoneInput)).check(matches(hasErrorText("Số điện thoại phải có 10 chữ số và bắt đầu bằng 0")));
            screenshot("04_add_invalid");
            onView(withId(R.id.phoneInput)).perform(replaceText("0901234567"), closeSoftKeyboard());
            onView(withId(R.id.saveContactButton)).perform(scrollTo(), click());
            onView(withId(R.id.phoneInput)).check(matches(hasErrorText("Số điện thoại đã tồn tại trong danh bạ")));
            assertEquals(3, ContactRepository.getInstance().getContacts().size());
            screenshot("05_add_duplicate");
            onView(withId(R.id.phoneInput)).perform(replaceText("0966666666"), closeSoftKeyboard());
            onView(withId(R.id.groupInput)).perform(scrollTo(), click());
            onData(allOf(is(instanceOf(String.class)), is("Bạn bè"))).perform(click());
            onView(withId(R.id.genderMale)).check(matches(isChecked()));
            onView(withId(R.id.favoriteInput)).perform(scrollTo(), click());
            onView(withId(R.id.notesInput)).perform(scrollTo(), replaceText("Bạn học lớp D24CQPM03"), closeSoftKeyboard());
            onView(withId(R.id.backButton)).perform(scrollTo());
            screenshot("06_add_form");
            onView(withId(R.id.saveContactButton)).perform(scrollTo(), click());
            onView(withId(R.id.totalCount)).check(matches(withText("4 liên hệ")));
            onView(withId(R.id.favoriteCount)).check(matches(withText("2 yêu thích")));
            onData(instanceOf(Contact.class)).inAdapterView(withId(R.id.contactList)).atPosition(3).check(matches(isDisplayed()));
            screenshot("07_contacts_added");
            onData(instanceOf(Contact.class)).inAdapterView(withId(R.id.contactList)).atPosition(3).perform(click());
            onView(withId(R.id.detailName)).check(matches(withText("Phạm Hải Đăng")));
            onView(withId(R.id.detailPhone)).check(matches(withText("0966666666")));
            onView(withId(R.id.detailGroup)).check(matches(withText("Bạn bè")));
            onView(withId(R.id.detailNotes)).check(matches(withText("Bạn học lớp D24CQPM03")));
            onView(withId(R.id.detailFavorite)).check(matches(isChecked()));
            screenshot("08_detail");
            onView(withId(R.id.backButton)).perform(scrollTo(), click());
        }
    }
    @Test public void favoriteDialAndDeleteUpdateList() throws Exception {
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            onData(instanceOf(Contact.class)).inAdapterView(withId(R.id.contactList)).atPosition(0).perform(click());
            onView(withId(R.id.detailFavorite)).perform(scrollTo(), click());
            onView(withId(R.id.backButton)).perform(scrollTo(), click());
            onView(withId(R.id.favoriteCount)).check(matches(withText("2 yêu thích")));
            onData(instanceOf(Contact.class)).inAdapterView(withId(R.id.contactList)).atPosition(0).perform(click());
            onView(withId(R.id.callButton)).perform(scrollTo(), click());
            intended(allOf(hasAction(Intent.ACTION_DIAL), hasData("tel:0901234567")));
            // The external dialer may first close its keypad before leaving the activity.
            Thread.sleep(5000);
            screenshot("09_dialer");
            for (int attempt = 0; attempt < 5; attempt++) {
                AccessibilityNodeInfo root = InstrumentationRegistry.getInstrumentation()
                        .getUiAutomation().getRootInActiveWindow();
                if (root != null && "com.example.myapplication".contentEquals(root.getPackageName())) break;
                InstrumentationRegistry.getInstrumentation().getUiAutomation()
                        .performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                Thread.sleep(700);
            }
            onView(withId(R.id.deleteButton)).perform(scrollTo(), click());
            screenshot("10_delete_confirmation");
            onView(withText(R.string.cancel)).perform(click());
            assertEquals(3, ContactRepository.getInstance().getContacts().size());
            onView(withId(R.id.deleteButton)).perform(scrollTo(), click());
            onView(withId(android.R.id.button1)).perform(click());
            onView(withId(R.id.totalCount)).check(matches(withText("2 liên hệ")));
            onView(withId(R.id.favoriteCount)).check(matches(withText("1 yêu thích")));
            screenshot("11_contacts_deleted");
        } finally { Intents.release(); }
    }
}
