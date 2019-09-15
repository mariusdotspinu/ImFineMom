package UI.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import UI.util.DialogUtils;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import Business.Locator;
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
        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        toolbar.setTitle(R.string.settings_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
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

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_layout);
            Preference about = findPreference(getString(R.string.aboutButton));
            about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    DialogUtils.showRationaleDialog("I'm Fine Mom\n" +
                                    "Copyright © 2019\n" +
                                    "All rights reserved\n" +
                                    "Owned by Marius Spînu\n" +
                                    "mariusdotspinu@gmail.com",
                            getActivity(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    return true;
                }
            });
        }
    }
}
