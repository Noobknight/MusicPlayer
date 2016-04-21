package com.tadev.musicplayer.utils.support;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tadev.musicplayer.R;

/**
 * Created by Iris Louis on 21/04/2016.
 */
public class ToastUtils {
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static void buildToast(int icon, String message) {
        LayoutInflater inflater = (LayoutInflater)
                sContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toast,
                null);
        ImageView img = (ImageView) view.findViewById(R.id.icon);
        TextView txtMessage = (TextView) view.findViewById(R.id.tvtoast);
        img.setImageResource(icon);
        txtMessage.setText(message);
        Toast toast = new Toast(sContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
}
