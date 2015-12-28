package edu.upc.fib.idi.idireceptes.activity;

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
        Recepta recepta = new Recepta();
        recepta.setDescription(editDescripcio.getText().toString());
        recepta.setTitle(editTitol.getText().toString());
        ReceptaRepository repository = Factory.getInstanceReceptaRepository(getApplicationContext());
        repository.insert(recepta);
        finish();
    }
}
