package edu.upc.fib.idi.idireceptes.repositories;

import android.content.ContentValues;

import edu.upc.fib.idi.idireceptes.data.ReceptaContract.ReceptaEntry;
import edu.upc.fib.idi.idireceptes.data.ReceptaDBHelper;
import edu.upc.fib.idi.idireceptes.model.Recepta;

/**
 * Created by casassg on 25/12/15.
 *
 * @author casassg
 */
public class ReceptaRepository extends Repository<Recepta>{

    public ReceptaRepository(ReceptaDBHelper helper) {
        super(helper);
    }

    @Override
    protected String getTableName() {
        return ReceptaEntry.TABLE_NAME;
    }

    @Override
    protected ContentValues parseToContentValues(Recepta object) {
        ContentValues values = new ContentValues();
        values.put(ReceptaEntry.COL_DESCR,object.getDescription());
        values.put(ReceptaEntry.COL_NAME,object.getTitle());
        values.put(ReceptaEntry.COL_DESCR,object.getDescription());
        return null;
    }

    @Override
    public void checkInsert(Recepta object) {

    }
}
