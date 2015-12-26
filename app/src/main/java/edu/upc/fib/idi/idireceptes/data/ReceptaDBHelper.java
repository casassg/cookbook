package edu.upc.fib.idi.idireceptes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.upc.fib.idi.idireceptes.data.ReceptaContract.IngredientEntry;
import edu.upc.fib.idi.idireceptes.data.ReceptaContract.ReceptaEntry;
import edu.upc.fib.idi.idireceptes.data.ReceptaContract.IngredientsReceptaEntry;


/**
 * Created by casassg on 25/12/15.
 *
 * @author casassg
 */
public class ReceptaDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "recepta.db";

    public ReceptaDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_RECEPTA = "CREATE TABLE "+ ReceptaEntry.TABLE_NAME+ " ("+
                ReceptaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ReceptaEntry.COL_DESCR+ " TEXT NOT NULL, " +
                ReceptaEntry.COL_NAME+ " TEXT NOT NULL, " +
                "UNIQUE ("+ReceptaEntry.COL_NAME+") ON CONFLICT REPLACE);";
        final String SQL_CREATE_INGREDIENT = "CREATE TABLE " + IngredientEntry.TABLE_NAME+" ("+
                IngredientEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IngredientEntry.COL_NAME + " TEXT NOT NULL, " +
                "UNIQUE ("+IngredientEntry.COL_NAME+") ON CONFLICT REPLACE);";
        final  String SQL_CREATE_INGREDIENT_RECEPTA = "CREATE TABLE " + IngredientsReceptaEntry.TABLE_NAME+" ("+
                IngredientsReceptaEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IngredientsReceptaEntry.COL_ID_INGR + " INTEGER NOT NULL, "+
                IngredientsReceptaEntry.COL_ID_RECEPT + " INTEGER NOT NULL, "+
                "UNIQUE ("+IngredientsReceptaEntry.COL_ID_INGR+","+IngredientsReceptaEntry.COL_ID_RECEPT+") ON CONFLICT REPLACE," +
                " FOREIGN KEY (" + IngredientsReceptaEntry.COL_ID_INGR+ ") REFERENCES " +
                IngredientEntry.TABLE_NAME + " (" + IngredientEntry._ID+ "), " +
                " FOREIGN KEY (" + IngredientsReceptaEntry.COL_ID_RECEPT+ ") REFERENCES " +
                ReceptaEntry.TABLE_NAME + " (" + ReceptaEntry._ID+ "));";

        db.execSQL(SQL_CREATE_INGREDIENT);
        db.execSQL(SQL_CREATE_RECEPTA);
        db.execSQL(SQL_CREATE_INGREDIENT_RECEPTA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Implement upgrade

    }
}
