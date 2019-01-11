package UI.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import Business.Locator;
import UI.util.DialogUtils;
import mspinu.imfinemom.R;

import static commons.util.Constants.SETTINGS_CHANGED;

/**
 * Created by Marius on 9/19/2017.
 */

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences settingsPref;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Preferences");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getFragmentManager().beginTransaction().replace(R.id.preferenceContent, new SettingsFragment())
                .commit();

        PreferenceManager.setDefaultValues(getBaseContext(), R.xml.preferences_layout, false);

        settingsPref = Locator.getPreferences(this);
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Intent intent = new Intent();
                intent.setData(Uri.parse(SETTINGS_CHANGED));
                setResult(RESULT_OK, intent);
            }
        };

        settingsPref.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onDestroy() {
        settingsPref.unregisterOnSharedPreferenceChangeListener(prefListener);
        super.onDestroy();
    }


    public static class SettingsFragment extends PreferenceFragment {
        private Preference about;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences_layout);

            about = findPreference(getString(R.string.aboutButton));
            about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    DialogUtils.showAboutDialog(getActivity());
                    return true;
                }
            });
        }

    }
}
