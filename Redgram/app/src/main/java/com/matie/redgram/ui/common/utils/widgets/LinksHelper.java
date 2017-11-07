package com.matie.redgram.ui.common.utils.widgets;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.profile.ProfileActivity;
import com.matie.redgram.ui.subcription.SubscriptionActivity;

public class LinksHelper {

    public static final String SUB = "subreddit";
    public static final String PROFILE = "profile";

    private LinksHelper(){}

    @Deprecated
    public static void showExternalDialog(DialogUtil dialogUtil, String title, MaterialDialog.ListCallback callback) {
        dialogUtil.build()
                .title(title)
                .items(R.array.shareOptions)
                .itemsCallback(callback)
                .show();
    }

    public static void showExternalDialog(Context context, String title, MaterialDialog.ListCallback callback) {
        DialogUtil.builder(context)
                .title(title)
                .items(R.array.shareOptions)
                .itemsCallback(callback)
                .show();
    }

    public static void callShareDialog(Context context, String contentToShare){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, contentToShare);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.send_to)));
    }

    @Deprecated
    public static void callReportDialog(DialogUtil dialogUtil, MaterialDialog.SingleButtonCallback callback) {
        dialogUtil.build()
                .title("Report Post?")
                .positiveText("Report")
                .negativeText("Cancel")
                .onPositive(callback)
                .show();
    }

    public static void callReportDialog(Context context, MaterialDialog.SingleButtonCallback callback) {
        DialogUtil.builder(context)
                .title("Report Post?")
                .positiveText("Report")
                .negativeText("Cancel")
                .onPositive(callback)
                .show();
    }

    public static void openResult(Context context, String result, String type) {
        Intent intent = null;
        if (SUB.equalsIgnoreCase(type)) {
            intent = new Intent(context, MainActivity.class);
            intent.putExtra(SubscriptionActivity.RESULT_SUBREDDIT_NAME, result);
        } else if (PROFILE.equalsIgnoreCase(type)) {
            intent = ProfileActivity.intent(context);
            intent.putExtra(ProfileActivity.RESULT_USER_NAME, result);
        }

        if (intent != null) {
            ((BaseActivity) context).openIntent(intent);
        }
    }

    private static Uri getUriToOpen(PostItem item, CharSequence charSequence) {
        Uri urlToOpen = null;
        if (charSequence.toString().equalsIgnoreCase("Link")) {
            urlToOpen = Uri.parse(item.getUrl());
        }
        if (charSequence.toString().equalsIgnoreCase(("Comments"))) {
            urlToOpen = Uri.parse("https://reddit.com/" + item.getPermalink());
        }
        return urlToOpen;
    }

    public static MaterialDialog.ListCallback getBrowseCallback(Context context, PostItem item) {
        return (materialDialog, view, i, charSequence) -> {
            Uri urlToOpen = getUriToOpen(item, charSequence);
            if (urlToOpen != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, urlToOpen);
                context.startActivity(browserIntent);
            }
        };
    }

    @Deprecated
    public static MaterialDialog.ListCallback getCopyCallback(Context context, ToastHandler toastHandler, PostItem item) {
        return (materialDialog, view, i, charSequence) -> {
            Uri urlToOpen = getUriToOpen(item, charSequence);
            if (urlToOpen != null) {
                ClipboardManager clipboard = (ClipboardManager)
                        context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newUri(context.getContentResolver(), "URI", urlToOpen);
                clipboard.setPrimaryClip(clip);
                toastHandler.showToast("Link Copied", Toast.LENGTH_SHORT);
            }
        };
    }

    public static MaterialDialog.ListCallback getCopyCallback(Context context, PostItem item) {
        return (materialDialog, view, i, charSequence) -> {
            Uri urlToOpen = getUriToOpen(item, charSequence);
            if (urlToOpen != null) {
                ClipboardManager clipboard = (ClipboardManager)
                        context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newUri(context.getContentResolver(), "URI", urlToOpen);
                clipboard.setPrimaryClip(clip);
                ToastHandler.showToast(context, "Link Copied", Toast.LENGTH_SHORT);
            }
        };
    }

    public static MaterialDialog.ListCallback getShareCallback(Context context, PostItem item) {
        return new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                String urlToShare = "";
                if (charSequence.toString().equalsIgnoreCase("Link")) {
                    urlToShare = item.getUrl();
                }

                if (charSequence.toString().equalsIgnoreCase("Comments")) {
                    urlToShare = "https://reddit.com/" + item.getPermalink();
                }

                LinksHelper.callShareDialog(context, urlToShare);
            }
        };
    }

    @Deprecated
    public static void callAgeConfirmDialog(DialogUtil dialogUtil, MaterialDialog.SingleButtonCallback callback) {
        if(dialogUtil != null){
            dialogUtil.build()
                    .title("Are you over 18?")
                    .positiveText("Yes")
                    .negativeText("Cancel")
                    .onPositive(callback)
                    .show();
        }
    }

    public static void callAgeConfirmDialog(Context context, MaterialDialog.SingleButtonCallback callback) {
        DialogUtil.builder(context)
            .title("Are you over 18?")
            .positiveText("Yes")
            .negativeText("Cancel")
            .onPositive(callback)
            .show();
    }

}

