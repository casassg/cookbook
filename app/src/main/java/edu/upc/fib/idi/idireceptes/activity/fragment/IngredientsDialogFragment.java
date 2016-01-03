package edu.upc.fib.idi.idireceptes.activity.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.idi.idireceptes.R;

/**
 * Created by casassg on 02/01/16.
 *
 * @author casassg
 */
public class IngredientsDialogFragment extends DialogFragment {

    public static final String POSITION_KEY = "position";
    public static final String SUBSTITUTS_AGAFATS = "substituts";
    public final static String INGREDIENTS_KEY = "ingredients";
    private final static String TAG = IngredientsDialogFragment.class.getSimpleName();
    private String[] mIngredients;
    private ArrayList<String> mSelectedItems;
    private OnSaveDialogIngredientsListener listener;
    private int mPosition;

    public IngredientsDialogFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnSaveDialogIngredientsListener) activity;
        } catch (ClassCastException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void setArguments(Bundle args) {
        mIngredients = args.getStringArray(INGREDIENTS_KEY);
        mPosition = args.getInt(POSITION_KEY);
        mSelectedItems = args.getStringArrayList(SUBSTITUTS_AGAFATS);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (mIngredients == null || mSelectedItems == null) {
            dismiss();
        }
        boolean[] checked = new boolean[mIngredients.length];
        for (int i = 0; i < mIngredients.length; ++i) {
            checked[i] = mSelectedItems.contains(mIngredients[i]);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.select_ingredients)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(mIngredients, checked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(mIngredients[which]);
                                } else if (mSelectedItems.contains(mIngredients[which])) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(mIngredients[which]);
                                }
                            }
                        })
                        // Set the action buttons
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.saveSubstituts(mPosition, mSelectedItems);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIngredients == null || mIngredients.length == 0) {
            dismiss();
        }
    }

    public interface OnSaveDialogIngredientsListener {
        void saveSubstituts(int position, List<String> selectedItems);
    }

}
