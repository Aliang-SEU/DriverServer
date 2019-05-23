package cn.edu.seu.driverserver.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IndexService {

    /**
     * 文件存储状态统计
     * @param data
     * @return
     */
    public Map<String, Object> getFileStat(Object data);

}
