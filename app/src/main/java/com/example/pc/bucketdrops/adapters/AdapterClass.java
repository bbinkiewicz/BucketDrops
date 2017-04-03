package com.example.pc.bucketdrops.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pc.bucketdrops.R;
import com.example.pc.bucketdrops.beans.Drop;

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
            
            DropHolder dropHolder = (DropHolder) holder;
            Drop drop = mResult.get(position);
            
            dropHolder.setWhat(drop.getGoal());
            dropHolder.setBackground(drop.isCompleted());

           


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
        Context mContext;

        TextView mTextWhat;

        public DropHolder(View itemView, final MarkListener markListener, Realm realm, RealmResults realmResults) {
            super(itemView);
            mTextWhat = (TextView) itemView.findViewById(R.id.tv_what_row);
            mContext = itemView.getContext();
            mRealm = realm;
            mRealmResults = realmResults;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Drop drop = (Drop) mRealmResults.get(getAdapterPosition());
                    markListener.showDialogMark(drop);

                }
            });

        }

        private void setWhat(String text){
            mTextWhat.setText(text);
        }

        private void setBackground(boolean isCompleted){

            Drawable background;

            if(isCompleted){
                background = mContext.getDrawable(R.color.row_bg_completed);
            }

            else{
                background = mContext.getDrawable(R.drawable.row_drop_bg_color);
            }

            itemView.setBackground(background);


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
