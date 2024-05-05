package com.barbacil.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class UserPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        HideUI.setImmersiveMode(this);
    }
}