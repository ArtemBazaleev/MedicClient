package com.example.medicapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.medicapp.R;
import com.example.medicapp.model.ResultModel;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultHolder> {

    private Context mContext;
    private List<ResultModel> mData;
    private IOnResultClicked mListener;

    public ResultsAdapter(Context mContext, List<ResultModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setmListener(IOnResultClicked mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_result_card, viewGroup,false);
        return new ResultHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder resultHolder, int i) {
        resultHolder.bind(mData.get(i));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ConstraintLayout constraintLayout;
        private ConstraintLayout clicker;
        private ResultModel model;
        private TextView date;
        private TextView conclusion;
        private TextView desc;

        ResultHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constraintLayout5);
            clicker = itemView.findViewById(R.id.conclusion_item_result);
            date = itemView.findViewById(R.id.textView8);
            conclusion = itemView.findViewById(R.id.text_conclusion);
            desc = itemView.findViewById(R.id.textView9);
            clicker.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onResult(model);
        }

        public void bind(ResultModel model) {
            this.model = model;
            if (model.getType() == ResultModel.TYPE_CONCLUSION){
                constraintLayout.setVisibility(View.GONE);
                date.setText(model.getCreated());
                conclusion.setText(model.getDesc());
                conclusion.setVisibility(View.VISIBLE);
            }
            else {
                desc.setText(model.getDesc());
                date.setText(model.getCreated());
                constraintLayout.setVisibility(View.VISIBLE);
                conclusion.setVisibility(View.GONE);
            }
        }
    }

    public interface IOnResultClicked{
        void onResult(ResultModel model);
    }
}
