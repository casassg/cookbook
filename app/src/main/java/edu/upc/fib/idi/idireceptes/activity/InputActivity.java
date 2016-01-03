package edu.upc.fib.idi.idireceptes.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.idi.idireceptes.R;
import edu.upc.fib.idi.idireceptes.activity.fragment.IngredientsDialogFragment;
import edu.upc.fib.idi.idireceptes.model.Ingredient;
import edu.upc.fib.idi.idireceptes.model.Recepta;
import edu.upc.fib.idi.idireceptes.repositories.IngredientsRepository;
import edu.upc.fib.idi.idireceptes.repositories.ReceptaRepository;
import edu.upc.fib.idi.idireceptes.repositories.Repository;
import edu.upc.fib.idi.idireceptes.util.Factory;
import edu.upc.fib.idi.idireceptes.util.ImageTreat;
import edu.upc.fib.idi.idireceptes.util.Permissions;

public class InputActivity extends AppCompatActivity implements IngredientsDialogFragment.OnSaveDialogIngredientsListener {

    public static final String KEY_ID = "id_recepta";
    private static final int REQUEST_TAKE_PICTURE = 5;
    private static final int TARGET_INGREDIENTS = 42;
    private static final String KEY_PATH = "path";
    private final String TAG = InputActivity.class.getSimpleName();
    private String mCurrentPhotoPath;
    private List<IngredientHolder> ingredientsHolder;
    private ArrayList<String> nomsIngredients;
    private Recepta recepta;
    private boolean isModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        if (getIntent().hasExtra(KEY_ID)) {
            long id = getIntent().getLongExtra(KEY_ID, 0);
            recepta = Factory.getInstanceReceptaRepository(this).getAmpliated(id);
            isModify = true;
            EditText editTitol = (EditText) findViewById(R.id.titol);
            EditText editDescripcio = (EditText) findViewById(R.id.description);
            editDescripcio.setText(recepta.getDescription());
            editTitol.setText(recepta.getName());
        }
        if (recepta == null)
            recepta = new Recepta();

        if (isModify) {
            toolbar.setTitle(R.string.edita_recepta);

            String path = ImageTreat.getAbsolutePathImage(recepta);
            File f = new File(path);
            try {
                File tmp = createTempFile();
                if (f.exists()) {
                    ImageTreat.renameFile(tmp.getName(), path);
                    mCurrentPhotoPath = tmp.getAbsolutePath();
                    setImage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

        updateIngredients();


    }

    private void setImage() {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        ImageTreat.setImage(imageView, true, mCurrentPhotoPath, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Factory.tempRecepta = new WeakReference<Recepta>(recepta);
        if (mCurrentPhotoPath != null && !"".equals(mCurrentPhotoPath))
            outState.putString(KEY_PATH, mCurrentPhotoPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (Factory.tempRecepta != null)
            recepta = Factory.tempRecepta.get();
        mCurrentPhotoPath = savedInstanceState.getString(KEY_PATH);
        if (mCurrentPhotoPath != null && !"".equals(mCurrentPhotoPath))
            setImage();


        updateIngredients();


    }

    private void updateIngredients() {
        IngredientsRepository repository = Factory.getInstanceIngredientsRepositori(this);
        List<Ingredient> ingredients = repository.getAll();
        LinearLayout layout = (LinearLayout) findViewById(R.id.all_ingredients);
        layout.removeAllViewsInLayout();
        nomsIngredients = new ArrayList<>();
        ingredientsHolder = new ArrayList<>();
        int position = 0;
        for (Ingredient ingredient : ingredients) {
            View ingredientView = getLayoutInflater().inflate(R.layout.list_ingredients, null);
            layout.addView(ingredientView);
            if (recepta.hasIngredient(ingredient)) {
                for (Ingredient ingredientOriginal : recepta.getIngredients()) {
                    if (ingredientOriginal.equals(ingredient)) {
                        ingredient = ingredientOriginal;
                    }
                }
            }
            ingredientsHolder.add(new IngredientHolder(ingredientView, ingredient, position++));
            nomsIngredients.add(ingredient.getName());
        }
    }

    public void insertRecepta(View view) {
        ReceptaRepository repository = Factory.getInstanceReceptaRepository(getApplicationContext());

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
        recepta.setDescription(description);
        recepta.setName(title);


        long id = repository.insert(recepta);

        recepta.setId(id);
        String photoPath = this.mCurrentPhotoPath;
        String finalPath = ImageTreat.saveImageRecepta(recepta, photoPath);
        galleryAddPic(finalPath);

        Intent intent = new Intent(getApplicationContext(), ReceptaListActivity.class);
        intent.putExtra(ReceptaListActivity.ID_RECEPTA, id);
        setResult(RESULT_OK, intent);

        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                insertRecepta(findViewById(R.id.button));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCurrentPhotoPath != null) {
            File f = new File(mCurrentPhotoPath);
            if (f.exists() && !f.delete()) {
                Log.e(TAG, "Could not delete the temp photo");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PICTURE && resultCode == RESULT_OK) {

            File f = new File(mCurrentPhotoPath);
            if (f.exists()) {
                saveAndResizeFile(mCurrentPhotoPath);
                setImage();
            } else {
                Log.e(TAG, "Image just disapeared");
            }

        }
    }

    private void saveAndResizeFile(String mCurrentPhotoPath) {
        Bitmap bmp = ImageTreat.getBitmap(mCurrentPhotoPath);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(mCurrentPhotoPath);
            assert bmp != null;
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void galleryAddPic(String path) {
        if (path != null && !"".equals(path)) {
            Uri contentUri = Uri.fromFile(new File(path));
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Permissions.PERMISSION_REQUES_WRITE_EXTERNAL &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePicture();
        }
    }


    private void takePicture() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            Permissions.checkWritePermission(this);
            try {
                File image = createTempFile();
                mCurrentPhotoPath = image.getAbsolutePath();
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                startActivityForResult(takePicture, REQUEST_TAKE_PICTURE);
            } catch (IOException e) {
                Log.e(TAG, "Can't create temp image file");
            }

        }

    }

    @NonNull
    private File createTempFile() throws IOException {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                "temp",
                ".jpg",
                storageDir
        );
    }

    @Override
    public void saveSubstituts(int position, List<String> selectedItems) {
        ingredientsHolder.get(position).setSubstituts(selectedItems);
    }

    private List<Ingredient> getIngredientsFromList(List<String> selectedItems) {
        Repository<Ingredient> repository = Factory.getInstanceIngredientsRepositori(this);
        List<Ingredient> result = new ArrayList<>();
        for (String ingredientName : selectedItems) {
            result.add(repository.get(ingredientName));
        }
        return result;
    }

    public class IngredientHolder {
        public Ingredient mItem;
        public TextView mSubst;
        public Button mButton;
        public CheckBox mCheckBox;
        private int mPosition;
        private ArrayList<String> mSubstituts;

        public IngredientHolder(View rootView, Ingredient item, int position) {
            mItem = item;
            mPosition = position;
            mSubst = (TextView) rootView.findViewById(R.id.subtituts);
            mButton = (Button) rootView.findViewById(R.id.button);
            mCheckBox = (CheckBox) rootView.findViewById(R.id.checkBox);
            mCheckBox.setText(item.getName());

            if (recepta.hasIngredient(mItem)) {
                setIngredientState();
            }

            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mButton.setVisibility(View.VISIBLE);
                        recepta.addIngredient(mItem);
                    } else {
                        mButton.setVisibility(View.INVISIBLE);
                        recepta.removeIngredient(mItem);
                        mSubst.setText("");
                        mSubst.setVisibility(View.GONE);
                        mItem.setSubstituts(new ArrayList<Ingredient>());
                    }
                }
            });

            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment dialogFragment = new IngredientsDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArray(
                            IngredientsDialogFragment.INGREDIENTS_KEY,
                            nomsIngredients.toArray(new String[nomsIngredients.size()])
                    );
                    if (mSubstituts == null) {
                        mSubstituts = new ArrayList<>();
                    }
                    bundle.putStringArrayList(
                            IngredientsDialogFragment.SUBSTITUTS_AGAFATS,
                            mSubstituts
                    );
                    bundle.putInt(IngredientsDialogFragment.POSITION_KEY, mPosition);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getFragmentManager(), "ingredients");

                }
            });
        }

        private void setIngredientState() {
            mCheckBox.setChecked(true);
            mButton.setVisibility(View.VISIBLE);
            String ingredients = "";
            if (mSubstituts == null) {
                mSubstituts = new ArrayList<>();
            }
            if (mItem.getSubstituts() == null) return;
            for (Ingredient substitut : mItem.getSubstituts()) {
                ingredients += "\t\to " + substitut.getName() + "\n";
                mSubstituts.add(substitut.getName());
            }
            if (!"".equals(ingredients)) {
                mSubst.setText(ingredients);
                mSubst.setVisibility(View.VISIBLE);
            }
        }

        public void setSubstituts(List<String> selectedItems) {
            mSubstituts = new ArrayList<>(selectedItems);
            List<Ingredient> substituts = getIngredientsFromList(selectedItems);
            mItem.setSubstituts(substituts);
            String ingredients = "";
            for (String ingredient : selectedItems) {
                ingredients += "\t\to " + ingredient + "\n";
            }
            if (!"".equals(ingredients)) {
                mSubst.setText(ingredients);
                mSubst.setVisibility(View.VISIBLE);
            }
        }


    }


}
