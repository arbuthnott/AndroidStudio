package com.example.w0276812.movietrailers;

/**
 * Created by w0276812 on 10/28/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    public static final String KEY_ROWID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION ="description";
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_URL ="url";
    public static final String KEY_RATING = "rating";
    public static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "MyDB";
    private static final String DATABASE_TABLE = "movies";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE =
            "create table movies(id integer primary key autoincrement,title text not null,description text not null,rating integer not null,"
                + "alias text not null,url text not null);";
            //"create table contacts(_id integer primary key autoincrement,name text not null,email text not null);";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }//end constructor

        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(DATABASE_CREATE);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }//end method onCreate

        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
            Log.w(TAG,"Upgrade database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }//end method onUpgrade
    }//end inner class dataBaseHelper

    //open the database
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //close the database
    public void close()
    {
        DBHelper.close();
    }

    // insert a movie into the database
    // Does not reference ids, so will create a new row.
    public long insertMovie(Movie movie) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, movie.getTitle());
        initialValues.put(KEY_DESCRIPTION, movie.getDescription());
        initialValues.put(KEY_RATING, movie.getRating());
        initialValues.put(KEY_ALIAS, movie.getAlias());
        initialValues.put(KEY_URL, movie.getUrl());
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //delete a particular movie
    public boolean deletebyId(long rowId) {
        return db.delete(DATABASE_TABLE,KEY_ROWID + "=" + rowId,null) >0;//0 used for bool return
    }

    // delete a particular movie
    // references movie id in the object.
    public boolean deleteMovie(Movie movie) {
        return db.delete(DATABASE_TABLE,KEY_ROWID + "=" + movie.getId(),null) > 0;//0 used for bool return
    }

    //delete the whole thing!
    public boolean deleteAll() {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }

    //retrieve all the movies
    public Cursor getAllMovies() {
        return db.query(DATABASE_TABLE,new String[]{KEY_ROWID,KEY_TITLE,KEY_DESCRIPTION,KEY_RATING,KEY_ALIAS,
                KEY_URL},null,null,null,null,null);
    }

    // retrieve a single movie
    // has the id.
    public Movie getMovie(long rowId) throws SQLException {
        Cursor cur = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_TITLE,KEY_DESCRIPTION,KEY_ALIAS,
                KEY_URL},KEY_ROWID + "=" + rowId,null,null,null,null,null);
        Movie movie = null;
        if(cur != null) {
            cur.moveToFirst();
            int id = cur.getInt(cur.getColumnIndex(KEY_ROWID));
            String title = cur.getString(cur.getColumnIndex(KEY_TITLE));
            String description = cur.getString(cur.getColumnIndex(KEY_DESCRIPTION));
            String alias = cur.getString(cur.getColumnIndex(KEY_ALIAS));
            String url = cur.getString(cur.getColumnIndex(KEY_URL));
            int rating = cur.getInt(cur.getColumnIndex(KEY_RATING));

            movie = new Movie(id, title, description, alias, url, rating);
        }
        return movie;
    }

    // updates a movie in the db
    // movie must have an id.
    public boolean updateMovie(Movie movie) {
        ContentValues cval = new ContentValues();
        cval.put(KEY_TITLE, movie.getTitle());
        cval.put(KEY_DESCRIPTION, movie.getDescription());
        cval.put(KEY_RATING, movie.getRating());
        cval.put(KEY_ALIAS, movie.getAlias());
        cval.put(KEY_URL, movie.getUrl());
        return db.update(DATABASE_TABLE, cval, KEY_ROWID + "=" + movie.getId(), null) > 0;
    }

    // update just the rating
    public boolean updateRating(long rowId, int rating) {
        ContentValues cval = new ContentValues();
        cval.put(KEY_RATING, rating);
        return db.update(DATABASE_TABLE, cval, KEY_ROWID + "=" + rowId, null) > 0;
    }

    // checks if the database has an item matching this movie title.
    public boolean inDataBase(Movie movie) {
        Cursor cur = getAllMovies();
        if(cur != null) {
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (cur.getString(cur.getColumnIndex("title")).equals(movie.getTitle())) {
                    return true;
                }
                cur.moveToNext();
            }
        }
        return false;
    }

}//end class DBAdapter

