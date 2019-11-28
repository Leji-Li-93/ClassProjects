package com.example.lljsm.codeviewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ColorSelectActivity extends AppCompatActivity implements View.OnClickListener, ChangeTextColorInterface {

    private HashMap<String, String> colors;
    private ArrayList<RelativeLayout> layouts;
    private ArrayList<ImageView> imageViews;
    private ArrayList<TextView> textViews;
    private ImageView imageview_back;
    private int[] layout_ids = {
            R.id.layout_keyword,
            R.id.layout_comment,
            R.id.layout_string,
            R.id.layout_regular,
            R.id.layout_number,
            R.id.layout_char,
            R.id.layout_operator,
            R.id.layout_background_odd,
            R.id.layout_background_even
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_setting);

        Intent intent = getIntent();
        colors = new HashMap<String, String>();
        int length = CodeFormatTool.default_color_key_name.length;
        String s = null;
        layouts = new ArrayList<RelativeLayout>(length);
        imageViews = new ArrayList<ImageView>(length);
        textViews = new ArrayList<TextView>(length);

        for (int i = 0; i < length; i++) {
            s = CodeFormatTool.default_color_key_name[i];
            colors.put(s, intent.getStringExtra(s));
            layouts.add((RelativeLayout) findViewById(layout_ids[i]));
            imageViews.add((ImageView)(layouts.get(i)).findViewById(R.id.imageview_color_block));
            imageViews.get(i).setBackgroundColor(Color.parseColor(colors.get(s)));
            textViews.add((TextView)(layouts.get(i).findViewById(R.id.textview_color_setting_name)));
            textViews.get(i).setText(s);
            layouts.get(i).setOnClickListener(this);
        }

        imageview_back = (ImageView) findViewById(R.id.imageview_back);
        imageview_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int index = 0;
        switch (id){
            case R.id.layout_keyword:
                index = 0;
                break;
            case R.id.layout_comment:
                index = 1;
                break;
            case R.id.layout_string:
                index = 2;
                break;
            case R.id.layout_regular:
                index = 3;
                break;
            case R.id.layout_number:
                index = 4;
                break;
            case R.id.layout_char:
                index = 5;
                break;
            case R.id.layout_operator:
                index = 6;
                break;
            case R.id.layout_background_odd:
                index = 7;
                break;
            case R.id.layout_background_even:
                index = 8;
                break;

            case R.id.imageview_back:
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }

        if(id != R.id.imageview_back){
            FragmentManager fragmentManager = getSupportFragmentManager();
            ColorSelectionFragment fragment = ColorSelectionFragment.newInstance(colors.get(CodeFormatTool.default_color_key_name[index]), index);
            fragment.setChangeColorListener(this);
            fragment.show(fragmentManager, "ColorSelectionFragment");
        }
    }

    @Override
    public void change_color(String color, int index) {
        colors.remove(CodeFormatTool.default_color_key_name[index]);
        colors.put(CodeFormatTool.default_color_key_name[index], color);
        imageViews.get(index).setBackgroundColor(Color.parseColor(colors.get(CodeFormatTool.default_color_key_name[index])));
        try {
            CodeFormatTool.save_to_color_file(this, colors);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Cannot save color change", Toast.LENGTH_LONG).show();
        }
    }
}
