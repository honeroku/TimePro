package org.honeroku.timepro.app.plugin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;

import org.honeroku.timepro.R;
import org.honeroku.timepro.app.plugin.bundle.BundleManager;
import org.honeroku.timepro.app.plugin.bundle.BundleScrubber;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SettingActivity extends AbstractPluginActivity {

    @Bind(R.id.attendance) Switch attendanceSwitch;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);

        BundleScrubber.scrub(getIntent());

        Bundle localeBundle = getIntent().getBundleExtra(
                com.twofortyfouram.locale.Intent.EXTRA_BUNDLE
        );
        BundleScrubber.scrub(localeBundle);

        if (savedInstanceState == null) {
            if (BundleManager.isBundleValid(localeBundle)) {
                Boolean isClockIn = localeBundle.getBoolean(
                        BundleManager.BUNDLE_EXTRA_CLOCK_IN_OUT
                );
                attendanceSwitch.setChecked(isClockIn);
            }
        }
    }

    @Override
    public void finish() {
        if (!isCanceled()) {
            boolean isClockIn = attendanceSwitch.isChecked();
            String blurb = isClockIn ? "出勤記録" : "退勤記録";

            Intent resultIntent = new Intent();
            Bundle resultBundle = BundleManager.generateBundle(isClockIn);
            resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);
            resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, blurb);

            setResult(RESULT_OK, resultIntent);
        }

        super.finish();
    }

}
