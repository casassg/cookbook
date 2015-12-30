package edu.upc.fib.idi.idireceptes.data;

import android.provider.BaseColumns;

/**
 * Created by casassg on 25/12/15.
 *
 * @author casassg
 */
public class ReceptaContract {

    public interface BaseEntityColumns extends BaseColumns {
        String COL_NAME = "name";
    }

    public class ReceptaEntry implements BaseEntityColumns {
        public static final String TABLE_NAME="receptes";
        public static final String COL_DESCR="descr";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DESCR + " TEXT NOT NULL, " +
                COL_NAME + " TEXT NOT NULL, " +
                "UNIQUE (" + COL_NAME + ") ON CONFLICT REPLACE);";
    }

    public class IngredientEntry implements BaseEntityColumns {
        public static final String TABLE_NAME="ingredients";
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                "UNIQUE (" + COL_NAME + ") ON CONFLICT REPLACE);";
    }

    public class IngredientsReceptaEntry implements BaseColumns {
        public static final String TABLE_NAME="ingredientsreceptes";
        public static final String COL_ID_INGR="id_ingr";
        public static final String COL_ID_RECEPT="id_recept";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ID_INGR + " INTEGER NOT NULL, " +
                COL_ID_RECEPT + " INTEGER NOT NULL, " +
                "UNIQUE (" + COL_ID_INGR + "," + COL_ID_RECEPT + ") ON CONFLICT REPLACE," +
                " FOREIGN KEY (" + COL_ID_INGR + ") REFERENCES " +
                IngredientEntry.TABLE_NAME + " (" + IngredientEntry._ID + "), " +
                " FOREIGN KEY (" + COL_ID_RECEPT + ") REFERENCES " +
                ReceptaEntry.TABLE_NAME + " (" + ReceptaEntry._ID + "));";

    }

    public class IngredientSubstitut implements BaseColumns {
        public static final String TABLE_NAME = "ingredientsubstitut";
        public static final String COL_ID_ING_PRIMARI = "id_ingredient_primari";
        public static final String COL_ID_ING_SUBSTITUTIU = "id_ingredient_subtitutiu";
        public static final String COL_ID_RECEPT = "id_recept";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ID_ING_PRIMARI + " INTEGER NOT NULL, " +
                COL_ID_ING_SUBSTITUTIU + " INTEGER NOT NULL, " +
                COL_ID_RECEPT + " INTEGER NOT NULL, " +
                "UNIQUE (" + COL_ID_ING_SUBSTITUTIU + "," + COL_ID_ING_PRIMARI + "," + COL_ID_RECEPT + ") ON CONFLICT REPLACE," +
                " FOREIGN KEY (" + COL_ID_ING_PRIMARI + ") REFERENCES " +
                IngredientEntry.TABLE_NAME + " (" + IngredientEntry._ID + "), " +
                " FOREIGN KEY (" + COL_ID_ING_SUBSTITUTIU + ") REFERENCES " +
                IngredientEntry.TABLE_NAME + " (" + IngredientEntry._ID + "), " +
                " FOREIGN KEY (" + COL_ID_RECEPT + ") REFERENCES " +
                ReceptaEntry.TABLE_NAME + " (" + ReceptaEntry._ID + "));";


    }
}
