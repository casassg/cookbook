package edu.upc.fib.idi.idireceptes.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.idi.idireceptes.data.ReceptaContract.IngredientEntry;
import edu.upc.fib.idi.idireceptes.data.ReceptaContract.IngredientsReceptaEntry;
import edu.upc.fib.idi.idireceptes.model.Ingredient;

/**
 * Created by casassg on 29/12/15.
 *
 * @author casassg
 */
public class IngredientsRepository extends Repository<Ingredient> {
    public IngredientsRepository(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    protected Ingredient parseRow(Cursor cursor) {
        return new Ingredient();
    }

    @Override
    protected String getTableName() {
        return IngredientEntry.TABLE_NAME;
    }

    @Override
    protected ContentValues parseToContentValues(Ingredient object) {
        return new ContentValues();
    }

    @Override
    public void checkInsert(Ingredient object) {

    }

    public List<String> getByRecepte(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String SQL_SELECT =
                "SELECT i." + IngredientEntry.COL_NAME +
                        " FROM " + IngredientEntry.TABLE_NAME + " i, " + IngredientsReceptaEntry.TABLE_NAME + " ir" +
                        " WHERE ir." + IngredientsReceptaEntry.COL_ID_INGR + " = i." + IngredientEntry._ID + " AND" +
                        " ir." + IngredientsReceptaEntry.COL_ID_RECEPT + " = ?";
        Cursor cursor = db.rawQuery(SQL_SELECT, new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        List<String> ret = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            String tmp = cursor.getString(
                    cursor.getColumnIndex(IngredientEntry.COL_NAME)
            );
            ret.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return ret;
    }
}
