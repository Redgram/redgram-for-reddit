package com.matie.redgram.ui.settings.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.MenuItem;

import com.matie.redgram.R;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.ui.settings.SettingsActivity;

/**
 * This fragment shows general preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GeneralPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Prefs prefs = ((SettingsActivity)getActivity()).getUser().getPrefs();
        if(prefs != null){
            SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();

            editor.putBoolean(SettingsActivity.general_over_18, prefs.isOver18());
            if(!prefs.isOver18()){
                editor.putBoolean(SettingsActivity.general_label_nsfw, true);
                editor.putBoolean(SettingsActivity.general_preview_nsfw, true); //app-only
            }else{
                editor.putBoolean(SettingsActivity.general_label_nsfw, prefs.isLabelNsfw());
                editor.putBoolean(SettingsActivity.general_preview_nsfw, prefs.isDisableNsfwPreview());
            }
            editor.putBoolean(SettingsActivity.general_recent_post, prefs.isEnableRecentPost());
            editor.putBoolean(SettingsActivity.general_show_trending, prefs.isShowTrending());
            editor.putBoolean(SettingsActivity.general_store_visits, prefs.isStoreVisits());

            editor.commit();
        }

        addPreferencesFromResource(R.xml.pref_general);

        getPreferenceManager().findPreference(SettingsActivity.general_over_18).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean flag = (boolean)newValue;

                if(!flag){ //unchecked - false - if user is under 18

                    SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
                    editor.putBoolean(SettingsActivity.general_label_nsfw, !flag); //false
                    editor.putBoolean(SettingsActivity.general_preview_nsfw, !flag);
                    editor.commit();

                    toggleOver18Dependants(!flag);

                }

                return true;
            }
        });

    }

    private void toggleOver18Dependants(boolean flag){
        SwitchPreference labelPref = (SwitchPreference) getPreferenceManager().findPreference(SettingsActivity.general_label_nsfw);
        if(labelPref.isChecked() != flag){
            labelPref.setChecked(flag);
        }
        SwitchPreference prevPref = (SwitchPreference) getPreferenceManager().findPreference(SettingsActivity.general_preview_nsfw);
        if(prevPref.isChecked() != flag){
            prevPref.setChecked(flag);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

