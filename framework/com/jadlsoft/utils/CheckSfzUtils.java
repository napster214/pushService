/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jadlsoft.utils;

import com.jadlsoft.utils.DateUtils;

/**
 * 身份证验证类。
 * 
 * @author zhaohuibin
 * 
 */
public class CheckSfzUtils {

    /**
     * 检查校验位
     * @param certiCode
     * @return
     */
    private static boolean checkIDParityBit(String certiCode) {
        boolean flag = false;
        if (certiCode == null || "".equals(certiCode)) {
            return false;
        }
        int ai[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
        if (certiCode.length() == 18) {
            int i = 0;
            for (int k = 0; k < 18; k++) {
                char c = certiCode.charAt(k);
                int j;
                if (c == 'X') {
                    j = 10;
                } else if (c <= '9' && c >= '0') {
                    j = c - 48;
                } else {
                    return flag;
                }
                i += j * ai[k];
            }

            if (i % 11 == 1) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 检查日期格式
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
    private static boolean checkDate(String year, String month, String day) {
        try {
            String s3 = year + month + day;
            Integer.parseInt(s3);
            
            if(Integer.parseInt(year) < 1900 || Integer.parseInt(year) >  (Integer.parseInt(DateUtils.getCurrentData().substring(0,4))) - 16){
                return false;
            }
            
            if(Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12){
                return false;
            }
            
            if(Integer.parseInt(month) == 2 && Integer.parseInt(year) % 4 == 0){
                if(Integer.parseInt(day) < 1 || Integer.parseInt(month) > 29){
                    return false;
                }
            }
            
            if(Integer.parseInt(month) == 2 && Integer.parseInt(year) % 4 != 0){
                if(Integer.parseInt(day) < 1 || Integer.parseInt(month) > 28){
                    return false;
                }
            }
            
            if(Integer.parseInt(month) == 1 || Integer.parseInt(month) == 3 || Integer.parseInt(month) == 5 || Integer.parseInt(month) == 7 || Integer.parseInt(month) == 8 || Integer.parseInt(month) == 10 || Integer.parseInt(month) == 12){
                if(Integer.parseInt(day) < 1 || Integer.parseInt(month) > 31){
                    return false;
                }
            }
            
            if(Integer.parseInt(month) == 4 || Integer.parseInt(month) == 6 || Integer.parseInt(month) == 9 || Integer.parseInt(month) == 11){
                if(Integer.parseInt(day) < 1 || Integer.parseInt(month) > 31){
                    return false;
                }
            }
        } catch (NumberFormatException nex) {
            return false;
        }
        
        return true;
    }

    /**
     * 校验身份证
     * 
     * @param certiCode
     * 待校验身份证
     * @return 0--校验成功; 1--位数不对; 2--生日格式不对 ; 3--校验位不对 ; 4--其他异常;5--字符异常;
     * @param certiCode
     * @return
     */
    private static int checkCertiCode(String certiCode) {
        try {
            if (certiCode == null || certiCode.length() != 15
                    && certiCode.length() != 18) {
                return 1;
            }
            String s1;
            String s2;
            String s3;

            if (certiCode.length() == 15) {

                if (!checkFigure(certiCode)) {
                    return 5;
                }

                s1 = "19" + certiCode.substring(6, 8);
                s2 = certiCode.substring(8, 10);
                s3 = certiCode.substring(10, 12);

                if (!checkDate(s1, s2, s3)) {
                    return 2;
                }
            }

            if (certiCode.length() == 18) {
                if (!checkFigure(certiCode.substring(0, 17))) {
                    return 5;
                }

                s1 = certiCode.substring(6, 10);
                s2 = certiCode.substring(10, 12);
                s3 = certiCode.substring(12, 14);

                if (!checkDate(s1, s2, s3)) {
                    return 2;
                }
                if (!checkIDParityBit(certiCode)) {
                    return 3;
                }
            }
        } catch (Exception exception) {

            return 4;
        }
        return 0;
    }

    /**
     * 判断身份证号码是否正确
     * @param idCard
     * @return
     */
    public static boolean isIdCard(String idCard) {
        if (checkCertiCode(idCard) == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 判断身份证号码哪里不正确
     * @param idCard
     * @return
     */
    public static String idCardErrorCode(String idCard) {
    	//0--校验成功; 1--位数不对; 2--生日格式不对 ; 3--校验位不对 ; 4--其他异常;5--字符异常;
    	if (checkCertiCode(idCard) == 1) {
    		return "长度不正确";
    	}
    	if (checkCertiCode(idCard) == 2) {
    		return "校验位不正确";
    	}
    	if (checkCertiCode(idCard) == 3) {
    		return "校验位不正确";
    	}
    	if (checkCertiCode(idCard) == 4) {
    		return "其他异常";
    	}
    	if (checkCertiCode(idCard) == 5) {
    		return "字符异常";
    	}
    	
    	return "";
    }

    /**
     * 检查字符串是否全为数字
     * @param certiCode
     * @return
     */
    private static boolean checkFigure(String certiCode) {
        try {
            Long.parseLong(certiCode);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String cetitype = "110101198506020089";
        System.out.println(isIdCard(cetitype));
    }
}
