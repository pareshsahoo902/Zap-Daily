package com.zappvtltd.zapdaily.DialogHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.zappvtltd.zapdaily.R;

public class ProgressDialogLoading extends DialogFragment {

    static AlertDialog alertDialog;

//    public static AlertDialog getCodeSendingDialog(Activity activity){
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        LayoutInflater inflater = activity.getLayoutInflater();
//        View dialogLayout = inflater.inflate(R.layout.dialogloader, null);
//
//        TextView MSG =  dialogLayout.findViewById(R.id.text);
//        MSG.setText("Sending Code");
//        builder.setView(dialogLayout);
//
//        alertDialog =  builder.create();
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.setCancelable(false);
//
//        return alertDialog;
//    }

    public static AlertDialog getLoadingDialog(Activity activity, String MSGText){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialogloader, null);

        TextView  MSG =  dialogLayout.findViewById(R.id.text);
        MSG.setText(MSGText);
        builder.setView(dialogLayout);

        alertDialog =  builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        return alertDialog;
    }
}
