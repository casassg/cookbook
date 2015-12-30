package edu.upc.fib.idi.idireceptes.util;

import android.content.Context;
import android.support.annotation.NonNull;

import edu.upc.fib.idi.idireceptes.data.ReceptaDBHelper;
import edu.upc.fib.idi.idireceptes.repositories.IngredientsRepository;
import edu.upc.fib.idi.idireceptes.repositories.ReceptaRepository;

/**
 * Created by casassg on 27/12/15.
 *
 * @author casassg
 */
public final class Factory {
    public static ReceptaRepository getInstanceReceptaRepository(Context context) {
        return new ReceptaRepository(getHelper(context), getInstanceIngredientsRepositori(context));
    }

    @NonNull
    private static ReceptaDBHelper getHelper(Context context) {
        return new ReceptaDBHelper(context);
    }

    public static IngredientsRepository getInstanceIngredientsRepositori(Context context) {
        return new IngredientsRepository(getHelper(context));
    }
}
