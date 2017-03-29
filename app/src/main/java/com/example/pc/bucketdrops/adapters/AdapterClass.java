package com.example.pc.bucketdrops.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pc.bucketdrops.R;
import com.example.pc.bucketdrops.beans.Drop;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;


public class AdapterClass extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {

    //viewType for footer returned by getItemViewType
    private final static int FOOTER = 1;
    //viewType for item of RealmResults
    private final static int ITEM = 0;
    private Realm mRealm;
    private LayoutInflater mInflater;
    private RealmResults<Drop> mResult;
    private MarkListener mMarkListener;




    public AdapterClass(Context context, Realm realm, RealmResults<Drop> result) {
        mInflater = LayoutInflater.from(context);
        mRealm = realm;
        update(result);



    }

    public AdapterClass(Context context, Realm realm, RealmResults<Drop> result, MarkListener markListener) {
        mInflater = LayoutInflater.from(context);
        mRealm = realm;
        mMarkListener = markListener;
        update(result);

    }

    public void update(RealmResults<Drop> result){

            mResult = result;
            notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {

        if (mResult == null || position < mResult.size()){
            return ITEM;
        }
        else
            return FOOTER;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;




        if (viewType == ITEM) {
            holder = new DropHolder(mInflater.inflate(R.layout.row_drop, parent, false), mMarkListener, mRealm, mResult);
        }
        else {
            holder = new FooterHolder(mInflater.inflate(R.layout.footer, parent, false));
        }

        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof DropHolder) {

            Drop drop = mResult.get(position);
            ((DropHolder)holder).mTextWhat.setText(drop.getGoal());


        }


    }

    @Override
    //+1 because of footer
    public int getItemCount() {

        if(mResult == null || mResult.isEmpty())
            return 0;

        return mResult.size()+1;
    }

    @Override
    public void onSwipe(int position) {

        if (position<mResult.size()) {
            mRealm.beginTransaction();
            mResult.get(position).deleteFromRealm();
            mRealm.commitTransaction();
            notifyItemRemoved(position);
        }

    }



    private static class DropHolder extends RecyclerView.ViewHolder {

        Realm mRealm;
        RealmResults mRealmResults;

        TextView mTextWhat;
        private DropHolder(View itemView, final MarkListener markListener, Realm realm, RealmResults realmResults) {
            super(itemView);
            mRealm = realm;
            mRealmResults = realmResults;
            mTextWhat = (TextView) itemView.findViewById(R.id.tv_what_row);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Drop drop = (Drop) mRealmResults.get(getAdapterPosition());
                    Log.d("isCompleted", String.format(Locale.ENGLISH, "before: %b", drop.isCompleted()));
                    markListener.showDialogMark(getAdapterPosition());
                    markAsCompleted(drop);
                    Log.d("isCompleted", String.format(Locale.ENGLISH, "after: %b", drop.isCompleted()));

                }
            });


        }

        private void markAsCompleted(Drop drop) {

            mRealm.beginTransaction();
            drop.setCompleted(true);
            mRealm.copyToRealmOrUpdate(drop);
            mRealm.commitTransaction();

        }
    }

    private static class FooterHolder extends RecyclerView.ViewHolder {

        Button footerButton;

        private FooterHolder(View itemView) {
            super(itemView);
            footerButton = (Button) itemView.findViewById(R.id.btn_footer);

        }
    }
}
