package com.example.syfra.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.syfra.simpletodo.MainActivity.ITEM_POSITION;
import static com.example.syfra.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {
    //Track item text
    EditText etItemText;
    //Position of edited item in list
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        //Resolve edit text from layout
        etItemText = (EditText) findViewById(R.id.etItemText);
        //Set edit text value from intent extra
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        //Update position from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION,0);
        //Update the title bar of the activity
        getSupportActionBar().setTitle("Edit Item");
    }

    //Handler for Save button
    public void onSaveItem(View v){
        //Prepare new intent for result
        Intent i = new Intent();
        //Pass updated item text as extra
        i.putExtra(ITEM_TEXT,etItemText.getText().toString());
        //Pass original position as extra
        i.putExtra(ITEM_POSITION,position);
        //Set the intent as the result of the activity
        setResult(RESULT_OK,i);
        //Close the activity and redirect to main
        finish();
    }
}
