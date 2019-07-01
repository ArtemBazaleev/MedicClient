package com.example.medicapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.medicapp.R;
import com.example.medicapp.model.ReservationModel;

import java.util.ArrayList;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolderReservation> {
    private Context mContext;
    private ArrayList<ReservationModel> mData;

    public ReservationAdapter(Context mContext, ArrayList<ReservationModel> mData) {
        Log.d("ReservationAdapter: ","OK");
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolderReservation onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_reservation, viewGroup,false);
        Log.d("onCreateViewHolder: ","OK");
        return new ViewHolderReservation(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderReservation viewHolder, int i) {
        Log.d("onBindViewHolder: ","OK");
        viewHolder.bind(mData.get(i));
    }

    @Override
    public int getItemCount() {
        Log.d("getItemCount: ","OK");
        return mData.size();
    }

    public class ViewHolderReservation extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView time;
        private ReservationModel model;

        ViewHolderReservation(@NonNull View itemView) {
            super(itemView);
            Log.d("ViewHolderReservation: ","OK");
            date = itemView.findViewById(R.id.reservation_date);
            time = itemView.findViewById(R.id.reservation_time);
        }

        public void bind(ReservationModel reservationModel) {
            Log.d("ViewHolderReservation: ","bindOK");
            model = reservationModel;
            date.setText(model.getDate());
            time.setText(model.getTime());
        }
    }
}
