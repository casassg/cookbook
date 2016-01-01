package edu.upc.fib.idi.idireceptes.activity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.upc.fib.idi.idireceptes.R;
import edu.upc.fib.idi.idireceptes.activity.ReceptaDetailActivity;
import edu.upc.fib.idi.idireceptes.activity.ReceptaListActivity;
import edu.upc.fib.idi.idireceptes.model.Ingredient;
import edu.upc.fib.idi.idireceptes.model.Recepta;
import edu.upc.fib.idi.idireceptes.repositories.ReceptaRepository;
import edu.upc.fib.idi.idireceptes.util.Factory;
import edu.upc.fib.idi.idireceptes.util.ImageTreat;

/**
 * A fragment representing a single Recepta detail screen.
 * This fragment is either contained in a {@link ReceptaListActivity}
 * in two-pane mode (on tablets) or a {@link ReceptaDetailActivity}
 * on handsets.
 */
public class ReceptaDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private static final String TAG = ReceptaDetailFragment.class.getSimpleName();
    /**
     * The dummy content this fragment is presenting.
     */
    private Recepta mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReceptaDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            ReceptaRepository repository = Factory.getInstanceReceptaRepository(getActivity().getApplicationContext());

            String id = getArguments().getString(ARG_ITEM_ID);
            assert id != null;
            mItem = repository.getAmpliated(Long.valueOf(id));


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mItem.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recepta_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.recepta_description)).setText(mItem.getDescription());
            String ingredients = "";
            for (Ingredient ingredient : mItem.getIngredients()) {
                ingredients += "- " + ingredient.getName() + "\n";
                for (Ingredient substitut : ingredient.getSubstituts()) {
                    ingredients += "\t\to " + substitut.getName() + "\n";
                }
            }
            if ("".equals(ingredients)) {
                ingredients = "Sense ingredients";
            }
            ((TextView) rootView.findViewById(R.id.recepta_ingredients)).setText(ingredients);

            ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageView);
            if (ImageTreat.hasImage(mItem)) {
                ImageTreat.setImage(mItem, imageView, true, false);
            }

        }

        return rootView;
    }

}
