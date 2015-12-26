package edu.upc.fib.idi.idireceptes.repositories;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by casassg on 25/12/15.
 *
 * @author casassg
 */
public abstract class Repository <T>{

    private final SQLiteOpenHelper helper;

    public Repository(SQLiteOpenHelper helper){
        this.helper = helper;
    }

    public void insert(T object) {
        checkInsert(object);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = parseToContentValues(object);
        db.insert(getTableName(),null,values);
    }

    protected abstract String getTableName();

    protected abstract ContentValues parseToContentValues(T object);

    public abstract void checkInsert(T object);
}
