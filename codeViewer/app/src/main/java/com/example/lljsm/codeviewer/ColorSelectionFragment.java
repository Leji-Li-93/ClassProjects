package com.example.lljsm.codeviewer;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ColorSelectionFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener, Button.OnClickListener {

    private SeekBar seekBar_R;
    private SeekBar seekBar_G;
    private SeekBar seekBar_B;
    private TextView textview_preview;
    private TextView textview_value_R;
    private TextView textview_value_G;
    private TextView textview_value_B;
    private Button button_confirm;
    private int[] rgb;
    private int fragment_index = 0;
    private ChangeTextColorInterface color_change_listener;

    public static ColorSelectionFragment newInstance(String color_RGB, int index) {
        
        Bundle args = new Bundle();
        
        ColorSelectionFragment fragment = new ColorSelectionFragment();
        args.putString("Color", color_RGB);
        args.putInt("Index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_color_selector, container);
        seekBar_R = (SeekBar) view.findViewById(R.id.seekbar_color_R);
        seekBar_G = (SeekBar) view.findViewById(R.id.seekbar_color_G);
        seekBar_B = (SeekBar) view.findViewById(R.id.seekbar_color_B);
        textview_preview = (TextView) view.findViewById(R.id.textview_preview_text);
        textview_value_R = (TextView) view.findViewById(R.id.textview_color_R_value);
        textview_value_G = (TextView) view.findViewById(R.id.textview_color_G_value);
        textview_value_B = (TextView) view.findViewById(R.id.textview_color_B_value);
        button_confirm = (Button) view.findViewById(R.id.button_color_selector_confirm);

        Bundle bundle = getArguments();
        String RGB = null;
        if(null != bundle){
            RGB = bundle.getString("Color");
            fragment_index = bundle.getInt("Index");
        } else {
            RGB = "#000000";
        }

        rgb = CodeFormatTool.hex2RGB(RGB);

        for (int i = 0; i < 3; i++) {
            Log.i("222", "Color -> "+ String.valueOf(rgb[i]));
        }

        // set the initial status of each seekbar
        setSeekBarRange(seekBar_R, rgb[0]);
        setSeekBarRange(seekBar_G, rgb[1]);
        setSeekBarRange(seekBar_B, rgb[2]);

        // set the value of each hint textview
        textview_value_R.setText(String.valueOf(rgb[0]));
        textview_value_G.setText(String.valueOf(rgb[1]));
        textview_value_B.setText(String.valueOf(rgb[2]));

        // set the color of preview textview
        textview_preview.setTextColor(Color.parseColor(RGB));

        // set listener
        seekBar_R.setOnSeekBarChangeListener(this);
        seekBar_G.setOnSeekBarChangeListener(this);
        seekBar_B.setOnSeekBarChangeListener(this);
        button_confirm.setOnClickListener(this);

        return view;
    }

    public void setChangeColorListener(ChangeTextColorInterface listener){
        this.color_change_listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(null != dialog){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            // Set dialog dimension, 75% of the screen width and 50% of the screen height
            dialog.getWindow().setLayout((int)(displayMetrics.widthPixels * 0.75), (int)(displayMetrics.heightPixels * 0.5));
        }
    }



    // Set the initial status of each seekBar
    private void setSeekBarRange(SeekBar seekBar, int current_value){
        seekBar.setMax(255);
        seekBar.setProgress(current_value);
    }


    // seekbar listener
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int id = seekBar.getId();
        switch (id){
            case R.id.seekbar_color_R:
                rgb[0] = progress;
                textview_value_R.setText(String.valueOf(progress));
                break;
            case R.id.seekbar_color_G:
                rgb[1] = progress;
                textview_value_G.setText(String.valueOf(progress));
                break;
            case R.id.seekbar_color_B:
                rgb[2] = progress;
                textview_value_B.setText(String.valueOf(progress));
                break;
        }
        textview_preview.setTextColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        int id = seekBar.getId();
        switch (id){
            case R.id.seekbar_color_R:
                break;
            case R.id.seekbar_color_G:
                break;
            case R.id.seekbar_color_B:
                break;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int id = seekBar.getId();
        switch (id){
            case R.id.seekbar_color_R:
                break;
            case R.id.seekbar_color_G:
                break;
            case R.id.seekbar_color_B:
                break;
        }
    }

    // confirm button listener
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.button_color_selector_confirm){
            String changed_color = CodeFormatTool.RGB2hex(rgb);
            Log.i("222", "Button click -> " + changed_color);
            if(null != color_change_listener){
                color_change_listener.change_color(changed_color, fragment_index);
            }
            this.dismiss();
        }
    }
}
