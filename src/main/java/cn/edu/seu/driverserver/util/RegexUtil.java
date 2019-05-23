package cn.edu.seu.driverserver.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义的正则表达式校验
 */
public class RegexUtil {


    /**
     * 验证服务器的地址是否正确
     * @param url
     * @return
     */
    public static boolean verifyServerUrl(String url) {
        String regexEx = "[a-zA-Z]+://[^\\s]*";
        Pattern pattern = Pattern.compile(regexEx);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
}
