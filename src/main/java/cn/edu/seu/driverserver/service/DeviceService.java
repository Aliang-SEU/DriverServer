package cn.edu.seu.driverserver.service;

import cn.edu.seu.driverserver.domin.DeviceInfo;

import java.util.Date;
import java.util.List;

public interface DeviceService {

    public boolean registerDevice(int deviceId, String deviceName);

    public List<DeviceInfo> getDeviceInfo();
}
