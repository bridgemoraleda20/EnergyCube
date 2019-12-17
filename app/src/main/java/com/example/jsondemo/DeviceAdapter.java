package com.example.jsondemo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.graphics.Color.GRAY;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private static ArrayList<DeviceModel> mDeviceList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class DeviceViewHolder extends  RecyclerView.ViewHolder {

        public TextView mTextView1;
        public TextView mTextView2;

        public DeviceViewHolder(@NonNull View itemView, final OnItemClickListener listener) {

            super(itemView);
            mTextView1 = itemView.findViewById(R.id.deviceName);
            mTextView2 = itemView.findViewById(R.id.deviceCurrent);

            //itemView.setBackgroundColor(Color.parseColor("#414141"));
            //itemView.setBackgroundColor(Color.parseColor("#414141"));


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }


                }
            });
        }
    }

    public DeviceAdapter(ArrayList<DeviceModel> deviceList) {
        mDeviceList = deviceList;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_item, viewGroup, false);
        DeviceViewHolder dvh = new DeviceViewHolder(v, mListener);
        return dvh;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder deviceViewHolder, int i) {
        DeviceModel currentDevice = mDeviceList.get(i);

        deviceViewHolder.mTextView1.setText(currentDevice.getName());
        deviceViewHolder.mTextView2.setText("0 kw/h");

        if(currentDevice.isActive())
            deviceViewHolder.itemView.setBackgroundColor(Color.parseColor("#3FBA4B"));
        else
            deviceViewHolder.itemView.setBackgroundColor(Color.parseColor("#414141"));

    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }
}
