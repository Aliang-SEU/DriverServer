package cn.edu.seu.driverserver.controller;

import cn.edu.seu.driverserver.common.GoFastDfsApi;
import cn.edu.seu.driverserver.domin.DeviceInfo;
import cn.edu.seu.driverserver.domin.GoFastDfsUploadResult;
import cn.edu.seu.driverserver.domin.Video;
import cn.edu.seu.driverserver.mapper.DeviceMapper;
import cn.edu.seu.driverserver.mapper.VideoMapper;
import cn.edu.seu.driverserver.service.VideoFileService;
import cn.edu.seu.driverserver.domin.Response;
import cn.edu.seu.driverserver.util.DateUtil;
import cn.edu.seu.driverserver.util.UploadUtils;
import cn.hutool.core.date.DateUnit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class FileController extends BaseController{

    private static final Logger logger = Logger.getLogger(FileController.class);

    @Autowired
    private VideoFileService videoFileService;

    @Autowired
    DeviceMapper deviceMapper;

    @Autowired
    VideoMapper videoMapper;

    @Value("${upload.tempPath}")
    private String tempPath;

    @RequestMapping("/uploadToLocal")
    @ResponseBody
    public Response fileUploadToLocal(HttpServletRequest request) {
        Response response = new Response();

        //首先判断是否为Multipart类型的请求
        if (request instanceof StandardMultipartHttpServletRequest) {
            StandardMultipartHttpServletRequest httpServletRequest = (StandardMultipartHttpServletRequest) request;
            MultipartFile file = httpServletRequest.getFile("file");
            String folder = httpServletRequest.getParameter("folder");

            if (file != null && !file.isEmpty()) {
                if (videoFileService.saveFile(file, folder)) {
                    response.setRespCode(Response.RESPONSE_CODE_SUCCESS);
                    response.setRespMsg("文件上传成功!");
                } else {
                    response.setRespCode(Response.RESPONSE_CODE_FAILURE);
                    response.setRespMsg("内部错误，文件上传失败!");
                }
            } else {
                response.setRespCode(Response.RESPONSE_CODE_FAILURE);
                response.setRespMsg("找不到上传的文件!");
            }
        } else {
            response.setRespCode(Response.RESPONSE_CODE_ERROR);
            response.setRespMsg("请求格式错误!");
        }
        return response;
    }

    @RequestMapping("/uploadToServer")
    @ResponseBody
    public Response fileUploadToServer(HttpServletRequest request) {
        Response response = null;
        //首先判断是否为Multipart类型的请求
        if (request instanceof StandardMultipartHttpServletRequest) {
            StandardMultipartHttpServletRequest httpServletRequest = (StandardMultipartHttpServletRequest) request;
            //获取参数文件
            MultipartFile file = httpServletRequest.getFile("file");
            String scene = httpServletRequest.getParameter("scene");
            Integer deviceId = Integer.parseInt(httpServletRequest.getParameter("deviceId"));
            Integer type = Integer.parseInt(httpServletRequest.getParameter("type"));

            if (file != null && !file.isEmpty()) {
                response = UploadUtils.upload(tempPath, getPeersUrl()+ GoFastDfsApi.UPLOAD, "", scene, file, getPeersUrl());
                //如果文件上传成功
                if(response.getRespCode() == Response.RESPONSE_CODE_SUCCESS) {
                    if(deviceMapper.queryByDeviceId(deviceId) == 0){
                        DeviceInfo deviceInfo = new DeviceInfo();
                        deviceInfo.setDeviceId(deviceId);
                        deviceInfo.setDeviceName("设备" + deviceId);
                        deviceMapper.addDevice(deviceInfo);
                    }

                    GoFastDfsUploadResult goFastDfsUploadResult = (GoFastDfsUploadResult)response.getData();
                    Video video = new Video();
                    video.setDeviceId(deviceId);
                    video.setType(type);
                    video.setVideoUrl(goFastDfsUploadResult.getUrl());
                    String date = DateUtil.getFormatDate(new Date());
                    video.setCreateTime(date);
                    videoMapper.addVideoRecord(video);
                    response.setRespMsg("文件上传成功");
                    return response;
                }else {
                    return response;
                }
            } else {
                response.setRespCode(Response.RESPONSE_CODE_FAILURE);
                response.setRespMsg("找不到上传的文件!");
                return response;
            }
        } else {
            response = new Response(Response.RESPONSE_CODE_ERROR, "请求格式错误!");
            return response;
        }
    }


    @RequestMapping("/download")
    @ResponseBody
    public Response fileDownload(HttpServletResponse httpServletResponse, String folder, String fileName) {

        Response response = new Response<>();
        videoFileService.downLoadFile(httpServletResponse, folder, fileName, "");

        response.setRespCode(Response.RESPONSE_CODE_SUCCESS);
        response.setRespMsg("下载成功!");
        return response;
    }


}
