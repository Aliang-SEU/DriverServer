package cn.edu.seu.driverserver.service.Impl;

import cn.edu.seu.driverserver.service.VideoFileService;
import cn.edu.seu.driverserver.domin.DeviceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * 文件存储服务
 */

@PropertySource(value={"classpath:fileServiceConfig.properties"})
@Service
public class VideoFileServiceImpl implements VideoFileService {

    private ServletContext servletContext;

    private DeviceServiceImpl deviceService;

    @Autowired
    public VideoFileServiceImpl(ServletContext servletContext, DeviceServiceImpl deviceService) {
        this.servletContext = servletContext;
        this.deviceService = deviceService;
    }

    @Value(value="${VIDEO_FILE_SAVE_PATH}")
    private String VIDEO_FILE_SAVE_PATH;

    public VideoFileServiceImpl(){


    }

    /**
     * 保存文件到本地路径下
     * @param file
     * @param folder
     * @return
     */
    public boolean saveFile(MultipartFile file, String folder) {
        //check file
        if (!file.isEmpty()) {
            try {
                String path = VIDEO_FILE_SAVE_PATH + File.separator + folder;
                File folderFile = new File(path);

                //检查文件夹是否存在
                if (!folderFile.exists()) {
                    boolean dirFlag = folderFile.mkdirs();
                    if(!dirFlag)
                        return false;
                }
                String filePath = path + File.separator + file.getOriginalFilename();
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(filePath));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 下载视频文件
     * @param resp
     * @param folder
     * @param fileName
     * @param downloadName
     */
    public void downLoadFile(HttpServletResponse resp, String folder, String fileName, String downloadName){
        String downLoadFileName = null;
        try{
            downLoadFileName = new String(downloadName.getBytes("GBK"), "ISO-8859-1");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String realPath = VIDEO_FILE_SAVE_PATH + File.separator + folder;
        String realFilePath = realPath + File.separator + fileName;
        File file = new File(realFilePath);
        resp.reset();
        //设置文件类型为avi格式的视频文件
        resp.setContentType("video/avi");
        resp.setCharacterEncoding("utf-8");
        resp.setContentLength((int) file.length());
        resp.setHeader("Content-Disposition", "attachment;filename=" + downLoadFileName);

        byte[] buff = new byte[1024];

        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = resp.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<DeviceInfo> getDeviceInfoList(){
        List<DeviceInfo> result = deviceService.getDeviceInfo();
        return result;
    }
}
