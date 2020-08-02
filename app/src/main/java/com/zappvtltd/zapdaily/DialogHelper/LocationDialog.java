package com.zappvtltd.zapdaily.DialogHelper;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.cardview.widget.CardView;

import com.zappvtltd.zapdaily.R;

public class LocationDialog extends AppCompatDialogFragment {

    static AlertDialog alertDialog;
    EditText locationtext;
    ImageView currentloc;
    CardView select;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View dialogLayout = inflater.inflate(R.layout.dialog_location, null);
        builder.setView(dialogLayout);


        select=dialogLayout.findViewById(R.id.SelectButton);
        locationtext=dialogLayout.findViewById(R.id.Edit_Location);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = locationtext.getText().toString();
                Toast.makeText(getContext(), "location is "+location.toUpperCase(), Toast.LENGTH_SHORT).show();
            }
        });





        alertDialog =  builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        return alertDialog;
    }
}
