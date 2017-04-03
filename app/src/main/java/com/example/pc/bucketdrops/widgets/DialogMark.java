package com.example.pc.bucketdrops.widgets;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.pc.bucketdrops.R;
import com.example.pc.bucketdrops.beans.Drop;

import io.realm.Realm;


public class DialogMark extends DialogFragment {


    private ImageButton mBtnClose;
    private Button mBtnCompleted;
    private Realm mRealm = Realm.getDefaultInstance();

    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.dialog_mark_btn:

                    Bundle bundle = getArguments();

                    if(bundle.containsKey("drop")) {
                        Drop drop = (Drop) getArguments().get("drop");

                        if(drop != null) {
                            markAsCompleted(drop);
                        }
                    }
                    break;


            }

            dismiss();

        }
    };

    private void markAsCompleted(Drop drop) {

        mRealm.beginTransaction();
        drop.setCompleted(true);
        mRealm.copyToRealmOrUpdate(drop);
        mRealm.commitTransaction();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_mark, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);

        mBtnClose = (ImageButton) view.findViewById(R.id.dialog_mark_close);
        mBtnCompleted = (Button) view.findViewById(R.id.dialog_mark_btn);

        mBtnCompleted.setOnClickListener(mBtnClickListener);
        mBtnClose.setOnClickListener(mBtnClickListener);


    }

}
