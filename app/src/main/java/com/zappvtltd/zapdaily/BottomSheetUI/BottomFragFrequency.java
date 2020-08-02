package com.zappvtltd.zapdaily.BottomSheetUI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.zappvtltd.zapdaily.R;


public class BottomFragFrequency extends Fragment {

    Spinner spinner;

    private String product_id;
    private String frequency;
    LinearLayout customlayout;

    public BottomFragFrequency() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bottom_frag_frequency, container, false);
        Bundle bundle =this.getArguments();
        product_id =bundle.getString("pid");

        customlayout=view.findViewById(R.id.customlayout);
        spinner=view.findViewById(R.id.frequencyspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.frequency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                frequency=spinner.getSelectedItem().toString();
                if (frequency.equals("Custom")){
                    customlayout.setVisibility(View.VISIBLE);
                }
                else {
                    customlayout.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        return view;
    }
}