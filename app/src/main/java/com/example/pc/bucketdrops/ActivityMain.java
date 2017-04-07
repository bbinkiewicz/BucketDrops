package com.example.pc.bucketdrops;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.bucketdrops.adapters.AdapterClass;
import com.example.pc.bucketdrops.adapters.Divider;
import com.example.pc.bucketdrops.adapters.MarkListener;
import com.example.pc.bucketdrops.adapters.SimpleTouchCallback;
import com.example.pc.bucketdrops.beans.Drop;
import com.example.pc.bucketdrops.widgets.BucketRecyclerView;
import com.example.pc.bucketdrops.widgets.DialogMark;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class ActivityMain extends AppCompatActivity {

    BucketRecyclerView mRecycler;
    Realm mRealm;
    RealmResults<Drop> mResult;
    AdapterClass adapter;
    View mEmptyView;
    SimpleTouchCallback mCallback;
    Toolbar mToolbar;

    private MarkListener mMarkListener = new MarkListener() {
        @Override
        public void showDialogMark(Drop drop) {

            DialogMark dialog = new DialogMark();
            Bundle bundle = new Bundle();
            bundle.putSerializable("drop", drop);


            dialog.setArguments(bundle);
            dialog.show(getFragmentManager(), "show");

        }
    };

    private RealmChangeListener callback = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {

            if (mResult!=null) {
                adapter.update(mResult);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView nothingToShowTextView = (TextView) findViewById(R.id.nothing_to_show);
        setSupportActionBar(mToolbar);
        initBackgroundImage();


        //realm
        mRealm = Realm.getDefaultInstance();
        mResult = mRealm.where(Drop.class).findAllAsync();

        mEmptyView = findViewById(R.id.empty_drops);



        //recyclerView

        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_drops);
        mRecycler.hideIfEmpty(mToolbar);
        mRecycler.showIfEmpty(mEmptyView,nothingToShowTextView);
        mRecycler.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));

            adapter = new AdapterClass(this, mRealm, mResult, mMarkListener);
            mRecycler.setAdapter(adapter);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            mRecycler.setLayoutManager(manager);

        //swipe to delete
        mCallback = new SimpleTouchCallback(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(mCallback);
        helper.attachToRecyclerView(mRecycler);

        //shared preferences
        loadPreference();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_add){
            showDialogAdd(new View(this));
        }

        else {
            savePreference(id);
            loadPreference();
        }

        return super.onOptionsItemSelected(item);
    }

    private void savePreference(int filterValue){
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("filter", filterValue);
        editor.apply();
    }

    private void loadPreference(){


        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        int value = pref.getInt("filter", 0);

        switch(value) {
            case R.id.action_show_complete:
                mResult = mRealm.where(Drop.class).equalTo("isCompleted", true).findAllAsync();
                break;

            case R.id.action_show_incomplete:
                mResult = mRealm.where(Drop.class).equalTo("isCompleted", false).findAllAsync();
                break;

            case R.id.action_sort_descending_date:
                mResult = mRealm.where(Drop.class).findAllSortedAsync("when", Sort.DESCENDING);
                break;

            case R.id.action_sort_ascending_date:
            default:
                mResult = mRealm.where(Drop.class).findAllSortedAsync("when");
                break;
        }
        mResult.addChangeListener(callback);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mResult.addChangeListener(callback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResult.removeAllChangeListeners();

    }

    public void showDialogAdd(View view) {

        DialogAdd dialog = new DialogAdd();
        dialog.show(getFragmentManager(), "Add");

    }

    private void initBackgroundImage(){

        ImageView background = (ImageView) findViewById(R.id.iv_background);
        Glide.with(this).load(R.drawable.background).centerCrop().into(background);
    }

}
