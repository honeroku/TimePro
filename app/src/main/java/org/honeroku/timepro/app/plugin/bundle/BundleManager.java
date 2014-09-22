package org.honeroku.timepro.app.plugin.bundle;

import android.os.Bundle;
import android.util.Log;

public final class BundleManager {

    private static final String TAG = BundleManager.class.getName();

    public static final String BUNDLE_EXTRA_CLOCK_IN_OUT =
            "org.honeroku.timepro.extra.CLOCK_IN_OUT";

    public static boolean isBundleValid(final Bundle bundle) {
        if (null == bundle) {
            return false;
        }

        if (!bundle.containsKey(BUNDLE_EXTRA_CLOCK_IN_OUT)) {
            Log.e(TAG, "missing key");
            return false;
        }

        if (bundle.keySet().size() != 1) {
            Log.e(TAG, "invalid key count");
            return false;
        }

        return true;
    }

    public static Bundle generateBundle(boolean isClockIn) {
        Bundle result = new Bundle();
        result.putBoolean(BUNDLE_EXTRA_CLOCK_IN_OUT, isClockIn);

        return result;
    }

}
