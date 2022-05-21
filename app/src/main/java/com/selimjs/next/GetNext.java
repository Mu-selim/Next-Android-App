package com.selimjs.next;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GetNext extends AppCompatActivity
{
    private Button getNext, contact;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_next);
        getSupportActionBar().hide();
        getNextBtn();
        getcontactBtn();
    }

    public void getNextBtn()
    {
        getNext = findViewById(R.id.getNext);
        getNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(GetNext.this, FunctionActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getcontactBtn()
    {
        contact = findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(GetNext.this, Contact.class);
                startActivity(intent);
            }
        });
    }
}