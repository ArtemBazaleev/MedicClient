package com.example.medicapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        private ResultModel model;
        public ResultHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constraintLayout5);
            constraintLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onResult(model);
        }

        public void bind(ResultModel model) {
            this.model = model;
        }
    }

    public interface IOnResultClicked{
        void onResult(ResultModel model);
    }
}
