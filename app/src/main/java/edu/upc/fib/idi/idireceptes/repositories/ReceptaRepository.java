package edu.upc.fib.idi.idireceptes.repositories;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

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
