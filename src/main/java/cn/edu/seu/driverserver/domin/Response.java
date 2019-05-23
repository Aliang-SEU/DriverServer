package cn.edu.seu.driverserver.domin;

import lombok.Data;

import java.util.List;

/**
 * Http响应
 * @param <T>
 */
public class Response<T> {

    public static int RESPONSE_CODE_SUCCESS = 200;
    public static int RESPONSE_CODE_FAILURE = 300;
    public static int RESPONSE_CODE_ERROR = 400;

    private int respCode;

    private String respMsg;

    private T data;

    private List<T> listData;

    public Response() {
    }

    public Response(int respCode) {
        this.respCode = respCode;
    }

    public Response(int respCode, String respMsg) {
        this.respCode = respCode;
        this.respMsg = respMsg;
    }

    public Response(int respCode, T data) {
        this.respCode = respCode;
        this.data = data;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getListData() {
        return listData;
    }

    public void setListData(List<T> listData) {
        this.listData = listData;
    }

    @Override
    public String toString() {
        return "Response{" +
                "respCode=" + respCode +
                ", respMsg='" + respMsg + '\'' +
                ", data=" + data +
                ", listData=" + listData +
                '}';
    }
}
