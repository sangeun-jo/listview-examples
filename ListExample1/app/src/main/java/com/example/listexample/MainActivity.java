package com.example.listexample;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActionBar mActionbar;
    private ActionMode mActionMode;

    private ArrayList<String> items;
    private ArrayAdapter adapter;
    private ListView listview;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionbar = getSupportActionBar();

        items = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items);
        listview = findViewById(R.id.main_list);
        listview.setAdapter(adapter);
    }

    // 점 세개
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
               items.add("LIST" + (++count));
               adapter.notifyDataSetChanged();
                break;
            case R.id.edit:
                if(mActionMode == null){
                    mActionMode = startActionMode(mActionCallback);
                    mActionMode.setTitle("EDIT MODE");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    ActionMode.Callback mActionCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.edit_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            int count = adapter.getCount();
            SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
            switch (item.getItemId()) {
                case R.id.select_all:
                    for(int i = 0; i<count; i++){
                        listview.setItemChecked(i, true);
                    }
                    return true;
                case R.id.delete:
                    for(int i = count-1; i>=0; i--){
                        if(checkedItems.get(i)){
                            items.remove(i);
                        }
                    }
                    listview.clearChoices();
                    adapter.notifyDataSetChanged();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            listview.clearChoices();
            adapter.notifyDataSetChanged();

            mActionMode = null;
            mActionbar.show();
        }
    };
}