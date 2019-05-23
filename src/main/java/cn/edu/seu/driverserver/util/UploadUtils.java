package cn.edu.seu.driverserver.util;

import cn.edu.seu.driverserver.domin.GoFastDfsUploadResult;
import cn.edu.seu.driverserver.domin.Response;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传工具类
 * @author Perfree
 *
 */
public class UploadUtils {

    /**
     * 文件上传
     * @param url 服务器地址
     * @param path 路径
     * @param scene 场景
     * @param multipartFile 文件
     * @param showUrl 回显url
     * @return AjaxResult
     */
    public static Response upload(String tempPath, String url, String path, String scene, MultipartFile multipartFile, String showUrl) {
        Response response = null;
        File parentFile = new File(tempPath);
        if(parentFile.exists()){
            if (!parentFile.isDirectory()) {
                return new Response(Response.RESPONSE_CODE_ERROR, "临时目录不是一个文件夹");
            }
        }else{
            parentFile.mkdir();
        }
        File file = UploadUtils.getFile(multipartFile, tempPath);
        //File file = convertFileToMp4(oldfile, tempPath);
        Map<String, Object> map = new HashMap<>(16);
        map.put("output", "json");
        map.put("path", path);
        map.put("scene", scene);
        map.put("file", file);
        try {
            String post = HttpUtil.post(url,map);
            GoFastDfsUploadResult goFastDfsResult = JSONUtil.toBean(post, GoFastDfsUploadResult.class);
            //替换url
            goFastDfsResult.setUrl(showUrl+goFastDfsResult.getPath());
            response = new Response(Response.RESPONSE_CODE_SUCCESS, goFastDfsResult);
        } catch (Exception e) {
            response = new Response(Response.RESPONSE_CODE_ERROR, "上传出错");
        } finally {
            file.delete();
        }
        return response;
    }

    /**
     * multipartFile转file
     * @param multipartFile
     * @return File
     */
    private static File getFile(MultipartFile multipartFile, String tempPath) {
        String fileName = multipartFile.getOriginalFilename();
        String filePath = tempPath;
        File file = new File(filePath + fileName);
        try {
            multipartFile.transferTo(file.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 视频格式转换
     * @param file
     * @param tempPath
     * @return
     */
    private static File convertFileToMp4(File file, String tempPath) {
        String fileName = file.getName();
        if(fileName.endsWith(".mp4")) {
            return file;
        }else{
            String newFileName = tempPath + fileName.substring(0, fileName.lastIndexOf('.')) + ".mp4";
            File convertedFile = new File(newFileName);
            try {
                FFMpegUtil.convetor(file.getAbsolutePath(), newFileName);
            }catch (Exception e) {
                e.printStackTrace();
            }
            return convertedFile;
        }
    }
}
