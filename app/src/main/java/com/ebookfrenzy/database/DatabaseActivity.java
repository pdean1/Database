package com.ebookfrenzy.database;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ebookfrenzy.database.model.Product;
import com.ebookfrenzy.database.model.dbaccess.MyDBHandler;

public class DatabaseActivity extends AppCompatActivity {

    TextView idView;
    EditText etName;
    EditText etQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        idView = (TextView) findViewById(R.id.tvID);
        etName = (EditText) findViewById(R.id.etName);
        etQuantity = (EditText) findViewById(R.id.etQuantity);
    }

    public void newProduct(View v) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Product p = new Product(etName.getText().toString(),
                Integer.parseInt(etQuantity.getText().toString()));
        dbHandler.addProduct(p);
        idView.setText("Record Added!");
        etName.setText("");
        etQuantity.setText("");
    }

    public void lookupProduct(View v) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
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
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        if (dbHandler.deleteProduct(etName.getText().toString())) {
            idView.setText("Record Deleted!");
            etName.setText("");
            etQuantity.setText("");
        } else {
            idView.setText("No Match Found!");
        }
    }

    public void updateProduct(View v) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        try {
            Product p = new Product(Integer.parseInt(idView.getText().toString()),
                    etName.getText().toString(), Integer.parseInt(etQuantity.getText().toString()));
            if (dbHandler.updateProduct(p)) {
                idView.setText("Product updated!");
            } else {
                idView.setText("No product found!");
            }
        } catch (Exception e) {
            idView.setText("Find a product first.");
        }
        etName.setText("");
        etQuantity.setText("");
    }

    public void deleteAllProducts(View v) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.deleteAllProducts();
        idView.setText("All products deleted");
        etName.setText("");
        etQuantity.setText("");
    }
}
