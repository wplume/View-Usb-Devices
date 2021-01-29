package com.windplume.viewusbdevices;

import android.app.Application;
import android.hardware.usb.UsbDevice;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UsbDevicesViewModel extends AndroidViewModel {

    private MutableLiveData<List<UsbDevice>> usbDevices = new MutableLiveData<>();

    public UsbDevicesViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<UsbDevice>> getUsbDevices() {
        if (usbDevices.getValue() == null) {
            usbDevices.setValue(new ArrayList<>());
        }
        return usbDevices;
    }
}
