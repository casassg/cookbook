package edu.upc.fib.idi.idireceptes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import edu.upc.fib.idi.idireceptes.R;
import edu.upc.fib.idi.idireceptes.activity.fragment.ReceptaDetailFragment;

/**
 * An activity representing a single Recepta detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ReceptaListActivity}.
 */
public class ReceptaDetailActivity extends AppCompatActivity {

    private String idRecepta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepta_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        idRecepta = getIntent().getStringExtra(ReceptaDetailFragment.ARG_ITEM_ID);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InputActivity.class);
                intent.putExtra(InputActivity.KEY_ID, Long.valueOf(idRecepta));
                startActivityForResult(intent, ReceptaListActivity.NEW_RECEPTA);
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putString(ReceptaDetailFragment.ARG_ITEM_ID,
                    idRecepta);
            ReceptaDetailFragment fragment = new ReceptaDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recepta_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, ReceptaListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ReceptaListActivity.NEW_RECEPTA && resultCode == RESULT_OK) {
            Bundle arguments = new Bundle();
            arguments.putString(ReceptaDetailFragment.ARG_ITEM_ID,
                    idRecepta);
            ReceptaDetailFragment fragment = new ReceptaDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recepta_detail_container, fragment)
                    .commit();
        }
    }
}
