package edu.upc.fib.idi.idireceptes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import edu.upc.fib.idi.idireceptes.data.ReceptaContract.IngredientEntry;
import edu.upc.fib.idi.idireceptes.data.ReceptaContract.IngredientSubstitut;
import edu.upc.fib.idi.idireceptes.data.ReceptaContract.IngredientsReceptaEntry;
import edu.upc.fib.idi.idireceptes.data.ReceptaContract.ReceptaEntry;


/**
 * Created by casassg on 25/12/15.
 *
 * @author casassg
 */
public class ReceptaDBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "recepta.db";
    private static final int DATABASE_VERSION = 2;
    private long lastId = 7;

    public ReceptaDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(IngredientEntry.SQL_CREATE_TABLE);
        db.execSQL(ReceptaEntry.SQL_CREATE_TABLE);
        db.execSQL(IngredientsReceptaEntry.SQL_CREATE_TABLE);
        db.execSQL(IngredientSubstitut.SQL_CREATE_TABLE);

        //TRUITA
        db.insert(ReceptaEntry.TABLE_NAME, null, getTruita());

        db.insert(IngredientEntry.TABLE_NAME, null, getOus());
        db.insert(IngredientEntry.TABLE_NAME, null, getPatates());

        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getOus(), getTruita()));
        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getPatates(), getTruita()));


        //CALDO

        db.insert(ReceptaEntry.TABLE_NAME, null, getCaldo());

        db.insert(IngredientEntry.TABLE_NAME, null, getAigua());
        db.insert(IngredientEntry.TABLE_NAME, null, getCapDeRap());
        db.insert(IngredientEntry.TABLE_NAME, null, getCapDeLluc());
        db.insert(IngredientEntry.TABLE_NAME, null, getSal());

        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getAigua(), getCaldo()));
        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getSal(), getCaldo()));
        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getCapDeLluc(), getCaldo()));
        db.insert(IngredientSubstitut.TABLE_NAME, null, getRelacioSubstitutio(getCaldo(), getCapDeLluc(), getCapDeRap()));

        //Peix a la marinera

        db.insert(ReceptaEntry.TABLE_NAME, null, getPeixALaMarinera());

        db.insert(IngredientEntry.TABLE_NAME, null, getDorada());


        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getSal(), getPeixALaMarinera()));
        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getDorada(), getPeixALaMarinera()));
        db.insert(IngredientSubstitut.TABLE_NAME, null, getRelacioSubstitutio(getPeixALaMarinera(), getDorada(), getCapDeRap()));
        db.insert(IngredientSubstitut.TABLE_NAME, null, getRelacioSubstitutio(getPeixALaMarinera(), getDorada(), getCapDeLluc()));

        //Macarrons amb tomaquet
        db.insert(ReceptaEntry.TABLE_NAME, null, getMacarronsAmbTomaquet());

        db.insert(IngredientEntry.TABLE_NAME, null, getMacarrons());
        db.insert(IngredientEntry.TABLE_NAME, null, getTomaquet());


        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getMacarrons(), getMacarronsAmbTomaquet()));
        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getTomaquet(), getMacarronsAmbTomaquet()));
        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getSal(), getMacarronsAmbTomaquet()));
        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getAigua(), getMacarronsAmbTomaquet()));


        //Melo amb pernil

        db.insert(ReceptaEntry.TABLE_NAME, null, getMeloAmbPernil());

        db.insert(IngredientEntry.TABLE_NAME, null, getMelo());
        db.insert(IngredientEntry.TABLE_NAME, null, getPernil());

        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getPernil(), getMeloAmbPernil()));
        db.insert(IngredientsReceptaEntry.TABLE_NAME, null, getRelacioIngredientRecepta(getMelo(), getMeloAmbPernil()));



    }

    private ContentValues getMelo() {
        return getIngredientCV("Meló", 10);
    }

    private ContentValues getPernil() {
        return getIngredientCV("Pernil", 11);
    }

    private ContentValues getMeloAmbPernil() {
        ContentValues cv = new ContentValues();
        cv.put(ReceptaEntry.COL_NAME, "Meló amb pernil");
        String descr = "1.\tTallem el meló en troços allargats\n" +
                "2.\tPosem dos troços per plat (1 plat per persona)\n" +
                "3.\tTallem el pernil (en cas de no tenirlo tallat)\n" +
                "4.\tPosem 4 talls de pernil per plat\n" +
                "5.\tServim i degustem\n";
        cv.put(ReceptaEntry.COL_DESCR, descr);
        cv.put(ReceptaEntry._ID, 8);
        return cv;
    }

    private ContentValues getDorada() {
        return getIngredientCV("Dorada", 9);
    }

    private ContentValues getMacarrons() {
        return getIngredientCV("Macarrons", 7);
    }

    private ContentValues getTomaquet() {
        return getIngredientCV("Tomaquet", 8);
    }

    private ContentValues getMacarronsAmbTomaquet() {
        ContentValues cv = new ContentValues();
        cv.put(ReceptaEntry.COL_NAME, "Macarrons amb tomaquet");
        String descr = "1.\tPosem una cassola amb aigua a bullir\n" +
                "2.\tAfegim una culleradeta de sal a l'aigua\n" +
                "3.\tAfegim els macarrons i deixem que bullin per 10 minuts\n" +
                "4.\tTriturem els tomaquets a banda\n" +
                "5.\tRetirem els macarrons i colem\n" +
                "5.\tAfegim el tomaquet triturat\n" +
                "6.\tServim i degustem\n";
        cv.put(ReceptaEntry.COL_DESCR, descr);
        cv.put(ReceptaEntry._ID, 4);
        return cv;
    }

    private ContentValues getPeixALaMarinera() {
        ContentValues cv = new ContentValues();
        cv.put(ReceptaEntry.COL_NAME, "Cap de peix a la Barbacoa");
        String descr = "1.\tAfegim sal al peix\n" +
                "2.\tPosem el peix a a brasa\n" +
                "3.\tDeixem que quedi doradet\n" +
                "4.\tServim i degustem\n";
        cv.put(ReceptaEntry.COL_DESCR, descr);
        cv.put(ReceptaEntry._ID, 3);
        return cv;
    }

    private ContentValues getRelacioSubstitutio(ContentValues recepta, ContentValues ingre_primari, ContentValues ingre_substitut) {
        ContentValues cv = new ContentValues();
        cv.put(IngredientSubstitut.COL_ID_RECEPT, recepta.getAsInteger(ReceptaEntry._ID));
        cv.put(IngredientSubstitut.COL_ID_ING_PRIMARI, ingre_primari.getAsInteger(IngredientEntry._ID));
        cv.put(IngredientSubstitut.COL_ID_ING_SUBSTITUTIU, ingre_substitut.getAsInteger(IngredientEntry._ID));
        return cv;
    }

    private ContentValues getRelacioIngredientRecepta(ContentValues ingredient, ContentValues recepta) {
        ContentValues cv = new ContentValues();
        cv.put(IngredientsReceptaEntry.COL_ID_INGR, ingredient.getAsInteger(IngredientEntry._ID));
        cv.put(IngredientsReceptaEntry.COL_ID_RECEPT, recepta.getAsInteger(ReceptaEntry._ID));
        return cv;
    }

    private ContentValues getPatates() {
        return getIngredientCV("Patates", 2);
    }

    @NonNull
    private ContentValues getIngredientCV(String patates, int id) {
        ContentValues cv = new ContentValues();
        cv.put(IngredientEntry.COL_NAME, patates);
        cv.put(IngredientEntry._ID, id);
        return cv;
    }

    private ContentValues getOus() {
        return getIngredientCV("Ous", 1);
    }

    private ContentValues getAigua() {
        return getIngredientCV("Aigua", 3);
    }

    private ContentValues getSal() {
        return getIngredientCV("Sal", 4);
    }


    private ContentValues getCapDeRap() {
        return getIngredientCV("Cap de rap", 5);
    }

    private ContentValues getCapDeLluc() {
        return getIngredientCV("Cap de lluç", 6);
    }


    private ContentValues getTruita() {
        ContentValues cv = new ContentValues();
        cv.put(ReceptaEntry.COL_NAME, "Truita de patates");
        String descr = "1.\tObrir ous en un plat i batre\n" +
                "2.\tFregir patates en una cassola\n" +
                "3.\tAfegir els ous en la cassola\n" +
                "4.\tAl cap de uns minuts, si la truita esta feta per una banda, donar la volta.\n" +
                "5.\tI finalment, disfruta de la truita ben feta";
        cv.put(ReceptaEntry.COL_DESCR, descr);
        cv.put(ReceptaEntry._ID, 1);
        return cv;
    }

    private ContentValues getCaldo() {
        ContentValues cv = new ContentValues();
        cv.put(ReceptaEntry.COL_NAME, "Caldo de peix");
        cv.put(ReceptaEntry.COL_DESCR, "Posar aigua, bullir i degustar");
        cv.put(ReceptaEntry._ID, 2);
        return cv;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL(IngredientSubstitut.SQL_CREATE_TABLE);
        }

    }

}
