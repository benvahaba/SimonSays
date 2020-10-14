package com.example.simon_says_app;

import androidx.annotation.NonNull;

public class UserInformation
{
    public String nickname;
    public int score;

    public UserInformation(){}

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(nickname+" "+score).toString();
    }
}
