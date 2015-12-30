package edu.upc.fib.idi.idireceptes.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.idi.idireceptes.data.ReceptaContract.IngredientEntry;
import edu.upc.fib.idi.idireceptes.data.ReceptaContract.IngredientSubstitut;
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
    protected Ingredient parseToObject(Cursor cursor) {
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

    public List<Ingredient> getByRecepte(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String SQL_SELECT =
                "SELECT i." + IngredientEntry.COL_NAME + ", i." + IngredientEntry._ID +
                        " FROM " + IngredientEntry.TABLE_NAME + " i, " + IngredientsReceptaEntry.TABLE_NAME + " ir" +
                        " WHERE ir." + IngredientsReceptaEntry.COL_ID_INGR + " = i." + IngredientEntry._ID + " AND" +
                        " ir." + IngredientsReceptaEntry.COL_ID_RECEPT + " = ?";
        Cursor cursor = db.rawQuery(SQL_SELECT, new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        List<Ingredient> ret = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Ingredient tmp = relationalToObject(cursor);
            tmp.setSubstituts(getSubstituts(id, tmp.getId(), db));
            ret.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return ret;
    }

    private List<Ingredient> getSubstituts(long idRecepta, Long idIngredient, SQLiteDatabase db) {
        final String SQL_SELECT =
                "SELECT i." + IngredientEntry.COL_NAME + ", i." + IngredientEntry._ID +
                        " FROM " + IngredientEntry.TABLE_NAME + " i, " + IngredientSubstitut.TABLE_NAME + " isu" +
                        " WHERE isu." + IngredientSubstitut.COL_ID_ING_SUBSTITUTIU + " = i." + IngredientEntry._ID + " AND" +
                        " isu." + IngredientSubstitut.COL_ID_RECEPT + " = " + idRecepta + " AND" +
                        " isu." + IngredientSubstitut.COL_ID_ING_PRIMARI + " = " + idIngredient;
        Cursor cursor = db.rawQuery(SQL_SELECT, null);
        cursor.moveToFirst();
        List<Ingredient> ret = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Ingredient tmp = relationalToObject(cursor);
            ret.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();
        return ret;
    }
}
