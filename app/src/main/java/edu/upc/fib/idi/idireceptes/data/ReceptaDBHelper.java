package edu.upc.fib.idi.idireceptes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import edu.upc.fib.idi.idireceptes.data.ReceptaContract.IngredientEntry;
import edu.upc.fib.idi.idireceptes.data.ReceptaContract.IngredientSubstitut;
import edu.upc.fib.idi.idireceptes.data.ReceptaContract.IngredientsReceptaEntry;
import edu.upc.fib.idi.idireceptes.data.ReceptaContract.ReceptaEntry;


/**
 * Created by casassg on 25/12/15.
 *
 * @author casassg
 */
public class ReceptaDBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "recepta.db";
    private static final int DATABASE_VERSION = 2;

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

        db.execSQL(IngredientEntry.SQL_CREATE_TABLE);
        db.execSQL(ReceptaEntry.SQL_CREATE_TABLE);
        db.execSQL(IngredientsReceptaEntry.SQL_CREATE_TABLE);

        //TRUITA
        db.insert(ReceptaEntry.TABLE_NAME, null, getTruita());

        db.insert(IngredientEntry.TABLE_NAME, null, getOus());
        db.insert(IngredientEntry.TABLE_NAME, null, getPatates());

        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getOus(), getTruita()));
        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getPatates(), getTruita()));


        //CALDO

        db.insert(ReceptaEntry.TABLE_NAME, null, getCaldo());

        db.insert(IngredientEntry.TABLE_NAME, null, getAigua());
        db.insert(IngredientEntry.TABLE_NAME, null, getCapDeLluc());
        db.insert(IngredientEntry.TABLE_NAME, null, getSal());

        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getAigua(), getCaldo()));
        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getSal(), getCaldo()));
        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getCapDeLluc(), getCaldo()));

        createSubstitut(db);

    }

    private void createSubstitut(SQLiteDatabase db) {


        db.insert(IngredientEntry.TABLE_NAME, null, getCapDeRap());

        db.execSQL(IngredientSubstitut.SQL_CREATE_TABLE);


        ContentValues cv = getRelacioSubstitutio(getCaldo(), getCapDeLluc(), getCapDeRap());
        db.insert(IngredientSubstitut.TABLE_NAME, null, cv);

    }

    private ContentValues getRelacioSubstitutio(ContentValues recepta, ContentValues ingre_primari, ContentValues ingre_substitut) {
        ContentValues cv = new ContentValues();
        cv.put(IngredientSubstitut.COL_ID_RECEPT, recepta.getAsInteger(ReceptaEntry._ID));
        cv.put(IngredientSubstitut.COL_ID_ING_PRIMARI, ingre_primari.getAsInteger(IngredientEntry._ID));
        cv.put(IngredientSubstitut.COL_ID_ING_SUBSTITUTIU, ingre_substitut.getAsInteger(IngredientEntry._ID));
        return cv;
    }

    private ContentValues getRelacioIngredientRecepta(ContentValues ingredient, ContentValues recepta) {
        ContentValues cv = new ContentValues();
        cv.put(IngredientsReceptaEntry.COL_ID_INGR, ingredient.getAsInteger(IngredientEntry._ID));
        cv.put(IngredientsReceptaEntry.COL_ID_RECEPT, recepta.getAsInteger(ReceptaEntry._ID));
        return cv;
    }

    private ContentValues getPatates() {
        ContentValues cv = new ContentValues();
        cv.put(IngredientEntry.COL_NAME, "Patates");
        cv.put(IngredientEntry._ID, 2);
        return cv;
    }

    private ContentValues getOus() {
        ContentValues cv = new ContentValues();
        cv.put(IngredientEntry.COL_NAME, "Ous");
        cv.put(IngredientEntry._ID, 1);
        return cv;
    }

    private ContentValues getAigua() {
        ContentValues cv = new ContentValues();
        cv.put(IngredientEntry.COL_NAME, "Aigua");
        cv.put(IngredientEntry._ID, 3);
        return cv;
    }

    private ContentValues getSal() {
        ContentValues cv = new ContentValues();
        cv.put(IngredientEntry.COL_NAME, "Sal");
        cv.put(IngredientEntry._ID, 4);
        return cv;
    }


    private ContentValues getCapDeRap() {
        ContentValues cv = new ContentValues();
        cv.put(IngredientEntry.COL_NAME, "Cap de rap");
        cv.put(IngredientEntry._ID, 5);
        return cv;
    }

    private ContentValues getCapDeLluc() {
        ContentValues cv = new ContentValues();
        cv.put(IngredientEntry.COL_NAME, "Cap de lluç");
        cv.put(IngredientEntry._ID, 6);
        return cv;
    }


    private ContentValues getTruita() {
        ContentValues cv = new ContentValues();
        cv.put(ReceptaEntry.COL_NAME, "Truita");
        cv.put(ReceptaEntry.COL_DESCR, "Obrir ou en cassola, coure, extreure");
        cv.put(ReceptaEntry._ID, 1);
        return cv;
    }

    private ContentValues getCaldo() {
        ContentValues cv = new ContentValues();
        cv.put(ReceptaEntry.COL_NAME, "Caldo de peix");
        cv.put(ReceptaEntry.COL_DESCR, "Posar aigua, bullir i degustar");
        cv.put(ReceptaEntry._ID, 2);
        return cv;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            createSubstitut(db);
        }

    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }
}
