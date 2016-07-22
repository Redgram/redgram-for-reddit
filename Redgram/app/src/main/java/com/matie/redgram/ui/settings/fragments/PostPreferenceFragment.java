package com.matie.redgram.ui.settings.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import com.matie.redgram.R;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.ui.settings.SettingsActivity;

/**
 * This fragment shows posts preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PostPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Prefs prefs = ((SettingsActivity)getActivity()).getUser().getPrefs();
        if(prefs != null){
            SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();

            editor.putBoolean(SettingsActivity.pref_posts_show_flair, prefs.isShowLinkFlair());
            editor.putBoolean(SettingsActivity.pref_posts_hide_ups, prefs.isHideUps());
            editor.putBoolean(SettingsActivity.pref_posts_hide_downs, prefs.isHideDowns());

            editor.putString(SettingsActivity.pref_posts_media, prefs.getMedia());
            editor.putString(SettingsActivity.pref_posts_min_score, prefs.getMinLinkScore() == 0 ? "" : prefs.getMinLinkScore()+"");
            editor.putString(SettingsActivity.pref_posts_num_display, prefs.getNumSites()+"");

            editor.commit();
        }

        addPreferencesFromResource(R.xml.pref_posts);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences
        // to their values. When their values change, their summaries are
        // updated to reflect the new value, per the Android Design
        // guidelines.
        SettingsActivity.bindPreferenceSummaryToValue(findPreference(SettingsActivity.pref_posts_num_display));
        SettingsActivity.bindPreferenceSummaryToValue(findPreference(SettingsActivity.pref_posts_min_score));
        SettingsActivity.bindPreferenceSummaryToValue(findPreference(SettingsActivity.pref_posts_media));
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
