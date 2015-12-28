package edu.upc.fib.idi.idireceptes.util;

import android.content.Context;

import edu.upc.fib.idi.idireceptes.data.ReceptaDBHelper;
import edu.upc.fib.idi.idireceptes.repositories.ReceptaRepository;

/**
 * Created by casassg on 27/12/15.
 *
 * @author casassg
 */
public final class Factory {
    public static ReceptaRepository getInstanceReceptaRepository(Context context) {
        return new ReceptaRepository(new ReceptaDBHelper(context));
    }
}
