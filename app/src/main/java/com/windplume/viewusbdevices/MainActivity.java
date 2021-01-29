package com.windplume.viewusbdevices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.windplume.viewusbdevices.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private static final String DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";

    BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (device == null) return;
            String productName = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                productName = device.getProductName();
            }
            if (Objects.equals(intent.getAction(), ATTACHED)) {
                Toast.makeText(context, String.format(Locale.CHINA, "检测到新的USB设备: %s/%d, %d", productName, device.getVendorId(), device.getProductId()), Toast.LENGTH_SHORT).show();
                adapter.addUsbDevice(device);
            } else if (Objects.equals(intent.getAction(), DETACHED)) {
                Toast.makeText(context, String.format(Locale.CHINA, "检测到USB设备断开: %s/%d, %d", productName, device.getVendorId(), device.getProductId()), Toast.LENGTH_SHORT).show();
                adapter.removeUsbDevice(device);
            }
        }
    };
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        UsbDevicesViewModel usbDevicesViewModel = new ViewModelProvider(this).get(UsbDevicesViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setUsbDevices(usbDevicesViewModel);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new MyAdapter(getUsbDevices());
        binding.recyclerView.setAdapter(adapter);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ATTACHED);
        filter.addAction(DETACHED);
        registerReceiver(usbReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(usbReceiver);
    }

    public List<UsbDevice> getUsbDevices() {
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        if (usbManager == null) {
            throw new NullPointerException();
        }
        return new ArrayList<>(usbManager.getDeviceList().values());
    }
}
