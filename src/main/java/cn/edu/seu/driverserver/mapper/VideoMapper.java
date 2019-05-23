package cn.edu.seu.driverserver.mapper;

import cn.edu.seu.driverserver.domin.Video;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideoMapper {

    @Insert("INSERT INTO t_videos(deviceId, videoUrl, type, createTime) values(#{deviceId}, #{videoUrl}, #{type}, #{createTime})")
    public Integer addVideoRecord(Video video);

    @Select("SELECT * FROM t_videos where deviceId = #{deviceId} and type = #{type}")
    public List<Video> queryAllVideoByDeviceIdAndType(@Param("deviceId") int deviceId, @Param("type") int type);

    @Select("SELECT * FROM t_videos where deviceId = #{deviceId} and type = #{type} limit #{row}, #{limit}")
    public List<Video> queryVideoByDeviceIdAndTypeAndNumber(@Param("deviceId") int deviceId, @Param("type") int type,  @Param("row") int row, @Param("limit") int limit);

    @Select("SELECT count(1) from t_videos where deviceID = #{deviceId} and type =#{type}")
    public Integer queryVideoCountByDeviceIdAndType(@Param("deviceId") int deviceId, @Param("type") int type);
}
