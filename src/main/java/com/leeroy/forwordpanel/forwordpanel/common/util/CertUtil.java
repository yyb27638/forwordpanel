package com.leeroy.forwordpanel.forwordpanel.common.util;


import com.leeroy.forwordpanel.forwordpanel.common.annotation.NoAuth;
import com.leeroy.forwordpanel.forwordpanel.common.annotation.NoIp;
import com.leeroy.forwordpanel.forwordpanel.common.annotation.NoLogin;
import org.springframework.web.method.HandlerMethod;

public class CertUtil {

	public static boolean checkNoLogin(Object handler){
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		boolean result=false;
		HandlerMethod handlerMethod = (HandlerMethod) handler;
        //String methodName = handlerMethod.getMethod().getName();
        // 判断是否需要检查登录(方法)
        NoLogin noLogin = handlerMethod.getMethod().getAnnotation(NoLogin.class);
        if (null != noLogin) {
        	result= true;
        }
        // 判断是否需要检查登录(类)
        noLogin = handlerMethod.getMethod().getDeclaringClass().getAnnotation(NoLogin.class);
        if (null != noLogin) {
        	result= true;
        }
        return result;
	}

	public static boolean checkNoAuth(Object handler){
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		boolean result=false;
		HandlerMethod handlerMethod = (HandlerMethod) handler;
        //String methodName = handlerMethod.getMethod().getName();
        // 判断是否需要检查权限(方法)
 		NoAuth noAuth = handlerMethod.getMethod().getAnnotation(NoAuth.class);
        if (null != noAuth) {
        	result= true;
        }
        // 判断是否需要检查权限(类)
        noAuth = handlerMethod.getMethod().getDeclaringClass().getAnnotation(NoAuth.class);
        if (null != noAuth) {
        	result= true;
        }
        return result;
	}

	public static boolean checkNoIp(Object handler){
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		boolean result=false;
		HandlerMethod handlerMethod = (HandlerMethod) handler;
        String methodName = handlerMethod.getMethod().getName();
        // 判断是否需要检查IP(方法)
		NoIp noIp = handlerMethod.getMethod().getAnnotation(NoIp.class);
        if (null != noIp) {
        	result= true;
        }
        // 判断是否需要检查IP(类)
        noIp = handlerMethod.getMethod().getDeclaringClass().getAnnotation(NoIp.class);
        if (null != noIp) {
        	result= true;
        }
        return result;
	}

	public static boolean checkIpInRange(String ip, String cidr) {
		boolean result=false;
		if(cidr.contains("/")){
			String[] ips = ip.split("\\.");
	        int ipAddr = (Integer.parseInt(ips[0]) << 24)
	                | (Integer.parseInt(ips[1]) << 16)
	                | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
	        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
	        int mask = 0xFFFFFFFF << (32 - type);
	        String cidrIp = cidr.replaceAll("/.*", "");
	        String[] cidrIps = cidrIp.split("\\.");
	        int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
	                | (Integer.parseInt(cidrIps[1]) << 16)
	                | (Integer.parseInt(cidrIps[2]) << 8)
	                | Integer.parseInt(cidrIps[3]);

	        result= (ipAddr & mask) == (cidrIpAddr & mask);
		}else{
   			 result=ip.equals(cidr);
		}
		return result;

    }
    public static void main(String[] args) {
        System.out.println(checkIpInRange("192.168.1.128", "192.168.1.64/26"));
        System.out.println(checkIpInRange("192.168.1.2", "192.168.0.0/23"));
        System.out.println(checkIpInRange("192.168.0.1", "192.168.0.0/24"));
        System.out.println(checkIpInRange("192.168.0.0", "192.168.0.0/32"));
    }
}
