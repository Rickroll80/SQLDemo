package com.example.sqldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    // references to buttons and other controls on the layout
    Button btn_add, btn_viewAll;
    EditText et_name, et_age;
    Switch sw_activeCustomer;
    ListView lv_customerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign control variables with values from the resource folder
        btn_add = findViewById(R.id.btn_add);
        btn_viewAll = findViewById(R.id.btn_viewAll);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        sw_activeCustomer = findViewById(R.id.sw_active);
        lv_customerList = findViewById(R.id.lv_customerList);

        // listener for the ADD button
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // deals with invalid user input before clicking "add" button
                CustomerModel customerModel;
                try {
                    customerModel = new CustomerModel(-1, et_name.getText().toString(), Integer.parseInt(et_age.getText().toString()), sw_activeCustomer.isChecked());
                    Toast.makeText(MainActivity.this, customerModel.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error creating customer", Toast.LENGTH_SHORT).show();
                    // default customer created upon failure
                    customerModel = new CustomerModel(-1, "Error", 0, false);
                }

                // add a new customer to the database whenever the btn_add click listener is invoked

                // databaseHelper is a reference to the customer database
                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

                // add the customer to the database
                boolean success = databaseHelper.addOne(customerModel);
                Toast.makeText(MainActivity.this, "Success= " + success, Toast.LENGTH_SHORT).show();

            }
        });

        // Upon click, the View All button displays the table contents
        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Summary: lv_customerList is a ListView object, which is a kind of AdapterView.
                   ListViews, being AdapterViews, need an adapter to determine their children.
                   An ArrayAdapter is created and the list of CustomerModel objects from the database table is passed in.
                   Then, the ArrayAdapter object is set as the adapter for the ListView lv_customerList.
                 */

                // reference to the database
                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                List<CustomerModel> dbContents = databaseHelper.getEveryone();

                // Toast.makeText(MainActivity.this, dbContents.toString(), Toast.LENGTH_SHORT).show();
                // ArrayAdapter of CustomerModels - dbContents is passed to this adapter
                ArrayAdapter customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, dbContents); // simplest possible array adapter

                // associate the ArrayAdapter with the View All button
                // set the ListView customerList's adapter to be the ArrayAdapter
                lv_customerList.setAdapter(customerArrayAdapter);

            }
        });









    }
}