package cn.edu.seu.driverserver.controller;


import cn.edu.seu.driverserver.domin.DeviceInfo;
import cn.edu.seu.driverserver.domin.Response;
import cn.edu.seu.driverserver.domin.Video;
import cn.edu.seu.driverserver.mapper.DeviceMapper;
import cn.edu.seu.driverserver.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class DeviceController extends BaseController {

    @Autowired
    DeviceMapper deviceMapper;

    @Autowired
    VideoMapper videoMapper;

    @GetMapping("/videoPage")
    public String videoPage(Model model) {
        List<DeviceInfo> deviceInfoList = deviceMapper.getAllDevice();
        model.addAttribute("deviceList", deviceInfoList);
        return "fatigueDriving/videoPage";
    }

    @GetMapping("/getVideos/{deviceId}/{type}")
    @ResponseBody
    public Response getVideos(@PathVariable("deviceId") Integer deviceId, @PathVariable("type") Integer type,
                              @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        Response response = null;
        List<Video> result = videoMapper.queryVideoByDeviceIdAndTypeAndNumber(deviceId, type, (page - 1)* limit, limit);
        Integer count = videoMapper.queryVideoCountByDeviceIdAndType(deviceId, type);
        if (result != null && result.size() > 0) {
            response = new Response(Response.RESPONSE_CODE_SUCCESS);
            response.setData(count);
            response.setListData(result);
            return response;
        } else {
            response = new Response(Response.RESPONSE_CODE_ERROR, "没有相关的数据");
            return response;
        }
    }
}
