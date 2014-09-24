package org.honeroku.timepro.app.plugin.ui;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.twofortyfouram.locale.BreadCrumber;

import org.honeroku.timepro.R;

public abstract class AbstractPluginActivity extends Activity {

    private static final String TAG = AbstractPluginActivity.class.getName();

    private boolean isCanceled = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupTitle();
    }

    private void setupTitle() {
        CharSequence callingApplicationLabel = null;
        try {
            callingApplicationLabel = getPackageManager().getApplicationLabel(
                    getPackageManager().getApplicationInfo(getCallingPackage(), 0)
            );
        } catch (final NameNotFoundException e) {
            Log.e(TAG, "Calling package couldn't be found", e);
        }
        if (null != callingApplicationLabel) {
            setTitle(callingApplicationLabel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.twofortyfouram_locale_help_save_dontsave, menu);

        setupActionBar();

        return true;
    }

    private void setupActionBar() {
        getActionBar().setSubtitle(BreadCrumber.generateBreadcrumb(
                getApplicationContext(),
                getIntent(),
                getString(R.string.plugin_name)
        ));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            getActionBar().setIcon(getPackageManager().getApplicationIcon(getCallingPackage()));
        } catch (final NameNotFoundException e) {
            Log.w(TAG, "An error occurred loading the host's icon", e);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case (android.R.id.home):
                finish();
                return true;
            case (R.id.twofortyfouram_locale_menu_dontsave):
                isCanceled = true;
                finish();
                return true;
            case (R.id.twofortyfouram_locale_menu_save):
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean isCanceled() {
        return isCanceled;
    }

}
