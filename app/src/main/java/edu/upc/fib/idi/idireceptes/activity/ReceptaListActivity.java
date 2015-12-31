package edu.upc.fib.idi.idireceptes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.upc.fib.idi.idireceptes.R;
import edu.upc.fib.idi.idireceptes.activity.fragment.ReceptaDetailFragment;
import edu.upc.fib.idi.idireceptes.model.Recepta;
import edu.upc.fib.idi.idireceptes.repositories.ReceptaRepository;
import edu.upc.fib.idi.idireceptes.util.Factory;
import edu.upc.fib.idi.idireceptes.util.ImageTreat;

/**
 * An activity representing a list of Receptes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ReceptaDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ReceptaListActivity extends AppCompatActivity {

    public static final String ID_RECEPTA = "id_recepta";
    static final int NEW_RECEPTA = 42;
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler.layout";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ReceptaRepository repository;
    private int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemPosition = 0;
        repository = Factory.getInstanceReceptaRepository(getApplicationContext());

        setContentView(R.layout.activity_recepta_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), InputActivity.class);
                startActivityForResult(intent, NEW_RECEPTA);
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recepta_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.recepta_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recepta_list);
        itemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recepta_list);
        recyclerView.scrollToPosition(itemPosition);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_RECEPTA && resultCode == RESULT_OK) {
            if (data.hasExtra(ID_RECEPTA)) {
                long id = data.getLongExtra(ID_RECEPTA, 1);
                openRecepta(id);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recepta_list);
        assert recyclerView != null;
        recyclerView.swapAdapter(new SimpleItemRecyclerViewAdapter(repository.getAll()), true);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(repository.getAll()));
    }

    private void openRecepta(long id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(ReceptaDetailFragment.ARG_ITEM_ID, String.valueOf(id));
            ReceptaDetailFragment fragment = new ReceptaDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recepta_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, ReceptaDetailActivity.class);
            intent.putExtra(ReceptaDetailFragment.ARG_ITEM_ID, String.valueOf(id));
            startActivity(intent);
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Recepta> mValues;

        public SimpleItemRecyclerViewAdapter(List<Recepta> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recepta_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mContentView.setText(mValues.get(position).getName());

            new ImageTreat(
                    ImageTreat.getAbsolutePathImage(holder.mItem),
                    holder.imageView,
                    4000,
                    200,
                    false
            );


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openRecepta(holder.mItem.getId());
                }
            });
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            super.onViewRecycled(holder);
            holder.imageView.setImageDrawable(null);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mContentView;
            public final ImageView imageView;
            public Recepta mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.content);
                imageView = (ImageView) view.findViewById(R.id.miniatura);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
