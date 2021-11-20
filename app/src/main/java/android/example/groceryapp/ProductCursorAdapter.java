package android.example.groceryapp;

import static android.content.ContentValues.TAG;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.example.groceryapp.data.ProductContract;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.product_name);
        TextView modelTextView = (TextView) view.findViewById(R.id.product_model);
        TextView priceTextView = (TextView) view.findViewById(R.id.product_price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.product_current_quantity);
        ImageButton buyImageButton = (ImageButton) view.findViewById(R.id.product_buy_button);

        final int productIdColumnIndex = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry._ID));
        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int modelColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_MODEL);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);

        String productName = cursor.getString(nameColumnIndex);
        String productModel = cursor.getString(modelColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        final int quantityProduct = cursor.getInt(quantityColumnIndex);

        if (TextUtils.isEmpty(productModel)) {
            modelTextView.setVisibility(View.GONE);

            // Update the TextViews with the attributes for the current product
            nameTextView.setText(productName);
            modelTextView.setText(productModel);
            priceTextView.setText(productPrice);
            quantityTextView.setText(String.valueOf(quantityProduct));

            buyImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri productUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, productIdColumnIndex);
                    adjustProductQuantity(context, productUri, quantityProduct);
                }
            });

        }

    }

    private void adjustProductQuantity(Context context, Uri productUri, int currentQuantityInStock) {



            // Subtract 1 from current value if quantity of product >= 1
            int newQuantityValue = (currentQuantityInStock >= 1) ? currentQuantityInStock - 1 : 0;

            if (currentQuantityInStock == 0) {
                Toast.makeText(context.getApplicationContext(), "Product is out of stock!", Toast.LENGTH_SHORT).show();
            }

            // Update table by using new value of quantity
            ContentValues contentValues = new ContentValues();
            contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, newQuantityValue);
            int numRowsUpdated = context.getContentResolver().update(productUri, contentValues, null, null);
            if (numRowsUpdated > 0) {
                // Show error message in Logs with info about pass update.
                Log.i(TAG, "Item has been sold");
            } else {
                Toast.makeText(context.getApplicationContext(), "No available product in stock", Toast.LENGTH_SHORT).show();
                // Show error message in Logs with info about fail update.
                Log.e(TAG, "Issue with uploading value of quantity");
            }


        }
    }


