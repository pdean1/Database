package com.ebookfrenzy.database.model.dbaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ebookfrenzy.database.model.Product;

/**
 * Created by Patrick on 2/20/2016.
 */
public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    public static final String TABLE_PRODUCTS = "products";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_QUANTITY = "quantity";

    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_products_table = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_PRODUCTNAME + " TEXT," +
                COLUMN_QUANTITY + " INTEGER )";
        db.execSQL(create_products_table);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    /**
     * Gets all Products in the Database and returns a cursor of them.
     * If there are no items in the database then the cursor returns null
     *
     * @return A Cursor of all products or null
     */
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, new String[] {COLUMN_ID, COLUMN_PRODUCTNAME,
                COLUMN_QUANTITY}, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        else
        {
            return null;
        }
    }

    /**
     * Adds a product to the database
     * @param product the product to be added.
     */
    public void addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    /**
     * Finds a product in the database and returns it to the caller. If this function does not find
     * a product, then the returned product is null
     * @param productname Name of the product to find
     * @return A valid product or a null product
     */
    public Product findProduct(String productname) {
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME +
                " = \"" + productname + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Product p = new Product();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            p.setID(Integer.parseInt(cursor.getString(0)));
            p.setProductName(cursor.getString(1));
            p.setQuantity(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        } else {
            p = null;
        }
        db.close();
        return p;
    }

    /**
     * This function delete's a product in TABLE_PRODUCTS based on the ID of the product retrieved
     * by it's product name.
     * @param productname The name of the product to delete
     * @return True if deleted false otherwise.
     */
    public boolean deleteProduct(String productname) {
        boolean result = false;
        String q = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME
                + " = \"" + productname + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        Product p = new Product();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            p.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(p.getID())});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    /**
     * Updates the product passed to this function.
     * @param p The product to update
     * @return True if updated, false otherwise.
     */
    public boolean updateProduct(Product p) {
        boolean result = false;
        String q = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_ID + " = " + p.getID();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(q, null);
        if (c.moveToFirst())
        {
            String q2 = "UPDATE " + TABLE_PRODUCTS + " SET " + COLUMN_PRODUCTNAME + " = \""
                    + p.getProductName() + "\", " + COLUMN_QUANTITY + " = " + p.getQuantity()
                    + " WHERE " + COLUMN_ID + " = " + p.getID();
            db.execSQL(q2);
            result = true;
        }
        db.close();
        return result;
    }

    /**
     * Deletes all records in the product database regardless if the
     * database contains records or not.
     */
    public void deleteAllProducts()
    {
        String q = "DELETE FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(q);
        db.close();
    }
}
