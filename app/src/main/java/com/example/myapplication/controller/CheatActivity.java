package com.example.myapplication.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_KEY_SCORE_NUMBER ="isAnswerTrue" ;
    public static final String EXTRA_KEY_IS_CHEAT = "com.example.myapplication.controller.isCheat";
    private Button mButtonShowCheat;
    private TextView mTextViewCheat;
    private boolean mTrueAnswer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        findAllView();
        setAllOnclickListener();
        extractAnswer();
    }

    private void extractAnswer(){
        Intent intent = getIntent();
        mTrueAnswer = intent.getBooleanExtra(EXTRA_KEY_SCORE_NUMBER,false);
    }

    private void findAllView() {
        mButtonShowCheat = findViewById(R.id.button_show_cheat);
        mTextViewCheat=findViewById(R.id.textViewAnswer);
    }

    private void setAllOnclickListener(){
        mButtonShowCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTrueAnswer){
                    mTextViewCheat.setText(R.string.button_true);
                }else mTextViewCheat.setText(R.string.button_false);

                setResult();
            }
        });
    }

    private void setResult(){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_KEY_IS_CHEAT, true);
        setResult(RESULT_OK,intent);
    }
}