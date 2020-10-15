package com.windplume.viewusbdevices;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class UsbDevicesViewModel extends AndroidViewModel {
    MutableLiveData<String> msg = new MutableLiveData<>();
    public MutableLiveData<Integer> count = new MutableLiveData<>();

    public UsbDevicesViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getMsg() {
        return msg;
    }

    public void setMsg(MutableLiveData<String> msg) {
        this.msg = msg;
    }
}
