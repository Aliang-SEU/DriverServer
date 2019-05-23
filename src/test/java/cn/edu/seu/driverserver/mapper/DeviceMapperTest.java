package cn.edu.seu.driverserver.mapper;

import cn.edu.seu.driverserver.DriverserverApplication;
import cn.edu.seu.driverserver.domin.DeviceInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={DriverserverApplication.class})
public class DeviceMapperTest {

    @Autowired
    DeviceMapper deviceMapper;

    @Test
    public void getAllDevice() throws Exception {
        System.out.println(deviceMapper.getAllDevice());
    }


    @Test
    public void addDevice() throws Exception {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceName("123");
        deviceInfo.setDeviceId(123);
        //System.out.println(deviceMapper.addDevice(deviceInfo));

        System.out.println(deviceMapper.queryByDeviceId(0));
    }



}