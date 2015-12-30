package edu.upc.fib.idi.idireceptes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import edu.upc.fib.idi.idireceptes.R;
import edu.upc.fib.idi.idireceptes.model.Recepta;
import edu.upc.fib.idi.idireceptes.repositories.ReceptaRepository;
import edu.upc.fib.idi.idireceptes.util.Factory;

public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void insertRecepta(View view) {
        EditText editTitol = (EditText) findViewById(R.id.titol);
        EditText editDescripcio = (EditText) findViewById(R.id.description);

        String description = editDescripcio.getText().toString();
        String title = editTitol.getText().toString();

        if ("".equals(title)) {
            Snackbar.make(view, R.string.title_missing, Snackbar.LENGTH_SHORT).show();
            return;
        } else if ("".equals(description)) {
            Snackbar.make(view, R.string.description_missing, Snackbar.LENGTH_SHORT).show();
            return;
        }
        Recepta recepta = new Recepta();
        recepta.setDescription(description);
        recepta.setName(title);

        ReceptaRepository repository = Factory.getInstanceReceptaRepository(getApplicationContext());
        long id = repository.insert(recepta);

        Intent intent = new Intent(getApplicationContext(), ReceptaListActivity.class);
        intent.putExtra(ReceptaListActivity.ID_RECEPTA, id);
        setResult(RESULT_OK, intent);

        finish();

    }
}
