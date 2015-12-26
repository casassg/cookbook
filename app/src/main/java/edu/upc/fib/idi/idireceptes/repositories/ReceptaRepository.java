package edu.upc.fib.idi.idireceptes.repositories;

import android.content.ContentValues;
import android.database.Cursor;

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
    protected Recepta parseRow(Cursor cursor) {
        Recepta recepta = new Recepta();
        String name = cursor.getString(
                cursor.getColumnIndex(ReceptaEntry.COL_NAME)
        );
        String descr = cursor.getString(
                cursor.getColumnIndex(ReceptaEntry.COL_DESCR)
        );
        long id = cursor.getLong(
                cursor.getColumnIndex(ReceptaEntry._ID)
        );
        recepta.setTitle(name);
        recepta.setDescription(descr);
        recepta.setId(id);
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
        values.put(ReceptaEntry.COL_NAME,object.getTitle());
        values.put(ReceptaEntry.COL_DESCR,object.getDescription());
        return values;
    }

    @Override
    public void checkInsert(Recepta object) {

    }
}
