package cn.edu.seu.driverserver.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface VideoFileService {


    public boolean saveFile(MultipartFile file, String folder);

    public void downLoadFile(HttpServletResponse resp, String folder, String fileName, String downloadName);

}
