package android.example.groceryapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.example.groceryapp.data.ProductContract;
import android.example.groceryapp.data.ProductContract.ProductEntry;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

public class EditorActivity extends AppCompatActivity {

    private EditText productNameEditText;
    private EditText productModelEditText;
    private EditText productPriceEditText;
    private EditText supplierIdEditText;
    private Spinner productGradeSpinner;
    private EditText productQuantityEditText;
    private Button mAddProductButton;
    private Button mRejectProductButton;


    private int mGrade = ProductContract.ProductEntry.GRADE_UNKNOWN;
    private int mQuantity;

    private boolean mProductHasChanged = false;
    // BOOLEAN status for required fields,TRUE if these fields have been populated
    private boolean hasAllRequiredValues = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        productNameEditText = findViewById(R.id.p_name);

        productModelEditText = findViewById(R.id.p_model);

        productPriceEditText = findViewById(R.id.p_price);

        productGradeSpinner = findViewById(R.id.spinner_gender);

        supplierIdEditText = findViewById(R.id.s_id);

        productQuantityEditText = findViewById(R.id.p_quantity);

        mAddProductButton = (Button) findViewById(R.id.addProductButton);

        mRejectProductButton = (Button) findViewById(R.id.rejectProductButton);

        setupSpinner();


    }

    private void setupSpinner() {
        ArrayAdapter gradeSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_grade_options, R.layout.simple_spinner_item);

        gradeSpinnerAdapter.setDropDownViewResource(R.layout.simple_dropdown_item);


        productGradeSpinner.setAdapter(gradeSpinnerAdapter);


        productGradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("New")) {
                        mGrade = ProductContract.ProductEntry.GRADE_NEW;

                    } else if (selection.equals("Used")) {
                        mGrade = ProductContract.ProductEntry.GRADE_USED;

                    } else {
                        mGrade = ProductContract.ProductEntry.GRADE_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                mGrade = ProductContract.ProductEntry.GRADE_UNKNOWN;
            }
        });

    }


    private boolean insertProduct() {

        int quantity = 0;
        String nameString = productNameEditText.getText().toString().trim();
        String modelString = productModelEditText.getText().toString().trim();
        String quantityString = productQuantityEditText.getText().toString().trim();
        String priceString = productPriceEditText.getText().toString().trim();
        String supplierString = supplierIdEditText.getText().toString().trim();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ProductEntry.COLUMN_PRODUCT_MODEL, modelString);
        contentValues.put(ProductEntry.COLUMN_SUPPLER_ID, supplierString);

        // Validation section
        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, getString(R.string.validation_msg_product_name), Toast.LENGTH_SHORT).show();
            return hasAllRequiredValues;
        } else {
            contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, nameString);
        }

        if (TextUtils.isEmpty(quantityString)) {
            Toast.makeText(this, getString(R.string.validation_msg_product_quantity), Toast.LENGTH_SHORT).show();
            return hasAllRequiredValues;
        } else {
            // If the quantity is not provided by the user, don't try to parse the string into an
            // integer value. Use 0 by default.
            quantity = Integer.parseInt(quantityString);
            contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        }

        if (TextUtils.isEmpty(priceString)) {
            Toast.makeText(this, getString(R.string.validation_msg_product_price), Toast.LENGTH_SHORT).show();
            return hasAllRequiredValues;
        } else {
            contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, priceString);
        }

        return hasAllRequiredValues;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:

                saveProduct();

            case R.id.action_delete:

                showDeleteConfirmationDialog();


            case R.id.action_order_more:

                orderMore();

            case android.R.id.home:
                if (!mProductHasChanged) {

                    NavUtils.navigateUpFromSameTask(EditorActivity.this);

                    return true;
                }


                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes?");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep Editing ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();

                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void orderMore() {

    }


    private void showDeleteConfirmationDialog() {

    }


    private boolean saveProduct() {
        int quantity = 0;


        String nameString = productNameEditText.getText().toString().trim();
        String modelString = productModelEditText.getText().toString().trim();
        String quantityString = productQuantityEditText.getText().toString().trim();
        String priceString = productPriceEditText.getText().toString().trim();
        String supplierEmailString = supplierIdEditText.getText().toString().trim();

        ContentValues values = new ContentValues();

        // REQUIRED VALUES
        // Validation section
        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, getString(R.string.validation_msg_product_name), Toast.LENGTH_SHORT).show();
            return hasAllRequiredValues;
        } else {
            values.put(ProductEntry.COLUMN_PRODUCT_NAME, nameString);
        }

        if (TextUtils.isEmpty(quantityString)) {
            Toast.makeText(this, getString(R.string.validation_msg_product_quantity), Toast.LENGTH_SHORT).show();
            return hasAllRequiredValues;
        } else {
            // If the quantity is not provided by the user, don't try to parse the string into an
            // integer value. Use 0 by default.
            quantity = Integer.parseInt(quantityString);
            values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        }

        if (TextUtils.isEmpty(priceString)) {
            Toast.makeText(this, getString(R.string.validation_msg_product_price), Toast.LENGTH_SHORT).show();
            return hasAllRequiredValues;
        } else {
            values.put(ProductEntry.COLUMN_PRODUCT_PRICE, priceString);
        }

        values.put(ProductEntry.COLUMN_PRODUCT_MODEL,modelString);
        values.put(ProductEntry.COLUMN_SUPPLER_ID,supplierEmailString);


//        Uri newUri=getContentResolver().insert(ProductEntry.CONTENT_URI,values);
//      if (newUri==null)
//      {
//    setTitle("Add a Product");
//    mAddProductButton.setVisibility(View.GONE);
//    mRejectProductButton.setVisibility(View.GONE);
//    supplierIdEditText.setEnabled(true);
//    productQuantityEditText.setEnabled(true);
//
//    invalidateOptionsMenu();
//
//       }
//       else
//    {
//
//
//    }
        hasAllRequiredValues = true;
        return hasAllRequiredValues;
    }

    public void addItemButton(View view) {
        mQuantity++;
        displayQuantity();
    }

    public void rejectItemButton(View view) {
        if (mQuantity == 0) {
            Toast.makeText(this, "Can't decrease quantity", Toast.LENGTH_SHORT).show();
        } else {
            mQuantity--;
            displayQuantity();
        }
    }

    public void displayQuantity() {
        productQuantityEditText.setText(String.valueOf(mQuantity));
    }

}
