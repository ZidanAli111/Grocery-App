package android.example.groceryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.example.groceryapp.data.ProductContract.ProductEntry;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProductProvider extends ContentProvider {

    private static final int PRODUCTS = 100;
    private static final int PRODUCT_ID = 101;

    private ProductDbHelper productDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCT, PRODUCTS);
        Log.i("SIGMA", "THIS IS THE PRODUCTS URIMATCHER: " + sUriMatcher);
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCT + "/#", PRODUCT_ID);
    }


    @Override
    public boolean onCreate() {

        productDbHelper = new ProductDbHelper(getContext());

        return true;
    }

    @Override
    public Cursor query( Uri uri,  String[] projection, String selection,  String[] selectionArgs,  String sortOrder) {


        SQLiteDatabase sqLiteDatabase = productDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case PRODUCTS:
                cursor = sqLiteDatabase.query(ProductEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))};

                cursor = sqLiteDatabase.query(ProductEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);

                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI:" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);


        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PRODUCTS:
                return ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri + " with match " + match);

        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {

            case PRODUCTS:
                return insertProduct(uri, values);
            default:
                throw new IllegalArgumentException("Cannot insert unknown URI:" + uri);

        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {

        SQLiteDatabase sqLiteDatabase = productDbHelper.getWritableDatabase();

        String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product requires a name");

        }
        Integer grade = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_GRADE);
        if (grade == null || !ProductEntry.isValidGrade(grade)) {
            throw new IllegalArgumentException("Product requires valid grade");
        }

        Integer quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Product requires valid quantity");
        }
        long id = sqLiteDatabase.insert(ProductEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e("SIGMA", "Failed to insert row for :" + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int delete(Uri uri, String selection,String[] selectionArgs) {

        SQLiteDatabase sqLiteDatabase = productDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);

        switch (match) {

            case PRODUCTS:
                rowsDeleted = sqLiteDatabase.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))};

                rowsDeleted = sqLiteDatabase.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for:" + uri);

        }


        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;

    }

    @Override
    public int update( Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, values, selection, selectionArgs);
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))};

                return updateProduct(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for uri:" + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }

        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_GRADE)) {
            Integer grade = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_GRADE);
            if (grade == null || !ProductEntry.isValidGrade(grade)) {
                throw new IllegalArgumentException("Product requires valid grade");
            }
        }

        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY)) {
            Integer quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Product requires valid quantity");
            }

        }


        if (values.size() == 0)
            return 0;

        SQLiteDatabase sqLiteDatabase = productDbHelper.getWritableDatabase();


        int rowUpdated = sqLiteDatabase.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowUpdated;

    }
}
