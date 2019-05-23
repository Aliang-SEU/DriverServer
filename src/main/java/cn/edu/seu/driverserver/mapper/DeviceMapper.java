package cn.edu.seu.driverserver.mapper;


import cn.edu.seu.driverserver.domin.DeviceInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DeviceMapper {

    @Select("select * from t_device")
    List<DeviceInfo> getAllDevice();

    @Insert("insert into t_device(deviceId, deviceName) values(#{deviceId}, #{deviceName})")
    Integer addDevice(DeviceInfo deviceInfo);

    @Select("select count(1) from t_device where deviceId = #{deviceId}")
    Integer queryByDeviceId(Integer deviceId);
}
