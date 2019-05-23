package cn.edu.seu.driverserver.service.Impl;

import cn.edu.seu.driverserver.service.DeviceService;
import cn.edu.seu.driverserver.domin.DeviceInfo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeviceServiceImpl implements DeviceService {

    //用于保存已有的设备信息
    private Map<Integer, DeviceInfo> deviceMap = new TreeMap<>();

    /**
     * 注册设备信息
     *
     * @return
     */
    public boolean registerDevice(int deviceId, String deviceName) {

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceId(deviceId);
        deviceInfo.setDeviceName(deviceName);

        if (deviceMap.containsKey(deviceId)) {
            deviceMap.put(deviceId, deviceInfo);
            return true;
        } else {
            deviceMap.put(deviceId, deviceInfo);
            return true;
        }
    }

    /**
     * 返回所有的设备信息
     * @return
     */
    public List<DeviceInfo> getDeviceInfo() {
        List<DeviceInfo> result = new ArrayList<>();

        for(Map.Entry<Integer, DeviceInfo> device : deviceMap.entrySet()){
            result.add(device.getValue());
        }

        return result;
    }

}
