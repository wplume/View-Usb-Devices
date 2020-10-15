package com.windplume.viewusbdevices;

import android.hardware.usb.UsbDevice;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.windplume.viewusbdevices.databinding.RecycleviewItemBinding;

import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final List<UsbDevice> usbDevices;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final com.windplume.viewusbdevices.databinding.RecycleviewItemBinding binding;

        public MyViewHolder(@NonNull RecycleviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public MyAdapter(List<UsbDevice> usbDevices) {
        this.usbDevices = usbDevices;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecycleviewItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.recycleview_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setUsbDevice(usbDevices.get(position));
    }

    @Override
    public int getItemCount() {
        return usbDevices.size();
    }

    public void addUsbDevice(UsbDevice device) {
        usbDevices.add(device);
        notifyDataSetChanged();
    }

    public void removeUsbDevice(UsbDevice device) {
        usbDevices.remove(device);
        notifyDataSetChanged();
    }
}
