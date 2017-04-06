package com.example.pc.bucketdrops;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.pc.bucketdrops.beans.Drop;

import java.util.Calendar;

import io.realm.Realm;


public class DialogAdd extends DialogFragment implements View.OnClickListener {

    private ImageButton closeButton;
    private EditText dialogInput;
    private Button addButton;
    private DatePicker datePicker;



    public DialogAdd(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        closeButton = (ImageButton) view.findViewById(R.id.btn_close);
        dialogInput = (EditText) view.findViewById(R.id.dialog_et);
        addButton = (Button) view.findViewById(R.id.btn_dialog_add_it);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);

        closeButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id){

            case R.id.btn_dialog_add_it:
                addAction();
                break;

        }
        dismiss();

    }
    // TODO process date
    private void addAction() {


        //get input from the user which is our goal
        //get the time when it was added
        //get date from the DatePicker

        String goal = dialogInput.getText().toString();
        long addTime = System.currentTimeMillis();
        long when = calculateTime();


        //setting up a realm


        Realm realm = Realm.getDefaultInstance();
        Drop drop = new Drop(addTime, goal, when, false);



        //write transaction
        realm.beginTransaction();
        realm.copyToRealm(drop);
        realm.commitTransaction();
        realm.close();


    }

    private long calculateTime(){
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, datePicker.getYear());
        calendar.set(Calendar.MONTH, datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

        return calendar.getTimeInMillis();
    }
}
