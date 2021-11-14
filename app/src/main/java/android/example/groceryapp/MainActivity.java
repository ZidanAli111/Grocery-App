package android.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton floatingActionButton=findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView productView=(ListView) findViewById(R.id.list);
        View emptyView=findViewById(R.id.empty_view);

        productView.setEmptyView(emptyView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_catalog,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_insert_dummy_data:
                insertProduct();
                return true;

            case R.id.action_delete_all_entries:
                deleteProduct();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertProduct() {
    }

    private void deleteProduct() {
    }

}