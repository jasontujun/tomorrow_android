package com.tomorrow.android.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.tomorrow.android.R;

/**
 * Created by jasontujun on 14-7-19.
 */
public class DialogUtil {

    public static Dialog createWaitingDialog(Context context, int msg,
                                      DialogInterface.OnCancelListener listener) {
        if (context == null)
            return null;
        return createWaitingDialog(context, context.getString(msg), listener);
    }

    public static Dialog createWaitingDialog(Context context, String msg,
                                      DialogInterface.OnCancelListener listener) {
        if (context == null)
            return null;
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_waiting);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        ImageView waitingView = (ImageView) dialog.findViewById(R.id.waiting);
        TextView msgView = (TextView) dialog.findViewById(R.id.msg);

        msgView.setText(msg);
        Animation rotate = AnimationUtils.loadAnimation(context, R.anim.rotation);
        if (rotate != null)
            waitingView.startAnimation(rotate);

        if (listener != null)
            dialog.setOnCancelListener(listener);

        return dialog;
    }
}
