package com.example.syfra.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //A numeric code to identify the edit activity
    public final static int EDIT_REQUEST_CODE=20;
    //Keys used for passing data between activities
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";


    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        //Mock Data
        //items.add("First item");
        //items.add("Second item");

        setupListViewListener();
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if(!itemText.equals("")) {
            itemsAdapter.add(itemText);
            Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(),"Invalid input",Toast.LENGTH_SHORT).show();
        etNewItem.setText("");
        writeItems();
    }

    private void setupListViewListener(){
        Log.i("Main Activity","Setting up listener on list view");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Main Activity","Item removed from list: "+position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Item removed",Toast.LENGTH_SHORT).show();
                writeItems();
                return true;
            }
        });

        //Set up item listener for edit (regular click)
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Create the new activity
                Intent i = new Intent(MainActivity.this,EditItemActivity.class);
                //Pass the data being edited
                i.putExtra(ITEM_TEXT,items.get(position));
                i.putExtra(ITEM_POSITION,position);
                //Display the new activity
                startActivityForResult(i,EDIT_REQUEST_CODE);
            }
        });
    }

    //Handle results from edit activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the activity completed ok
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            //Extract updated item text from result intent extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            //Extract original position of edited item
            int position = data.getExtras().getInt(ITEM_POSITION);

            //Update the model with the new item text at the edited position
            if(!updatedItem.equals(items.get(position)) && !updatedItem.equals("")) {
                items.set(position, updatedItem);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                Toast.makeText(this,"Item updated successfully",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this,"Item remained unchanged",Toast.LENGTH_SHORT).show();
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(),"todo.txt");
    }

    private void readItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("Main Activity","Error reading file",e);
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("Main Activity","Error writing file",e);
        }
    }
}
