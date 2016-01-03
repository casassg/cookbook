package edu.upc.fib.idi.idireceptes.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import edu.upc.fib.idi.idireceptes.R;

public class TextActivity extends AppCompatActivity {

    public static final String TYPE_TEXT_KEY = "typetext";
    public static final String HELP_TYPE = "help";
    public static final String ABOUT_TYPE = "about";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int titleId;
        int stringId;
        if (getIntent().hasExtra(TYPE_TEXT_KEY)) {
            String type = getIntent().getStringExtra(TYPE_TEXT_KEY);
            if (HELP_TYPE.equals(type)) {
                stringId = R.string.help;
                titleId = R.string.help_title;
            } else if (ABOUT_TYPE.equals(type)) {
                stringId = R.string.about;
                titleId = R.string.about_title;
            } else {
                finish();
                return;
            }
        } else {
            finish();
            return;
        }

        setTitle(titleId);
        toolbar.setTitle(titleId);
        ((TextView) findViewById(R.id.text)).setText(stringId);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
