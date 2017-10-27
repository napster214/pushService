package com.jadlsoft.utils.icutil;

import java.io.UnsupportedEncodingException;



/**
 * 根据OCX的CRC校验转化而来，无文档参考
 * 
 * @author 张方俊
 * 
 */
public class Crc {

	private static int[] crc_32_tab = new int[256];

	private static int ulPolynomial = 0x04c11db7;

	static {
		init_crc32_table();
	}

	private Crc() {
	}
	
	public static String getCrc(String crc_string) {
		byte[] Pointer = crc_string.getBytes();

		int crc_data = GenerateCRC32(Pointer, Pointer.length);

		char[] crc_array = new char[4];
		
		crc_array[0] = (char) (crc_data & 0x0ff);
		crc_array[1] = (char) (crc_data >> 8 & 0x0ff);
		
		crc_array[2] = (char) (crc_data >> 16 & 0x0ff);
		crc_array[3] = (char) (crc_data >> 24 & 0x0ff);		
		
		return new String(crc_array);
		
	}
	
		
	public static void main(String[] args){
		
		String test = "00000033@0=F0003@1=方俊@2=北京@3=20081010";
		
		byte[] kk1 = {-117};
		System.out.println("1 = "+Base64.encode(kk1));
		char[] kk3 = {139};
		try {
			System.out.println("2 = "+Base64.encode((new String(kk3)).getBytes("ISO8859_1")));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("3 = "+Base64.decode("iw==")[0]);
		System.out.println("4 = "+(int)Base64.decode("Pw==")[0]);
		
		//System.out.println("crc = "+getCrc("00000008@0=F0006"));
		TripleDES.encode(getCrc("00000008@0=F0006"));
		char[] kk = {139};
		String kk11 = new String(kk);
		int o = 139;
		byte p = (byte)o;
		System.out.println(" p = "+(int)p);
		try {
			byte[] k = kk11.getBytes();
			//char k1 = (char)k[0];
			System.out.println("adsf = "+(int)k[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("kk = "+(int)kk11.charAt(0));
		
	}

	public static int GenerateCRC32(byte[] DataBuf, int len) {
		int oldcrc32;
		int crc32;
		int oldcrc;
		int charcnt;
		int t;
		byte c;

		oldcrc32 = 0x00000000; // 初值为0
		charcnt = 0;
		while (len-- > 0) {
			t = (int) ((oldcrc32 >> 24) & 0xFF); // 要移出的字节的值
			oldcrc = crc_32_tab[t]; // 根据移出的字节的值查表
			c = DataBuf[charcnt]; // 新移进来的字节值
			int tp = (int)c;
			if(tp<0) tp = 256+tp;
			oldcrc32 = (oldcrc32 << 8) | tp;
			//oldcrc32 = (oldcrc32 << 8) | c; // 将新移进来的字节值添在寄存器末字节中
			oldcrc32 = oldcrc32 ^ oldcrc; // 将寄存器与查出的值进行xor运算
			charcnt++;
		}
		crc32 = oldcrc32;
		return crc32;
	}

	public static void init_crc32_table() {
		int crc, temp;
		// 256个值
		for (int i = 0; i <= 0xFF; i++) {
			temp = Reflect(i, 8);
			crc_32_tab[i] = temp << 24;
			for (int j = 0; j < 8; j++) {
				int t1, t2;
				int flag = crc_32_tab[i] & 0x80000000;
				t1 = (crc_32_tab[i] << 1);
				if (flag == 0)
					t2 = 0;
				else
					t2 = ulPolynomial;
				crc_32_tab[i] = t1 ^ t2;
			}
			crc = crc_32_tab[i];
			crc_32_tab[i] = Reflect(crc, 32);
			//System.out.println("crc_32_tab["+i+"] = "+crc_32_tab[i]);
		}
	}

	public static int Reflect(int ref, int ch) {
		int value = 0;
		// 交换bit0和bit7，bit1和bit6，类推
		for (int i = 1; i < (ch + 1); i++) {
			if ((ref & 1) != 0)
				value |= 1 << (ch - i);
			ref >>= 1;
		}
		return value;
	}
}
