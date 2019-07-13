package com.example.medicapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.medicapp.Constants;
import com.example.medicapp.R;
import com.example.medicapp.model.AdviceModel;
import com.example.medicapp.networking.response.advice.Advice;
import com.example.medicapp.ui.PlayerActivity;
import com.example.medicapp.ui.ResultViewActivity;


public class AdviceChildAdapter extends RecyclerView.Adapter<AdviceChildAdapter.ViewHolder> {
    private Advice mData;
    private Context mContext;
    private int mode;

    public AdviceChildAdapter(Advice mData, Context mContext, int mode) {
        this.mData = mData;
        this.mContext = mContext;
        this.mode = mode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_advice_child, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        switch (mode){
            case AdviceModel.MODE_IMAGES:
                viewHolder.bind(mData.getImages().get(i), "");
                break;
            case AdviceModel.MODE_VIDEO:
                viewHolder.bind(mData.getVideos().get(i).getPreview(), mData.getVideos().get(i).getVideo());
                break;
        }

    }


    @Override
    public int getItemCount() {
        if (mode == AdviceModel.MODE_IMAGES) {
            return mData.getImages().size();
        }
        return mData.getVideos().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private String imageUrl;
        private String videoUrl;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.imageView3);
            imageView.setOnClickListener(this::onClick);
        }

        private void onClick(View view) {
            if (mode == AdviceModel.MODE_IMAGES) {
                Intent i = new Intent(mContext, ResultViewActivity.class);
                i.putExtra(ResultViewActivity.IMAGE_PARAM, Constants.BASE_URL_IMAGE + imageUrl);
                mContext.startActivity(i);
            }
            else {

                Intent i = PlayerActivity.getVideoPlayerIntent(
                        mContext,
                        Constants.BASE_URL_IMAGE + videoUrl,
                        "", R.drawable.ic_video_play)
                        ;
                mContext.startActivity(i);
            }
        }

        public void bind(String image, String video) {
            imageUrl = image;
            this.videoUrl = video;
            Glide.with(mContext)
                    .load(Constants.BASE_URL_IMAGE + imageUrl)
                    .into(imageView);

        }
    }
}
