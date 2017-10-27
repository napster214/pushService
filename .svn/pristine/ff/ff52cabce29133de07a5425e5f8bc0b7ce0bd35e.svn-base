package com.jadlsoft.utils.icutil;

import java.util.Random;

/**
 * 服务器对传入OCX控件的参数的加密处理过程
 * 
 * @author 张方俊
 * 
 */
public class DealOCXParameter {

	private static Random random = new Random();

	/**
	 * 产生随机字符串的表。请不要改变。
	 */
	private static final char[] RANDOM_TABLE = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
			'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z' };

	/**
	 * 随机填充6－15个字符，其中第6位为长度
	 * 
	 * @param input :
	 *            已经组合好了的参数。例如：@0=F0004@1=123456@2=abc@3=PC
	 * 
	 * @return : 返回的是经过BASE64编码过后的字符串
	 */
	public static String dealParameter(String input) {
		if (input == null || input.equals(""))
			return "";		
		// 将参数长度加到参数头		
		String temp_str = "" + input.getBytes().length;
		input = "00000000".substring(temp_str.length()) + temp_str + input;
		// CRC校验
		String crc = Crc.getCrc(input);
		// 产生随机长度(6-15位)的字符串，并将混淆字符串加到参数头
		int randomlen = generateRandomInt(6, 15);
		String randomstring = generateRandomString(randomlen);
		char len = RANDOM_TABLE[java.lang.Math.abs(randomlen % 16)];
		String pre = "JADL" + randomstring.substring(0, 5) + len + randomstring.substring(6);
		
		input = Base64.encode(TripleDES.encode(pre,crc,input));
		
		temp_str = "" + input.length();
		input = "00000000".substring(temp_str.length()) + temp_str + input;
		
		return input;
	}

	/**
	 * 随机产生min至max之间的一个随机数
	 */
	protected static int generateRandomInt(int min, int max) {
		return random.nextInt(max - min) + min;
	}

	/**
	 * 随机产生一个len长度的字符串
	 */
	protected static String generateRandomString(int len) {
		if (len == 0)
			return "";
		byte[] randomBytes = new byte[len];
		random.nextBytes(randomBytes);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			sb.append(RANDOM_TABLE[java.lang.Math.abs(randomBytes[i] % 62)]);
		}		
				
		return sb.toString();
	}
	/*
	 * 测试程序
	 */
	public static void main(String[] args) {
		//String gmz = "@0=F0014@1=1@2=1@3=1@4=kz34567890123@5=gh23123456789@6=gm31234567890@7=wl@8=gmzbh1234567890@9=20060828@10=20080828@11=lx1@12=100@13=lx2@14=200@15=lx3@16=300@17=lx4@18=400@19=lx5@20=500@21=0";
		String gmz = "@0=F0003@1=方俊@2=北京@3=20081010";
		//gmz = "@0=F0003@1=fangjun@2=beijing@3=20081010";
		//gmz = "@00=F0011";
		//gmz = "@0=F0001";
		//gmz = "@00=F0014@01=1@02=0@03=0@04=1300000000000@05=1301072300001@06=1101012310000@07=北京公安局@08=123456206090015@09=20060913@10=20080913@11=0@12=1111@13=0@14=0@15=0@16=0@17=0@18=0@19=0@20=0@21=0";
		//System.out.println("1 = " + TripleDES.encode(gmz));	
		gmz = "@00=F0015@01=2@02=1@03=0@04=0@05=123456@06=123456@07=123456@08=123@09=123456106100003@10=20080828@11=20080828@12=1@13=1@14=1234567890123@15=1@16=1234567890123@17=400@18=0@19=0@20=0@21=0@22=0@23=0@24=0@25=0@26=0@27=0";
		gmz = dealParameter(gmz);
		
		System.out.println("edncode = " + gmz);
		
		//decode
		gmz = gmz.substring(8);
		System.out.println("decode1 = " + TripleDES.decode(Base64.decode(gmz)));
		//gmz = "I5bs1pwblH5bwPDJ2z/7mK7sOH87eHnxwCY8a5aX6mfHFvEpQyRq4Y4NupW1TsZ+HLFaesC7zwLuzGe60ejUpw==";
		//gmz = TripleDES.decode(Base64.decode(gmz));
		//System.out.println("decode2 = " + gmz);
		
		/*
		char[] gmz_l = gmz.toCharArray();
		for(int i=0;i<gmz_l.length;i++){
			System.out.println("["+i+"] = "+gmz_l[i]);
			System.out.println("int = "+(int)gmz_l[i]);
		}
		
		
		/*
		String input = "980";
		String temp = "" + input.length();
		input = "00000000".substring(temp.length()) + temp + input;
		System.out.println("input = " + input);

		int rlen = generateRandomInt(6, 15);
		String rstring = generateRandomString(rlen);
		System.out.println("rlen = " + rlen);
		char len = RANDOM_TABLE[java.lang.Math.abs(rlen % 16)];
		System.out.println("rstring = " + rstring);
		System.out.println("random string = "
				+ (rstring.substring(0, 5) + len + rstring.substring(5)));

		// 测试dealParameter，输入参数为@0=F0001
		input = "@0=F0006";
		String encode = dealParameter(input);
		System.out.println("deal = " + encode);
		// 解密
		encode = encode.substring(8);
		String decode = TripleDES.decode(Base64.decode(encode));
		System.out.println("decode = " + decode);
		*/
	}
}
