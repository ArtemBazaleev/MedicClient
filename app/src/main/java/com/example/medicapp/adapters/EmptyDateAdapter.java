package com.example.medicapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.medicapp.model.EmptyDateModel;
import com.example.medicapp.R;

import java.util.List;

public class EmptyDateAdapter extends RecyclerView.Adapter<EmptyDateAdapter.EmptyDateHolder> {

    private Context mContext;
    private OnItemClicked mListener;

    public void setmListener(OnItemClicked l ){
        this.mListener = l;
    }

    public EmptyDateAdapter(Context mContext, List<EmptyDateModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    private List<EmptyDateModel> mData;
    @NonNull
    @Override
    public EmptyDateHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_empty_date, viewGroup,false);
        return new EmptyDateHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EmptyDateHolder emptyDateHolder, int i) {
        emptyDateHolder.onBind(mData.get(i),i);
    }

    private void onChekedChanged(int position){
        for (int i = 0; i < getItemCount(); i++) {
            if (i != position)
                mData.get(i).setSelected(false);
        }
        this.notifyDataSetChanged();
    }

    private void unCheckAll(){
        for (EmptyDateModel i:mData)
            i.setSelected(false);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class EmptyDateHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ConstraintLayout layout;
        private int position;
        private TextView textView;
        private EmptyDateModel model;
        EmptyDateHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.constraint_empty_date);
            textView = itemView.findViewById(R.id.textView2);
            layout.setOnClickListener(this);
        }

        void onBind(EmptyDateModel model,int position){
            this.position = position;
            this.model = model;
            textView.setText(model.getTime());
            if (model.isSelected()){
                layout.setBackgroundResource(R.drawable.calendar_chosen_bg);
                textView.setTextColor(Color.WHITE);
            }
            else {
                layout.setBackgroundResource(R.drawable.round_white_bg);
                textView.setTextColor(Color.BLACK);
            }
        }

        @Override
        public void onClick(View v) {
            model.setSelected(true);
            onChekedChanged(position);
            mListener.onItemClicked(model);
        }
    }

    public interface OnItemClicked{
        void onItemClicked(EmptyDateModel model);
    }
}
