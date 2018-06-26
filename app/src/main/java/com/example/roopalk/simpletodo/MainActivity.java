package com.example.roopalk.simpletodo;

import android.os.Bundle;
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

    //implementing list of strings
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get a reference to the ListView
        lvItems = (ListView) findViewById(R.id.listView);

        //initializing items list
        readItems();
        //initializing the adapter
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        //wire adapter to view
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    public void onAddItem(View v)
    {
        //get a reference to the addItem thing
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);

        //get its content as a string;
        String itemAdded = etNewItem.getText().toString();

        //add to list
        itemsAdapter.add(itemAdded);

        //clear the editText
        etNewItem.setText("");

        writeItems();

        //send a toast to the user
        Toast.makeText(getApplicationContext(), "Item added!", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener()
    {

        /*TO DO -- ROOPAL
        1. ASK THE USER IF THEY WOULD LIKE TO DELETE
         */

        //long click listener -- notices when long pressed
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //remove item in list by position
                items.remove(position);

                //notify adapter
                itemsAdapter.notifyDataSetChanged();

                writeItems();

                //add this information to the log
                Log.i("MainActivity", "Removed item at " +  position);

                return true;
            }
        });
    }

    private File getDataFile()
    {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems()
    {
        try
        {
            //create the array using the content in this file
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch(IOException e)
        {
            e.printStackTrace();
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        }
        catch(IOException e)
        {
            Log.e("MainActivity", "Error writing file", e);
        }
    }

}
