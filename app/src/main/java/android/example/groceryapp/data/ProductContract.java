package android.example.groceryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ProductContract {


    private ProductContract() {
    }

    ;


    public static final String CONTENT_AUTHORITY = "com.example.android.product";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PRODUCT = "products";



    public static abstract class ProductEntry implements BaseColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT);


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT;


        public static final String TABLE_NAME = "products";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_PRODUCT_NAME = "name";

        public static final String COLUMN_PRODUCT_MODEL = "model";

        public static final String COLUMN_PRODUCT_GRADE = "grade";

        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";

        public static final String COLUMN_PRODUCT_PRICE = "price";

        public static final String COLUMN_SUPPLIER_ID = "SupplierId";


        public static final int GRADE_UNKNOWN = 0;
        public static final int GRADE_NEW = 1;
        public static final int GRADE_USED = 2;

        public static boolean isValidGrade(int grade) {

            if (grade == GRADE_UNKNOWN || grade == GRADE_NEW || grade == GRADE_USED) {
                return true;
            }
            return false;
        }


    }


}
