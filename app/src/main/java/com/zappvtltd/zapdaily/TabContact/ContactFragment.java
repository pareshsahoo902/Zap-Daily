package com.zappvtltd.zapdaily.TabContact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.thekhaeng.pushdownanim.PushDownAnim;
import com.zappvtltd.zapdaily.DrawerUI.Account;
import com.zappvtltd.zapdaily.EditProfile;
import com.zappvtltd.zapdaily.R;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;


public class ContactFragment extends Fragment {

    WebView webView;
    CardView Callus;


    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_contact, container, false);
        // Inflate the layout for this fragment
        webView=(WebView)view.findViewById(R.id.webView);
        Callus=(CardView)view.findViewById(R.id.call_card);
        webView.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLScx4BnRNqlSsByomA3mBA3CKxIhA4IqyNZ3czBm0Y80yZ16ZQ/viewform?usp=sf_link");

        PushDownAnim.setPushDownAnimTo(Callus)
                .setScale(MODE_SCALE,
                        PushDownAnim.DEFAULT_PUSH_SCALE)

                .setDurationPush(PushDownAnim.DEFAULT_PUSH_DURATION)
                .setDurationRelease(PushDownAnim.DEFAULT_RELEASE_DURATION)
                .setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
                .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        switch (v.getId()) {

                            case R.id.call_card:
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+91" + "7008842784", null));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                break;
                        }
                    }
                });





        return view;

    }
}