package com.example.testmodule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mengtao1 on 2017/11/11.
 */

public abstract class BaseAcitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, BaseAcitivity.class);
    }
}
