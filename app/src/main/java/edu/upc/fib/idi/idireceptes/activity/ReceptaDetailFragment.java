package edu.upc.fib.idi.idireceptes.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.upc.fib.idi.idireceptes.R;
import edu.upc.fib.idi.idireceptes.model.Recepta;
import edu.upc.fib.idi.idireceptes.repositories.ReceptaRepository;
import edu.upc.fib.idi.idireceptes.util.Factory;

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
            ReceptaRepository repository = Factory.getInstanceReceptaRepository(getContext());
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String id = getArguments().getString(ARG_ITEM_ID);
            mItem = repository.get(Long.valueOf(id));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recepta_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.recepta_detail)).setText(mItem.getDescription());
        }

        return rootView;
    }
}