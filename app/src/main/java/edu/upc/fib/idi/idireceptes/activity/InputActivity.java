package edu.upc.fib.idi.idireceptes.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import edu.upc.fib.idi.idireceptes.R;
import edu.upc.fib.idi.idireceptes.model.Recepta;
import edu.upc.fib.idi.idireceptes.repositories.ReceptaRepository;
import edu.upc.fib.idi.idireceptes.util.Factory;
import edu.upc.fib.idi.idireceptes.util.ImageTreat;
import edu.upc.fib.idi.idireceptes.util.Permissions;

public class InputActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PICTURE = 5;
    private final String TAG = InputActivity.class.getSimpleName();
    private String mCurrentPhotoPath;

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
                takePicture();
            }
        });
        ActionBar supportActionBar = getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.setDisplayHomeAsUpEnabled(true);
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

        recepta.setId(id);
        String name = recepta.getImageFilename();
        String path = this.mCurrentPhotoPath;
        renameFile(name, path);

        Intent intent = new Intent(getApplicationContext(), ReceptaListActivity.class);
        intent.putExtra(ReceptaListActivity.ID_RECEPTA, id);
        setResult(RESULT_OK, intent);

        finish();

    }

    private void renameFile(String name, String path) {
        File from = new File(path);
        File directory = from.getParentFile();
        File to = new File(directory, name);
        if (!from.renameTo(to)) {
            Log.e(TAG, "Could not rename file");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PICTURE) {
            File f = new File(mCurrentPhotoPath);
            if (f.exists()) {
                Uri contentUri = Uri.fromFile(f);
                galleryAddPic(contentUri);
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                new ImageTreat(f.getAbsolutePath(), imageView, 100000, 1000, true);
            } else {
                Log.e(TAG, "Image just disapeared");
            }

        }
    }

    private void galleryAddPic(Uri contentUri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Permissions.PERMISSION_REQUES_WRITE_EXTERNAL &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePicture();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void takePicture() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            Permissions.checkWritePermission(this);
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File image = null;
            try {
                image = File.createTempFile(
                        "temp",
                        ".jpg",
                        storageDir
                );
                mCurrentPhotoPath = image.getAbsolutePath();
            } catch (IOException e) {
                Log.e(TAG, "Can't create temp image file");
//                e.printStackTrace();
            }
            if (image != null) {
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                startActivityForResult(takePicture, REQUEST_TAKE_PICTURE);
            }
        }

    }

}
