package com.android.markit;

import java.util.ArrayList;
import com.android.markit.entry.Mark;
import com.android.markit.entry.MarkAdapter;
import com.android.markit.storage.ChecksSQLiteManager;
import com.android.markit.storage.IChecksSQLiteManager;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.app.ListActivity;

public class MarkItChecksListActivity extends ListActivity {

    private IChecksSQLiteManager dataManager;
    private int candidateForRemovingId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_it_checks_list);
        dataManager = new ChecksSQLiteManager(this);
        initializeListView();
        ListView listView =  getListView();
        registerForContextMenu(listView);
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int num, long id) {
                candidateForRemovingId = num;
                return false;
            }
            
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        getMenuInflater().inflate(R.menu.mark_it_checks_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_delete) {
            int deleteId = getListElementId(candidateForRemovingId);
            if(deleteId != -1)
            dataManager.deleteRow(deleteId);
            candidateForRemovingId = -1;
            initializeListView();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private int getListElementId(int num) {
        int id = -1;
        if(num != -1)
            id = ((Mark)getListAdapter().getItem(num)).getId();
        return id;
    }

    private void initializeListView() {
        if(dataManager != null) {
            ArrayList<Mark> checksList = dataManager.getValues();
            if(checksList != null) {
                MarkAdapter adapter = new MarkAdapter(this, R.layout.check_list_item, checksList);
                setListAdapter(adapter);
            }
        }
        else
            Log.i("MarkIt", "ChecksSQLiteManager is null");
    }
}