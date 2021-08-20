package com.sharifdev.torobche.model;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sharifdev.torobche.Adapters.ProfileChooseArrayAdapter;
import com.sharifdev.torobche.R;
import com.sharifdev.torobche.backUtils.UserUtils;

public class ProfileAvatar {
    public static int getAvatarResourceId(int avatarID) {
        switch (avatarID) {
            case 2:
                return R.mipmap.avatar_2;
            case 3:
                return R.mipmap.avatar_3;
            case 4:
                return R.mipmap.avatar_4;
            case 5:
                return R.mipmap.avatar_5;
            case 6:
                return R.mipmap.avatar_6;
            case 7:
                return R.mipmap.avatar_7;
            case 8:
                return R.mipmap.avatar_8;
            default:
                return R.mipmap.avatar_1;
        }
    }

    public static class ChangeAvatarBtnListener implements View.OnClickListener {
        Context context;
        Activity activity;
        Fragment fragment;
        View view;

        public ChangeAvatarBtnListener(Context context, Activity activity, Fragment fragment, View view) {
            this.context = context;
            this.activity = activity;
            this.fragment = fragment;
            this.view = view;
        }

        @Override
        public void onClick(View v) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
            builder.setTitle("Change Avatar");
            final String[] items = new String[]{"", "", "", "", "", "", "", ""};
            final Integer[] icons = new Integer[]{
                    R.mipmap.avatar_1,
                    R.mipmap.avatar_2,
                    R.mipmap.avatar_3,
                    R.mipmap.avatar_4,
                    R.mipmap.avatar_5,
                    R.mipmap.avatar_6,
                    R.mipmap.avatar_7,
                    R.mipmap.avatar_8,
            };
            ListAdapter adapter = new ProfileChooseArrayAdapter(activity, items, icons);
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UserUtils.changeProfile(which, fragment, view);
                }
            }).show();
        }
    }
}
