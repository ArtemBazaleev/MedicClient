package com.example.medicapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.medicapp.R;
import com.example.medicapp.model.AdviceModel;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class AdviceParentAdapter extends RecyclerView.Adapter<AdviceParentAdapter.ViewHolder> {
    private Context mContext;
    private List<AdviceModel> mData;

    public AdviceParentAdapter(Context mContext, List<AdviceModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_advice_parent, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(mData.get(i));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView txt;
        TextView header;
        AdviceModel model;
        AdviceChildAdapter adapter;
        LinearLayoutManager layoutManager;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_advice_parent);
            txt  = itemView.findViewById(R.id.textView18);
            header = itemView.findViewById(R.id.textView23);
            layoutManager= new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        }


        public void bind(AdviceModel adviceModel) {
            model = adviceModel;
            if (model.getmMode() == AdviceModel.MODE_TXT) {
                recyclerView.setVisibility(View.GONE);
                txt.setText(model.getAdvice().getText());
                header.setText(model.getAdvice().getName());
                txt.setVisibility(View.VISIBLE);
            }
            else {
                txt.setVisibility(View.GONE);
                header.setText(model.getAdvice().getName());
                recyclerView.setVisibility(View.VISIBLE);
                adapter = new AdviceChildAdapter(model.getAdvice(), mContext, model.getmMode());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

        }
    }
}
