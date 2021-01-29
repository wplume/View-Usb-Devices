package com.windplume.viewusbdevices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.widget.Toast;

import com.windplume.viewusbdevices.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private static final String DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    private MyAdapter adapter;
    private UsbDevicesViewModel viewModel;

    BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (device == null) return;

            configViewModel();
            String productName = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                productName = device.getProductName();
            }
            if (Objects.equals(intent.getAction(), ATTACHED)) {
                Toast.makeText(context, String.format(Locale.CHINA, "检测到新的USB设备: %s/%d, %d",
                        productName, device.getVendorId(), device.getProductId()),
                        Toast.LENGTH_SHORT).show();
            } else if (Objects.equals(intent.getAction(), DETACHED)) {
                Toast.makeText(context, String.format(Locale.CHINA, "检测到USB设备断开: %s/%d, %d",
                        productName, device.getVendorId(), device.getProductId()),
                        Toast.LENGTH_SHORT).show();
            }
            adapter.setUsbDevices(viewModel.getUsbDevices().getValue());
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(UsbDevicesViewModel.class);
        configViewModel();
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new MyAdapter(viewModel.getUsbDevices().getValue());
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

    public void configViewModel() {
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        if (usbManager == null) {
            throw new NullPointerException();
        }
        List<UsbDevice> usbDevices = new ArrayList<>(usbManager.getDeviceList().values());
        viewModel.getUsbDevices().setValue(usbDevices);
    }
}
