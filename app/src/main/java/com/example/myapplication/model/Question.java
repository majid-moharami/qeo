package com.example.myapplication.model;

public class Question {
    private String mTextQuestion;
    private boolean mAnswer,mIsCheat=false;
    private boolean mAccessCheat;
    private Colors mColor;

    public String getTextQuestion() {
        return mTextQuestion;
    }

    public void setTextQuestion(String textQuestion) {
        mTextQuestion = textQuestion;
    }

    public boolean isAnswer() {
        return mAnswer;
    }

    public void setAnswer(boolean answer) {
        mAnswer = answer;
    }

    public boolean isAccessCheat() {
        return mAccessCheat;
    }

    public void setAccessCheat(boolean accessCheat) {
        mAccessCheat = accessCheat;
    }

    public Colors getColor() {
        return mColor;
    }

    public void setColor(Colors color) {
        mColor = color;
    }

    public boolean isCheat() {
        return mIsCheat;
    }

    public void setCheat(boolean cheat) {
        mIsCheat = cheat;
    }

    public Question(String mTextQuestion, boolean mAnswer, boolean isAccessCheat, Colors color) {
        this.mTextQuestion = mTextQuestion;
        this.mAnswer = mAnswer;
        this.mAccessCheat=isAccessCheat;
        this.mColor=color;
    }
}
