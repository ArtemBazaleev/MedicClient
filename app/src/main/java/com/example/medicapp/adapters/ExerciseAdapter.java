package com.example.medicapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medicapp.model.ExerciseModel;
import com.example.medicapp.IOnLoadMore;
import com.example.medicapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder>{

    private Context mContext;
    private List<ExerciseModel> mData;
    private IOnLoadMore loadMore;
    private OnItemClicked clickedListener;

    public ExerciseAdapter(Context mContext, List<ExerciseModel> mData, IOnLoadMore loadMore) {
        this.mContext = mContext;
        this.mData = mData;
        this.loadMore = loadMore;
    }

    public void setClickedListener(OnItemClicked clickedListener) {
        this.clickedListener = clickedListener;
    }

    public void addData(List<ExerciseModel> models){
        mData.addAll(models);
        notifyItemInserted(mData.size());
    }

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_exercise, viewGroup,false);
        return new ExerciseHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseHolder exerciseHolder, int i) {
        exerciseHolder.onBind(mData.get(i));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        CardView cardView;
        TextView textView;
        TextView header;
        ExerciseModel exerciseModel;

        ExerciseHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_video);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
            textView = itemView.findViewById(R.id.exercise_desc);
            header = itemView.findViewById(R.id.categoryExercise);
        }


        void onBind(ExerciseModel model){
            exerciseModel = model;
            if (model.getType() == ExerciseModel.TYPE_HEADER) {
                cardView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
                header.setText(model.getCategory());
                return;
            }else{
                cardView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                header.setVisibility(View.GONE);
            }
            Picasso.with(mContext)
                    .load(model.getUrlImage())
                    .error(R.drawable.ic_close_gray)
                    .into(imageView);
            textView.setText(model.getName());

        }

        @Override
        public void onClick(View v) {
            if (clickedListener != null && exerciseModel.getType() != ExerciseModel.TYPE_HEADER) {
                clickedListener.onVideoClicked(exerciseModel);
            }
        }
    }

    public interface OnItemClicked{
        void onVideoClicked(ExerciseModel model);
    }
}
