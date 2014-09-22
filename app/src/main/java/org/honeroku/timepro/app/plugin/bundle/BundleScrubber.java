package org.honeroku.timepro.app.plugin.bundle;

import android.content.Intent;
import android.os.Bundle;

public final class BundleScrubber {

    public static boolean scrub(final Intent intent) {
        if (null == intent) {
            return false;
        }

        return scrub(intent.getExtras());
    }

    public static boolean scrub(final Bundle bundle) {
        if (null == bundle) {
            return false;
        }

        try {
            // if a private serializable exists, this will throw an exception
            bundle.containsKey(null);
        }
        catch (Exception e) {
            bundle.clear();
            return true;
        }

        return false;
    }

}
