package com.example.pc.bucketdrops.widgets;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pc.bucketdrops.R;


public class DialogMark extends DialogFragment {


    private ImageButton mBtnClose;
    private Button mBtnCompleted;

    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (getView().getId()){

                case R.id.dialog_mark_btn:
                    break;


            }

            dismiss();

        }
    };



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

        if (!getArguments().isEmpty() && getArguments() != null) {

            if (getArguments().containsKey("position")) {
                int position = this.getArguments().getInt("position");
                Toast.makeText(getActivity(), position + "", Toast.LENGTH_SHORT).show();
            }
        }
        mBtnCompleted.setOnClickListener(mBtnClickListener);
        mBtnClose.setOnClickListener(mBtnClickListener);


    }

}
