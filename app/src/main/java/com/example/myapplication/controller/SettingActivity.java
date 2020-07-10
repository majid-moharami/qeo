package com.example.myapplication.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.myapplication.R;

public class SettingActivity extends AppCompatActivity {

    private RadioButton mRadioButton14,mRadioButton18,mRadioButton24;
    private Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findAllView();
        extractFontSize();



    }

    private void findAllView(){
        mRadioButton14=findViewById(R.id.radio_14sp);
        mRadioButton18=findViewById(R.id.radio_18sp);
        mRadioButton24=findViewById(R.id.radio_24sp);
    }

    private void extractFontSize(){
        int size;
        mIntent = getIntent();
        size = mIntent.getIntExtra(MainActivity.EXTRA_KEY_TEXT_QUESTION_SIZE,18);
        Toast.makeText(this, (int) size, Toast.LENGTH_SHORT).show();
        if (size==14)
            mRadioButton14.setChecked(true);
        if (size == 18)
            mRadioButton18.setChecked(true);
        if (size == 24)
            mRadioButton24.setChecked(true);

    }


}