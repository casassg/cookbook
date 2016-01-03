package edu.upc.fib.idi.idireceptes.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.idi.idireceptes.data.ReceptaContract;
import edu.upc.fib.idi.idireceptes.data.ReceptaContract.ReceptaEntry;
import edu.upc.fib.idi.idireceptes.data.ReceptaDBHelper;
import edu.upc.fib.idi.idireceptes.model.Ingredient;
import edu.upc.fib.idi.idireceptes.model.Recepta;

/**
 * Created by casassg on 25/12/15.
 *
 * @author casassg
 */
public class ReceptaRepository extends Repository<Recepta>{

    private final IngredientsRepository ingredientRep;

    public ReceptaRepository(ReceptaDBHelper helper, IngredientsRepository ingre) {
        super(helper);
        ingredientRep = ingre;
    }

    @Override
    protected Recepta parseToObject(Cursor cursor) {
        Recepta recepta = new Recepta();
        String descr = cursor.getString(
                cursor.getColumnIndex(ReceptaEntry.COL_DESCR)
        );
        recepta.setDescription(descr);
        return recepta;
    }

    public Recepta getAmpliated(Long id) {
        Recepta recepta = get(id);
        List<Ingredient> ingredients = ingredientRep.getByRecepte(id);
        recepta.setIngredients(ingredients);
        return recepta;
    }

    @Override
    public long insert(Recepta object) {
        Long oldId = object.getId();
        if (oldId != null) {
            SQLiteDatabase db = helper.getWritableDatabase();
            db.delete(
                    ReceptaContract.IngredientsReceptaEntry.TABLE_NAME,
                    ReceptaContract.IngredientsReceptaEntry.COL_ID_RECEPT + " = " + oldId,
                    null
            );
            db.delete(
                    ReceptaContract.IngredientSubstitut.TABLE_NAME,
                    ReceptaContract.IngredientSubstitut.COL_ID_RECEPT + " = " + oldId,
                    null
            );
            db.close();
        }

        long id = super.insert(object);
        SQLiteDatabase db = helper.getWritableDatabase();
        for (Ingredient ingredient : object.getIngredients()) {
            ContentValues cv = new ContentValues();
            cv.put(ReceptaContract.IngredientsReceptaEntry.COL_ID_INGR, ingredient.getId());
            cv.put(ReceptaContract.IngredientsReceptaEntry.COL_ID_RECEPT, id);
            db.insert(ReceptaContract.IngredientsReceptaEntry.TABLE_NAME, null, cv);
            if (ingredient.getSubstituts() == null) {
                ingredient.setSubstituts(new ArrayList<Ingredient>());
            }
            for (Ingredient substitut : ingredient.getSubstituts()) {
                cv = new ContentValues();
                cv.put(ReceptaContract.IngredientSubstitut.COL_ID_RECEPT, id);
                cv.put(ReceptaContract.IngredientSubstitut.COL_ID_ING_PRIMARI, ingredient.getId());
                cv.put(ReceptaContract.IngredientSubstitut.COL_ID_ING_SUBSTITUTIU, substitut.getId());
                db.insert(ReceptaContract.IngredientSubstitut.TABLE_NAME, null, cv);
            }
        }
        db.close();
        return id;
    }

    @Override
    protected String getTableName() {
        return ReceptaEntry.TABLE_NAME;
    }

    @Override
    protected ContentValues parseToContentValues(Recepta object) {
        ContentValues values = new ContentValues();
        values.put(ReceptaEntry.COL_DESCR,object.getDescription());
        return values;
    }

    @Override
    public void checkInsert(Recepta object) {

    }
}
