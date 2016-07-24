package com.matie.redgram.ui.settings.fragments;

/**
 * Created by matie on 2016-07-19.
 */

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
 * This fragment shows comments preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CommentsPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Prefs prefs = ((SettingsActivity) getActivity()).getUser().getPrefs();
        if(prefs != null){
            SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();

            editor.putBoolean(SettingsActivity.pref_comments_highlight_controversial, prefs.isHighlightControversial());
            editor.putBoolean(SettingsActivity.pref_comments_ignore_suggested, prefs.isIgnoreSuggestedSort());
            editor.putBoolean(SettingsActivity.pref_comments_show_flair, prefs.isShowFlair());

            editor.putString(SettingsActivity.pref_comments_sort, prefs.getDefaultCommentSort());
            editor.putString(SettingsActivity.pref_comments_min_score, prefs.getMinCommentsScore() == 0 ? "" : prefs.getMinCommentsScore()+"");
            editor.putString(SettingsActivity.pref_comments_num_display, prefs.getNumComments()+"");

            editor.commit();
        }

        addPreferencesFromResource(R.xml.pref_comments);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences
        // to their values. When their values change, their summaries are
        // updated to reflect the new value, per the Android Design
        // guidelines.
        SettingsActivity.bindPreferenceSummaryToValue(findPreference(SettingsActivity.pref_comments_sort));
        SettingsActivity.bindPreferenceSummaryToValue(findPreference(SettingsActivity.pref_comments_num_display));
        SettingsActivity.bindPreferenceSummaryToValue(findPreference(SettingsActivity.pref_comments_min_score));
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

