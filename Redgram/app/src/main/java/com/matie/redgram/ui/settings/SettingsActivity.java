package com.matie.redgram.ui.settings;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.matie.redgram.R;
import com.matie.redgram.data.managers.storage.db.DatabaseHelper;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.ui.App;

import java.util.List;

import io.realm.Realm;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    //general
    public static final String general_show_trending = "general_show_trending";
    public static final String general_store_visits = "general_store_visits";
    public static final String general_over_18 = "general_over_18";
    public static final String general_label_nsfw = "general_label_nsfw";
    public static final String general_recent_post = "general_recent_post";

    //posts
    public static final String pref_posts_num_display = "pref_posts_num_display";
    public static final String pref_posts_min_score = "pref_posts_min_score";
    public static final String pref_posts_show_flair = "pref_posts_show_flair";
    public static final String pref_posts_media = "pref_posts_media";
    public static final String pref_posts_hide_ups = "pref_posts_hide_ups";
    public static final String pref_posts_hide_downs = "pref_posts_hide_downs";
    //comments
    public static final String pref_comments_sort = "pref_comments_sort";
    public static final String pref_comments_ignore_suggested = "pref_comments_ignore_suggested";
    public static final String pref_comments_highlight_controversial = "pref_comments_hightlight_controversial";
    public static final String pref_comments_show_flair = "pref_comments_show_flair";
    public static final String pref_comments_num_display = "pref_comments_num_display";
    public static final String pref_comments_min_score = "pref_comments_min_score";

    //sync
    public static final String pref_sync_period = "pref_sync_period";


    private Realm realm;
    private User user;

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            // For all preferences, set the summary to the value's
            // simple string representation.
            if(preference.getKey().equalsIgnoreCase(pref_posts_min_score) || preference.getKey().equalsIgnoreCase(pref_comments_min_score)) {
                if (stringValue.isEmpty()) {
                    stringValue = "Show all submissions";
                }
            }else if(preference.getKey().equalsIgnoreCase(pref_comments_num_display)){
                if(stringValue.isEmpty()){
                    stringValue = "200";
                }else if(stringValue.equalsIgnoreCase("0")){
                    stringValue = "1";
                }else if(Integer.parseInt(stringValue) > 500){
                    stringValue = "500";
                }
            }else if(preference.getKey().equalsIgnoreCase(pref_sync_period)){
                stringValue = ((ListPreference)preference).getEntry().toString();
            }

            preference.setSummary(stringValue);

            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        realm = ((App)getApplication()).getDatabaseManager().getInstance();
        Log.d("REALM_PATH", realm.getPath());
        user = DatabaseHelper.getSessionUser(realm);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = getLayoutInflater().inflate(R.layout.activity_settings_toolbar, null);
        FrameLayout content = (FrameLayout) view.findViewById(R.id.content);
        getLayoutInflater().inflate(layoutResID, content, true);
        setContentView(view);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || PostPreferenceFragment.class.getName().equals(fragmentName)
                || CommentsPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                || SyncPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        if(user != null){
            ((App)getApplication()).setUserPrefs(realm.copyFromRealm(user).getPrefs());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseHelper.close(realm);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Prefs prefs = user.getPrefs();
                //general
                if(key.equalsIgnoreCase(general_over_18)){
                    prefs.setOver18(sharedPreferences.getBoolean(key,prefs.isOver18()));
                }else if(key.equalsIgnoreCase(general_label_nsfw)){
                    prefs.setLabelNsfw(sharedPreferences.getBoolean(key,prefs.isLabelNsfw()));
                }else if(key.equalsIgnoreCase(general_recent_post)){
                    prefs.setEnableRecentPost(sharedPreferences.getBoolean(key, prefs.isEnableRecentPost()));
                }else if(key.equalsIgnoreCase(general_show_trending)){
                    prefs.setShowTrending(sharedPreferences.getBoolean(key, prefs.isShowTrending()));
                }else if(key.equalsIgnoreCase(general_store_visits)){
                    prefs.setStoreVisits(sharedPreferences.getBoolean(key, prefs.isStoreVisits()));
                }
                //posts
                else if(key.equalsIgnoreCase(pref_posts_show_flair)){
                    prefs.setShowLinkFlair(sharedPreferences.getBoolean(key, prefs.isShowLinkFlair()));
                }else if(key.equalsIgnoreCase(pref_posts_min_score) || key.equalsIgnoreCase(pref_comments_min_score)){

                    String val = sharedPreferences.getString(key, "");
                    int value = 0;
                    if(!val.isEmpty()){
                        value = Integer.parseInt(val);
                    }

                    if(key.equalsIgnoreCase(pref_posts_min_score)) {
                        prefs.setMinLinkScore(value);
                    } else{
                        prefs.setMinCommentsScore(value);
                    }

                }else if(key.equalsIgnoreCase(pref_posts_num_display)) {
                    String val = sharedPreferences.getString(key, prefs.getNumSites()+"");
                    prefs.setNumSites(Integer.parseInt(val));
                } else if(key.equalsIgnoreCase(pref_posts_media)){
                    prefs.setMedia(sharedPreferences.getString(key, "Subreddit"));
                } else if(key.equalsIgnoreCase(pref_posts_hide_ups)){
                    prefs.setHideUps(sharedPreferences.getBoolean(key, prefs.isHideUps()));
                } else if(key.equalsIgnoreCase(pref_posts_hide_downs)){
                    prefs.setHideDowns(sharedPreferences.getBoolean(key, prefs.isHideDowns()));
                }

                //comments
                else if(key.equalsIgnoreCase(pref_comments_num_display)){
                    String val = sharedPreferences.getString(key, prefs.getNumComments()+"");
                    if (val.isEmpty()) {
                        prefs.setNumComments(200);
                    }else if(Integer.parseInt(val) == 0){
                        prefs.setNumComments(1);
                    }else if(Integer.parseInt(val) > 500){
                        prefs.setNumComments(500);
                    }else{
                        prefs.setNumComments(Integer.parseInt(val));
                    }
                }else if(key.equalsIgnoreCase(pref_comments_sort)){
                    prefs.setDefaultCommentSort(sharedPreferences.getString(key, prefs.getDefaultCommentSort()));
                }else if(key.equalsIgnoreCase(pref_comments_show_flair)){
                    prefs.setShowFlair(sharedPreferences.getBoolean(key, prefs.isShowFlair()));
                }else if(key.equalsIgnoreCase(pref_comments_highlight_controversial)){
                    prefs.setHighlightControversial(sharedPreferences.getBoolean(key, prefs.isHighlightControversial()));
                }else if(key.equalsIgnoreCase(pref_comments_ignore_suggested)){
                    prefs.setIgnoreSuggestedSort(sharedPreferences.getBoolean(key, prefs.isIgnoreSuggestedSort()));
                }
            }
        });
    }

    public User getUser() {
        return user;
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);

            Prefs prefs = ((App)getActivity().getApplication()).getDatabaseManager().getSessionUser().getPrefs();
            if(prefs != null){
                SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();

                editor.putBoolean(general_label_nsfw, prefs.isLabelNsfw());
                editor.putBoolean(general_over_18, prefs.isOver18());
                editor.putBoolean(general_recent_post, prefs.isEnableRecentPost());
                editor.putBoolean(general_show_trending, prefs.isShowTrending());
                editor.putBoolean(general_store_visits, prefs.isStoreVisits());

                editor.commit();
            }

            addPreferencesFromResource(R.xml.pref_general);

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

    /**
     * This fragment shows posts preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class PostPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);

            Prefs prefs = ((App)getActivity().getApplication()).getDatabaseManager().getSessionUser().getPrefs();
            if(prefs != null){
                SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();

                editor.putBoolean(pref_posts_show_flair, prefs.isShowLinkFlair());
                editor.putBoolean(pref_posts_hide_ups, prefs.isHideUps());
                editor.putBoolean(pref_posts_hide_downs, prefs.isHideDowns());

                editor.putString(pref_posts_media, prefs.getMedia());
                editor.putString(pref_posts_min_score, prefs.getMinLinkScore() == 0 ? "" : prefs.getMinLinkScore()+"");
                editor.putString(pref_posts_num_display, prefs.getNumSites()+"");

                editor.commit();
            }

            addPreferencesFromResource(R.xml.pref_posts);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(pref_posts_num_display));
            bindPreferenceSummaryToValue(findPreference(pref_posts_min_score));
            bindPreferenceSummaryToValue(findPreference(pref_posts_media));
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

    /**
     * This fragment shows comments preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class CommentsPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);

            Prefs prefs = ((App)getActivity().getApplication()).getDatabaseManager().getSessionUser().getPrefs();
            if(prefs != null){
                SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();

                editor.putBoolean(pref_comments_highlight_controversial, prefs.isHighlightControversial());
                editor.putBoolean(pref_comments_ignore_suggested, prefs.isIgnoreSuggestedSort());
                editor.putBoolean(pref_comments_show_flair, prefs.isShowFlair());

                editor.putString(pref_comments_sort, prefs.getDefaultCommentSort());
                editor.putString(pref_comments_min_score, prefs.getMinCommentsScore() == 0 ? "" : prefs.getMinCommentsScore()+"");
                editor.putString(pref_comments_num_display, prefs.getNumComments()+"");

                editor.commit();
            }

            addPreferencesFromResource(R.xml.pref_comments);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(pref_comments_sort));
            bindPreferenceSummaryToValue(findPreference(pref_comments_num_display));
            bindPreferenceSummaryToValue(findPreference(pref_comments_min_score));
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

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);
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

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_sync);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference(pref_sync_period));
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

}
