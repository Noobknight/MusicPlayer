package com.tadev.musicplayer.utils.support;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.tadev.musicplayer.R;

/**
 * Created by Iris Louis on 20/04/2016.
 */
public class DialogUtils {

    public interface DialogListener{
        void onPositiveClick();
    }

    private DialogListener mListener;

    public DialogUtils(DialogListener mListener) {
        this.mListener = mListener;
    }

    public void showDialog(Context context, String title, String message) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(StringUtils.getStringRes(R.string.negative_dialog), null);
        builder.setNegativeButton(StringUtils.getStringRes(R.string.positive_dialog),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onPositiveClick();
            }
        });
        builder.show();
    }
}
