package edu.upc.fib.idi.idireceptes.data;

import android.provider.BaseColumns;

/**
 * Created by casassg on 25/12/15.
 *
 * @author casassg
 */
public class ReceptaContract {

    public class ReceptaEntry implements BaseColumns {
        public static final String TABLE_NAME="receptes";
        public static final String COL_DESCR="descr";
        public static final String COL_NAME="name";
    }

    public  class IngredientEntry implements BaseColumns {
        public static final String TABLE_NAME="ingredients";
        public static final String COL_NAME="name";
    }

    public class IngredientsReceptaEntry implements BaseColumns {
        public static final String TABLE_NAME="ingredientsreceptes";
        public static final String COL_ID_INGR="id_ingr";
        public static final String COL_ID_RECEPT="id_recept";
    }
}
