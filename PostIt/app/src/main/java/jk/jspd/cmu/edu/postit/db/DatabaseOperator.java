package jk.jspd.cmu.edu.postit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by liukaiyu on 11/13/15.
 */
public class DatabaseOperator {
    // If one Facebook user is added, a new table of his friends will be stored in the database;
    private static final String DB_NAME = "PostIt";
    //private static int ID;
    private SQLiteDatabase database;
    private DatabaseOpenHelper dbOpenHelper;

    public DatabaseOperator(Context context) {
        dbOpenHelper = new DatabaseOpenHelper(context, DB_NAME, null, 1);
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper
    {
        // public constructor
        public DatabaseOpenHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, name, factory, version);
        } // end DatabaseOpenHelper constructor

        // creates the contacts table when the database is created
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            //get user id from facebook id
            // query to create a new table named contacts
            String createQuery = "CREATE TABLE userID" +
                    "(userID INT, friendsID INT);";

            db.execSQL(createQuery); // execute the query
        } // end method onCreate

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
        } // end method onUpgrade

    } // end class DatabaseOpenHelper

    public void open() throws SQLException
    {
        // create or open a database for reading/writing
        database = dbOpenHelper.getWritableDatabase();
    } // end method open

    // close the database connection
    public void close()
    {
        if (database != null)
            database.close(); // close the database connection
    } // end method close

    // inserts a new contact in the database
    public long insertRecord(int ID, float[] Scores)
    {
        return 0;
    } // end method insertContact


}
