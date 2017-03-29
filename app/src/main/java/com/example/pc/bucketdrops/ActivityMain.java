package com.example.pc.bucketdrops;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

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

public class ActivityMain extends AppCompatActivity {

    BucketRecyclerView mRecycler;
    Realm mRealm;
    RealmResults<Drop> mResult;
    AdapterClass adapter;
    View mEmptyView;
    SimpleTouchCallback mCallback;

    private MarkListener mMarkListener = new MarkListener() {
        @Override
        public void showDialogMark(int position) {

            DialogMark dialog = new DialogMark();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);

            dialog.setArguments(bundle);
            dialog.show(getFragmentManager(), "Add");



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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        initBackgroundImage();

        //realm
        mRealm = Realm.getDefaultInstance();
        mResult = mRealm.where(Drop.class).findAllAsync();

        mEmptyView = findViewById(R.id.empty_drops);



        //recyclerView

        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_drops);
        mRecycler.hideIfEmpty(toolbar);
        mRecycler.showIfEmpty(mEmptyView);
        mRecycler.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));

            adapter = new AdapterClass(this, mRealm, mResult, mMarkListener);
            mRecycler.setAdapter(adapter);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            mRecycler.setLayoutManager(manager);

        //swipe to delete
        mCallback = new SimpleTouchCallback(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(mCallback);
        helper.attachToRecyclerView(mRecycler);

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
