package org.nla.android.guestconnect.test;

import android.test.ActivityInstrumentationTestCase2;
import org.nla.android.guestconnect.activities.MainActivity;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class); 
    }

    public void testActivity() {
    	MainActivity activity = getActivity();
        assertNotNull(activity);
    }
}

