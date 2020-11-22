package com.spotify.api.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

@Component
public class ServletUtil {

    private final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };
    private HttpServletRequest getRequest() {
        HttpServletRequest curRequest =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        return curRequest;
    }

    public String getAuthTokenFromRequest(){
        System.out.println(getRequest().getHeader("Host"));
        return getRequest().getHeader("Authorization").substring(7);
    }

    public String getHeader(String headerName){
        return getRequest().getHeader(headerName);
    }
    public String getIpAddress(){
        for (String x: IP_HEADER_CANDIDATES){
            String ip = getRequest().getHeader(x);
            if(ip!=null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)){
                return ip;
            }
        }
        return getRequest().getRemoteAddr();
    }

    public String getQueryParam(String query){
        String _result = getRequest().getQueryString();
        return _result;
    }


}
