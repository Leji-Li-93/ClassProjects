package com.example.lljsm.codeviewer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{

    public final static int PERMISSION_REQUEST_READ_STORAGE = 1;
    public final static int PERMISSION_REQUEST_WRITE_STORAGE = 2;
    public final static int OPEN_FILE = 11;
    public final static int REQUEST_CODE_COLORSELECTACTIVITY = 1;
    private RecyclerView code_views;
    private Toolbar toolbar;
    protected ArrayList<String> strings = new ArrayList<String>();
    protected CodeRecyclerViewAdapter codeRecyclerViewAdapter;
    HashMap<String, String> colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the main layout
        setContentView(R.layout.activity_main);

        code_views = (RecyclerView) findViewById(R.id.recyclerview_codes);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Request read file permission
        check_read_permission();
        // Request write file permission
        check_write_permission();
        // Open the file selector
        try {
            colors = CodeFormatTool.read_from_color_file(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        prepare_file();
    }

    // show the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_layout, menu);
        return true;
    }

    // bar item listener
    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            // When setting item was clicked
            case R.id.menu_item_setting:
                Intent intent = new Intent(MainActivity.this, ColorSelectActivity.class);
                int length = CodeFormatTool.default_color_key_name.length;
                String s= null;
                for (int i = 0; i < length; i++) {
                    s = CodeFormatTool.default_color_key_name[i];
                    intent.putExtra(s, colors.get(s));
                }
                startActivityForResult(intent, REQUEST_CODE_COLORSELECTACTIVITY);
                break;
            case R.id.menu_item_new:
                prepare_file();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void check_read_permission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // Permission is not granted
            // Request the permission - read storage
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READ_STORAGE);
        }
    }

    private void check_write_permission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_WRITE_STORAGE);
        }
    }

    // Handle the permission request response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_READ_STORAGE:
                // If request is cancelled, the grantResult will be empty
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.i("222", "Read External Storage Permission granted!");
                } else {
                    Toast.makeText(this, "Please enable read storage permission", Toast.LENGTH_SHORT).show();
                    check_read_permission();
                }
                break;
            case PERMISSION_REQUEST_WRITE_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.i("222", "Read External Storage Permission granted!");
                } else {
                    Toast.makeText(this, "Please enable read storage permission", Toast.LENGTH_SHORT).show();
                    check_read_permission();
                }
                break;
        }
    }

    // open the file manager
    private void prepare_file(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        startActivityForResult(intent, OPEN_FILE);
    }

    // get file data from manager
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode){
            case OPEN_FILE:
                Uri uri = null;
                if(data != null){
                    uri = data.getData();
                    try {
                        open_file(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.i("222", e.getMessage());
                        Toast.makeText(this, "Error accours when opening the file", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case REQUEST_CODE_COLORSELECTACTIVITY:
                Log.i("333", "Color select finish");
                try {
                    colors = CodeFormatTool.read_from_color_file(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<String> new_codes = CodeFormatTool.attach_color_to_code(strings, colors);
                //code_views.getRecycledViewPool().clear();
                codeRecyclerViewAdapter.update_date_set(new_codes, colors.get(CodeFormatTool.KEY_BACKGROUND_ODD), colors.get(CodeFormatTool.KEY_BACKGROUND_EVEN));
                /*code_views.setLayoutManager(null);
                code_views.setAdapter(null);
                codeRecyclerViewAdapter = new CodeRecyclerViewAdapter(new_codes, colors.get(CodeFormatTool.KEY_BACKGROUND_ODD), colors.get(CodeFormatTool.KEY_BACKGROUND_EVEN));
                code_views.setLayoutManager(new LinearLayoutManager(this));
                code_views.setAdapter(codeRecyclerViewAdapter);
                codeRecyclerViewAdapter.notifyDataSetChanged();*/
                break;
        }
    }

    // open target file from URI
    @SuppressLint("ResourceType")
    private void open_file(Uri uri) throws IOException{
        Log.i("222", uri.toString());
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        if(strings.size() > 0){
            strings.clear();
        }
        while((line = bufferedReader.readLine()) != null){
            strings.add(line);
        }
        bufferedReader.close();
        inputStream.close();
        ArrayList<String> colored_codes = new ArrayList<String>(CodeFormatTool.attach_color_to_code(strings, colors));
        code_views.setLayoutManager(new LinearLayoutManager(this));
        codeRecyclerViewAdapter = new CodeRecyclerViewAdapter(colored_codes, colors.get(CodeFormatTool.KEY_BACKGROUND_ODD), colors.get(CodeFormatTool.KEY_BACKGROUND_EVEN));
        code_views.setAdapter(codeRecyclerViewAdapter);
    }

}
