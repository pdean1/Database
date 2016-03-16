package com.ebookfrenzy.database;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.ebookfrenzy.database.model.Product;
import com.ebookfrenzy.database.model.dbaccess.MyDBHandler;

public class DatabaseActivity extends AppCompatActivity {

    MyDBHandler         dbHandler;
    SimpleCursorAdapter simpleCursorAdapter;
    TextView            idView;
    EditText            etName;
    EditText            etQuantity;
    ListView            lvProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        idView = (TextView) findViewById(R.id.tvID);
        etName = (EditText) findViewById(R.id.etName);
        etQuantity = (EditText) findViewById(R.id.etQuantity);
        lvProducts = (ListView) findViewById(R.id.productList);
        dbHandler = new MyDBHandler(this, null, null, 1);
        displayProductList();
    }

    public void newProduct(View v) {
        try {
            Product p = new Product(etName.getText().toString(),
                    Integer.parseInt(etQuantity.getText().toString()));
            dbHandler.addProduct(p);
            idView.setText("Record Added!");
            etName.setText("");
            etQuantity.setText("");
            displayProductList();
        } catch (Exception e) {
            idView.setText("Unable to add.\nTry again.");
        }
    }

    public void lookupProduct(View v) {
        Product p = dbHandler.findProduct(etName.getText().toString());
        if (p == null)
        {
            idView.setText("No Match Found!");
            return;
        }
        idView.setText(String.valueOf(p.getID()));
        etQuantity.setText(String.valueOf(p.getQuantity()));
    }

    public void deleteProduct(View v) {
        if (dbHandler.deleteProduct(etName.getText().toString())) {
            idView.setText("Record Deleted!");
            etName.setText("");
            etQuantity.setText("");
            displayProductList();
        } else {
            idView.setText("No Match \nFound!");
        }
    }

    public void updateProduct(View v) {
        try {
            Product p = new Product(Integer.parseInt(idView.getText().toString()),
                    etName.getText().toString(), Integer.parseInt(etQuantity.getText().toString()));
            if (dbHandler.updateProduct(p)) {
                idView.setText("Product updated!");
            } else {
                idView.setText("No product \nfound!");
            }
        } catch (Exception e) {
            idView.setText("Find a product \nfirst. Check \n all fields.");
        }
        etName.setText("");
        etQuantity.setText("");
        displayProductList();
    }

    public void deleteAllProducts(View v) {
        dbHandler.deleteAllProducts();
        idView.setText("All products \ndeleted");
        etName.setText("");
        etQuantity.setText("");
        lvProducts.setAdapter(null);
    }

    private void displayProductList() {
        try
        {
            Cursor cursor = dbHandler.getAllProducts();
            if (cursor == null)
            {
                idView.setText("Unable to generate cursor.");
                return;
            }
            if (cursor.getCount() == 0)
            {
                idView.setText("No Products in the Database.");
                return;
            }
            String[] columns = new String[] {
                    MyDBHandler.COLUMN_ID,
                    MyDBHandler.COLUMN_PRODUCTNAME,
                    MyDBHandler.COLUMN_QUANTITY
            };
            int[] boundTo = new int[] {
                    R.id.pId,
                    R.id.pName,
                    R.id.pQuantity
            };
            simpleCursorAdapter = new SimpleCursorAdapter(this,
                    R.layout.product_list,
                    cursor,
                    columns,
                    boundTo,
                    0);
            lvProducts.setAdapter(simpleCursorAdapter);
        }
        catch (Exception ex)
        {
            idView.setText("There was an error!");
        }
    }
}
