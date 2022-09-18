package com.example.sqldemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles CRUD operations
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";
    public static final String COLUMN_ID = "ID";

    public DatabaseHelper(@Nullable Context context) {
        // "customer.db" is the database name
        super(context, "customer.db", null, 1);
    }

    // called upon attempting to access a database for the first time
    // code must create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statements (as strings)
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CUSTOMER_NAME + " TEXT, " + COLUMN_CUSTOMER_AGE + " INT, " + COLUMN_ACTIVE_CUSTOMER + " BOOL)";

        db.execSQL(createTableStatement);
    }

    // called when database version number changes (prevents crash)
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Adds one customer to the table
     * @param customerModel
     * @return whether success or failure
     */
    public boolean addOne(CustomerModel customerModel) {
        // get the database that will be written to
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(); // like a hashmap

        // associate a column name with a value
        cv.put(COLUMN_CUSTOMER_NAME, customerModel.getName());
        cv.put(COLUMN_CUSTOMER_AGE, customerModel.getAge());
        cv.put(COLUMN_ACTIVE_CUSTOMER, customerModel.isActive());
        // Note: ID is auto-incremented in the database

        // insert the customerModel data as a row in the CUSTOMER_TABLE
        long insert = db.insert(CUSTOMER_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else { return true; }
    }

    /**
     * Find customerModel in the database.
     * If found, delete and return true.
     * Otherwise, return false.
     * @param customerModel
     * @return
     */
    public boolean deleteOne(CustomerModel customerModel) {
        // writable to permit deletion
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_ID + " = " + customerModel.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        // if an ID # is returned, return true (indicates success)
        if (cursor.moveToFirst()) {
            return true;
        } else { return false; }

    }

    // Create a method that SELECT-s all records from the database table
    // returns a list of CustomerModel
    public List<CustomerModel> getEveryone() {
        List<CustomerModel> returnList = new ArrayList<>();
        // get data from the database
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        // query returns a cursor (result set)
        Cursor cursor = db.rawQuery(queryString, null); // null b/c no prepared statements are in use here
        if (cursor.moveToFirst()) {
            // Loop through the cursor (result set) & create new customer objects
            // Insert the customer objects into the returnList.
            do {
                int customerID = cursor.getInt(0); // at pos. 0
                String customerName = cursor.getString(1); // at pos. 1
                int customerAge = cursor.getInt(2); // at pos. 2
                boolean customerActive = (cursor.getInt(3) == 1) ? true : false; // ternary operator

                CustomerModel newCustomer = new CustomerModel(customerID, customerName, customerAge, customerActive);
                returnList.add(newCustomer); // add the newCustomer to the returnList
            } while (cursor.moveToNext());
        } else {
            // Failure – do not add anything to the list
        }
        // clean up
        cursor.close();
        db.close();

        return returnList;
    }

}
