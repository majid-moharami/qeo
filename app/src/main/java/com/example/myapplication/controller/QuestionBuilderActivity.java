package com.example.myapplication.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;

public class QuestionBuilderActivity extends AppCompatActivity {
    private  Button mButtonStart;
    private EditText mTextViewString;

    public static final String EXTRA_KEY_QUESTION_STRING ="questionString";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_builder);

        mButtonStart=findViewById(R.id.button_start);
        mTextViewString=findViewById(R.id.editTextTextMultiLine);

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mTextViewString.getText().toString();
                Intent intent = new Intent(QuestionBuilderActivity.this , MainActivity.class);
                intent.putExtra(EXTRA_KEY_QUESTION_STRING , s);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean checkInputValidation(String s){
        return true;
    }
}