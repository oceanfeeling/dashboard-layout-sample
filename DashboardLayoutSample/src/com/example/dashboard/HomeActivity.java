package com.example.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by conne3ion on 9/20/13.
 */
public class HomeActivity extends ActionBarActivity implements View.OnClickListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ((Button) findViewById(R.id.btn_static)).setOnClickListener(this);
        ((Button) findViewById(R.id.btn_runtime)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_runtime) {
            startActivity(new Intent(this, RuntimeDashboardActivity.class));
        } else if(v.getId() == R.id.btn_static) {
            startActivity(new Intent(this, StaticDashboardActivity.class));
        }
    }
}