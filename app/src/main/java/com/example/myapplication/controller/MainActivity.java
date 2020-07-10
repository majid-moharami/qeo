package com.example.myapplication.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.Colors;
import com.example.myapplication.model.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String BUNDLE_KEY_CURRENT_INDEX = "mCurrentQuestions";
    public static final String BUNDLE_KEY_COUNTER_TIME = "mCounterTime";
    public static final String BUNDLE_KEY_REST_TIME = "mRestTime";
    public static final String BUNDLE_KEY_CHECK_ANSWER_ARRAY = "mCheckAnswer";
    public static final String BUNDLE_KEY_SCORE_NUMBER = "mScoreNumber";
    public static final String EXTRA_KEY_SCORE_NUMBER = "isAnswerTrue";
    public static final String EXTRA_KEY_TEXT_QUESTION_SIZE = "questionTextSize";
    public static final String EXTRA_KEY_BACKGROUND_COLOR = "mainActivityBackgroundColor";

    private static final String EXTRA_KEY_QUESTION_STRING = "questionString";
    public static final int REQUEST_CODE_START_CHEAT_ACTIVITY = 0;
    private ImageButton mButtonNext, mButtonPrevious, mButtonFistQ, mButtonLastQ;
    private Button mButtonTrue, mButtonFalse, mButtonRestart, mButtonCheat, mButtonSetting;
    private TextView mTextQuestion, mScoreNumber, mTextViewTime;
    private LinearLayout mMainActivityLinearLayout;

    private int mCountScore = 0, mTimer, mCounter = mTimer, mRestartTime = mTimer;
    private int mCurrentQuestion = 0;

    private Question[] mQuestionsBank;
    private int[] mCheckAnswer;
    private boolean[] mCheatAccessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        findAllView();

        Intent intent = getIntent();
        String s = intent.getStringExtra(MainActivity.EXTRA_KEY_QUESTION_STRING);
        assert s != null;
        mQuestionsBank = parse(s);
        mCounter = mTimer;
        mRestartTime = mTimer;
        mCheckAnswer = new int[mQuestionsBank.length];
        mCheatAccessButton = new boolean[mQuestionsBank.length];

        setAllOnclickListener();
        updateQuestion();

        if (savedInstanceState != null) {
            mCounter = savedInstanceState.getInt(BUNDLE_KEY_COUNTER_TIME);
            mCurrentQuestion = savedInstanceState.getInt(BUNDLE_KEY_CURRENT_INDEX);
            mCheckAnswer = savedInstanceState.getIntArray(BUNDLE_KEY_CHECK_ANSWER_ARRAY);
            checkAlreadyAnswered();
            mCountScore = savedInstanceState.getInt(BUNDLE_KEY_SCORE_NUMBER);
            mScoreNumber.setText(String.valueOf(mCountScore));
            mTimer = savedInstanceState.getInt(BUNDLE_KEY_REST_TIME);
        }
        new CountDownTimer((mTimer + 1) * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTextViewTime.setText(String.valueOf(mCounter));
                mCounter--;
            }

            @Override
            public void onFinish() {
                mTextViewTime.setText("0");
                checkFinish();
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null)
            return;
        if (requestCode == REQUEST_CODE_START_CHEAT_ACTIVITY)
            mQuestionsBank[mCurrentQuestion].setCheat(data.getBooleanExtra(CheatActivity.EXTRA_KEY_IS_CHEAT, false));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_KEY_CURRENT_INDEX, mCurrentQuestion);
        outState.putInt(BUNDLE_KEY_COUNTER_TIME, mCounter);
        outState.putIntArray(BUNDLE_KEY_CHECK_ANSWER_ARRAY, mCheckAnswer);
        outState.putInt(BUNDLE_KEY_SCORE_NUMBER, mCountScore);
        outState.putInt(BUNDLE_KEY_REST_TIME, mCounter);
    }

    private void findAllView() {
        mButtonTrue = findViewById(R.id.button_true);
        mButtonFalse = findViewById(R.id.button_false);
        mButtonNext = findViewById(R.id.button_next);
        mButtonPrevious = findViewById(R.id.button_previous);
        mButtonFistQ = findViewById(R.id.button_first_question);
        mButtonLastQ = findViewById(R.id.button_last_question);
        mButtonRestart = findViewById(R.id.button_restart);
        mButtonCheat = findViewById(R.id.button_cheat);
        mButtonSetting = findViewById(R.id.button_setting);
        mTextViewTime = findViewById(R.id.text_view_time);
        mScoreNumber = findViewById(R.id.score_number);
        mTextQuestion = findViewById(R.id.text_view_question);
        mMainActivityLinearLayout = findViewById(R.id.linear_layout_main_activity);
    }

    private void setAllOnclickListener() {
        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentQuestion = (mCurrentQuestion + 1) % mQuestionsBank.length;
                updateQuestion();
            }
        });

        mButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentQuestion = (--mCurrentQuestion + mQuestionsBank.length) % mQuestionsBank.length;
                updateQuestion();
            }
        });

        mButtonLastQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentQuestion = mQuestionsBank.length - 1;
                updateQuestion();
            }
        });

        mButtonFistQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentQuestion = 0;
                updateQuestion();
            }
        });
        mButtonCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAnswerTrue = mQuestionsBank[mCurrentQuestion].isAnswer();
                Intent intent = new Intent(MainActivity.this, CheatActivity.class);
                intent.putExtra(EXTRA_KEY_SCORE_NUMBER, isAnswerTrue);
                startActivityForResult(intent, REQUEST_CODE_START_CHEAT_ACTIVITY);
            }
        });

        mButtonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra(EXTRA_KEY_TEXT_QUESTION_SIZE,(int) mTextQuestion.getTextSize());
                intent.putExtra(EXTRA_KEY_BACKGROUND_COLOR, ((ColorDrawable) mMainActivityLinearLayout.getBackground()).getColor());
                startActivityForResult(intent, REQUEST_CODE_START_CHEAT_ACTIVITY);
            }
        });

        mButtonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentQuestion = 0;
                mCountScore = 0;
                mScoreNumber.setText(String.valueOf(mCountScore));
                Arrays.fill(mCheckAnswer, 0);
                mTextQuestion.setVisibility(View.VISIBLE);
                mButtonTrue.setVisibility(View.VISIBLE);
                mButtonFalse.setVisibility(View.VISIBLE);
                mButtonFistQ.setVisibility(View.VISIBLE);
                mButtonLastQ.setVisibility(View.VISIBLE);
                mButtonNext.setVisibility(View.VISIBLE);
                mButtonPrevious.setVisibility(View.VISIBLE);
                mButtonCheat.setVisibility(View.VISIBLE);
                mButtonRestart.setVisibility(View.GONE);
                mButtonTrue.setEnabled(true);
                mButtonFalse.setEnabled(true);
                mTimer = mRestartTime;
                mCounter = mRestartTime;
                new CountDownTimer((mTimer + 1) * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mTextViewTime.setText(String.valueOf(mCounter));
                        mCounter--;
                    }

                    @Override
                    public void onFinish() {
                        mTextViewTime.setText("0");
                        checkFinish();
                    }
                }.start();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void updateQuestion() {
        checkAlreadyAnswered();
        Question question = mQuestionsBank[mCurrentQuestion];
        cheatAccessButton();
        Colors c = question.getColor();
        if (c == Colors.BLACK) {
            mTextQuestion.setTextColor(Color.BLACK);
        } else if (c == Colors.RED) {
            mTextQuestion.setTextColor(Color.RED);
        } else if (c == Colors.BLUE) {
            mTextQuestion.setTextColor(Color.BLUE);
        } else if (c == Colors.GREEN) {
            mTextQuestion.setTextColor(Color.GREEN);
        }
        mTextQuestion.setText(question.getTextQuestion());
        checkFinish();
    }

    private void checkAnswer(boolean userPressed) {
        if (!mQuestionsBank[mCurrentQuestion].isCheat()) {
            boolean answer = mQuestionsBank[mCurrentQuestion].isAnswer();
            mCheckAnswer[mCurrentQuestion]++;
            if (userPressed == answer) {
                mCountScore += 10;
                mScoreNumber.setText(String.valueOf(mCountScore));
                Toast.makeText(this, R.string.toast_correct, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, R.string.toast_incorrect, Toast.LENGTH_SHORT).show();

            mButtonTrue.setEnabled(false);
            mButtonFalse.setEnabled(false);
        }
    }

    private void checkAlreadyAnswered() {
        if (mCheckAnswer[mCurrentQuestion] > 0) {
            mButtonTrue.setEnabled(false);
            mButtonFalse.setEnabled(false);
        } else {
            mButtonTrue.setEnabled(true);
            mButtonFalse.setEnabled(true);
        }
    }

    private void checkFinish() {
        boolean f = true;
        for (int i = 0; i < mCheckAnswer.length; i++) {
            if (mCheckAnswer[i] == 0)
                f = false;
        }
        if (f || mCounter == 0) {
            mTextQuestion.setVisibility(View.GONE);
            mButtonTrue.setVisibility(View.GONE);
            mButtonFalse.setVisibility(View.GONE);
            mButtonFistQ.setVisibility(View.GONE);
            mButtonLastQ.setVisibility(View.GONE);
            mButtonNext.setVisibility(View.GONE);
            mButtonPrevious.setVisibility(View.GONE);
            mButtonCheat.setVisibility(View.GONE);
            mButtonRestart.setVisibility(View.VISIBLE);
            mTextViewTime.setText("0");
        }
    }

    //parse the input string to question array from questionBuilderActivity
    /*
    {
[{“Tehran is in iran”}, {true}, {false}, {green}],
[{“Iran language is english”}, {false}, {true}, {red}],
[{“England is in usa”}, {false}, {false}, {black}]
} ,
{30}

     */
    private Question[] parse(String str) {
        String strTimer = "";
        List<String> question = new ArrayList<>();
        int count, count1;

        //extracting time
        count = str.lastIndexOf(',');
        for (int i = count; i < str.length(); i++) {
            strTimer += str.charAt(i);
        }

        count = strTimer.indexOf('{');
        count1 = strTimer.lastIndexOf('}');
        strTimer = strTimer.substring(count + 1, count1);
        mTimer = Integer.parseInt(strTimer);


        //extracting questions from big string
        while (true) {
            count = str.lastIndexOf('[');
            count1 = str.lastIndexOf(']');
            if (count == -1)
                break;
            String tempQuestion = str;
            str = str.substring(0, count);
            tempQuestion = tempQuestion.substring(count + 1, count1);
            question.add(tempQuestion);
        }

        //MAKING QUESTION CLASS ARRAY
        Question[] questions = new Question[question.size()];
        for (int i = question.size() - 1; i >= 0; i--) {
            String[] strings = question.get(i).split(",");
            boolean answer = false, isCheat = false;
            String questionText = "";
            Colors color = Colors.RED;
            for (int j = strings.length - 1; j >= 0; j--) {
                int c1 = strings[j].lastIndexOf('{'), c2 = strings[j].lastIndexOf('}');

                if (j == 3) {
                    if (strings[j].substring(c1 + 1, c2).equalsIgnoreCase("blue"))
                        color = Colors.BLUE;
                    else if (strings[j].substring(c1 + 1, c2).equalsIgnoreCase("black"))
                        color = Colors.BLACK;
                    else if (strings[j].substring(c1 + 1, c2).equalsIgnoreCase("red"))
                        color = Colors.RED;
                    else if (strings[j].substring(c1 + 1, c2).equalsIgnoreCase("green"))
                        color = Colors.GREEN;
                }
                if (j == 2) {
                    isCheat = Boolean.parseBoolean(strings[j].substring(c1 + 1, c2));
                }
                if (j == 1) {
                    answer = Boolean.parseBoolean(strings[j].substring(c1 + 1, c2));
                }
                if (j == 0) {
                    questionText = strings[j].substring(c1 + 2, c2 - 1);
                }
            }
            questions[question.size() - (i + 1)] = new Question(questionText, answer, isCheat, color);
        }
        return questions;
    }

    private void cheatAccessButton() {
        for (int i = 0; i < mQuestionsBank.length; i++) {
            mCheatAccessButton[i] = mQuestionsBank[i].isAccessCheat();
        }
        if (!mCheatAccessButton[mCurrentQuestion]) {
            mButtonCheat.setEnabled(false);
        } else mButtonCheat.setEnabled(true);
    }

}