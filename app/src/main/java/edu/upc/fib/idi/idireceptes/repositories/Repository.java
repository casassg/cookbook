package edu.upc.fib.idi.idireceptes.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.idi.idireceptes.model.Entity;

/**
 * Created by casassg on 25/12/15.
 *
 * @author casassg
 */
public abstract class Repository <T extends Entity>{

    private final SQLiteOpenHelper helper;

    public Repository(SQLiteOpenHelper helper){
        this.helper = helper;
    }

    public long insert(T object) {
        checkInsert(object);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = parseToContentValues(object);
        long id = db.insert(getTableName(),null,values);
        object.setId(id);
        db.close();
        return id;
    }

    public T get(long id){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                getTableName(),
                null,
                BaseColumns._ID + " = " + id,
                null, null, null, null
        );
        T ret = null;
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            ret = parseRow(cursor);
        }
        cursor.close();
        db.close();
        return  ret;
    }

    public List<T> getAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                getTableName(),
                null,
                null,
                null, null, null, null
        );
        List<T> ret = new ArrayList<>();
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            T tmp = parseRow(cursor);
            ret.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return ret;
    }

    protected abstract T parseRow(Cursor cursor);

    protected abstract String getTableName();

    protected abstract ContentValues parseToContentValues(T object);

    public abstract void checkInsert(T object);
}
