package com.example.fb0122.namedbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by fb0122 on 2016/6/15.
 */
public class Login extends AppCompatActivity {

    ImageView login_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwc_login);
        login_btn = (ImageView)findViewById(R.id.login_text);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,Login_Dialog.class);
                startActivity(i);
                finish();
            }
        });
    }
}
