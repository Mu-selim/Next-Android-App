package com.selimjs.next;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

public class FunctionActivity extends AppCompatActivity
{
    CardView cardHome, cardBluetooth, cardControl, cardMessage, cardSettings, cardExit;
    private static final int TIME_INTERVAL = 2000;
    private long backPressed;
    private boolean exit = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        getSupportActionBar().hide();

        cardHome = findViewById(R.id.cardHome);
        cardBluetooth = findViewById(R.id.cardBluetooth);
        cardControl = findViewById(R.id.cardControl);
        cardMessage = findViewById(R.id.cardMessage);
        cardSettings = findViewById(R.id.cardSettings);
        cardExit = findViewById(R.id.cardExit);


        cardHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                displayMessage("Home cliked");
            }
        });

        cardBluetooth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(FunctionActivity.this, BluetoothStartActivity.class);
                startActivity(i);
            }
        });

        cardControl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                progressDialog = new ProgressDialog(FunctionActivity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.custom_dailog);
                progressDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
                progressDialog.setCancelable(false);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(FunctionActivity.this, ControlActivity.class);
                        startActivity(i);
                        progressDialog.dismiss();
                    }
                }, 3000);
            }
        });

        cardMessage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                displayMessage("Message cliked");
            }
        });

        cardSettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                displayMessage("Settings cliked");
            }
        });

        cardExit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                exit = true;
                Exit();
            }
        });

    }

    public void displayMessage(String s)
    {
        Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void Exit()
    {
        if (exit)
        {
            super.onBackPressed();
            return;
        }
    }

    @Override
    public void onBackPressed()
    {
        if (backPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else
            Toast.makeText(getBaseContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        backPressed = System.currentTimeMillis();
    }
}