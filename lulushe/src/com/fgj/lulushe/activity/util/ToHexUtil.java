/**
 * @author   fengguangjing
 * @version  2015 下午3:46:08
 * @description 
 */
package com.fgj.lulushe.activity.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author EX-FENGGUANGJING001
 * utf16be 转化unicode 0x0f604 大于0x10000时 最高位加1 0x1f604
 */
public class ToHexUtil {

	/**
	 * UniToy有个“输出编码”功能，可以输出当前选择的文本编码。因为UniToy内部采用UTF-16编码，所以输出的编码就是文本的UTF
	 * -16编码。例如
	 * ：如果我们输出“汉”字的UTF-16编码，可以看到0x6C49，这与“汉”字的Unicode编码是一致的。如果我们输出“”字的UTF
	 * -16编码，可以看到0xD843, 0xDC30。“”字的Unicode编码是0x20C30，它的UTF-16编码是怎样得到的呢？
	 * 4.2.2.1 编码规则 UTF-16编码以16位无符号整数为单位。我们把Unicode编码记作U。编码规则如下：
	 * ?如果U<0x10000，U的UTF-16编码就是U对应的16位无符号整数（为书写简便，下文将16位无符号整数记作WORD）。
	 * ?如果U≥0x10000，我们先计算U'=U-0x10000，然后将U'写成二进制形式：yyyy yyyy yyxx xxxx
	 * xxxx，U的UTF-16编码（二进制）就是：110110yyyyyyyyyy 110111xxxxxxxxxx。 为什么U
	 * '可以被写成20个二进制位？Unicode的最大码位是0x10ffff，减去0x10000后，U'的最大值是0xfffff，所以肯定可以用20个二进制位
	 * 表 示 。 例如：“”字的Unicode编码是0x20C30，减去0x10000后，得到0x10C30，写成二进制是：
	 *  0001 0000 1100 0011 0000。
	 *  用前10位依次替代模板中的y，用后10位依次替代模板中的x，就得到：1101100001000011   1101110000110000，即0xD843 0xDC30。
	 */
	public static void main(String[] args) {
		try {
			FileInputStream in = new FileInputStream(
					"D:\\Users\\EX-FENGGUANGJING001\\Desktop\\emojiSBUtfMap");
			InputStreamReader inputreader = new InputStreamReader(in);
			BufferedReader buffreader = new BufferedReader(inputreader);
			String line;
			while ((line = buffreader.readLine()) != null) {
				// System.out.println(line);
				String unicode = "";
				try {
					String[] utf = line.split(", \"utf16\": \"");
					String[] utf2 = utf[1].split("\"}");
					unicode = utf2[0];
					// System.out.print(utf2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				uft16beToUnicode(line, unicode);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void uft16beToUnicode(String all, String utf16be) {
		// String utf16be = "\ud83d\ude04";// 1f60a
		// try {
		// System.out.println(URLDecoder.decode(utf16be, "utf16-be"));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }

		// 转换格式110110yyyyyyyyyy 110111xxxxxxxxxx
//		int codehh = 0xd83c;
//		int codell = 0xdf8c;
//		System.out.println("codehh = "+codehh+";codell = "+codell);
		
		int codeh = 0x0000;
		int codel = 0x0000;
		String[] codes = utf16be.split(" ");
		if (codes.length > 0) {
			String high = codes[0];
			for (int i = 0; i < high.length(); i++) {
				char c = high.charAt(i);
				if(i==0){
					codeh += charToInt(c)*16*16*16;
				}else if(i==1){
					codeh += charToInt(c)*16*16;
				}else if(i==2){
					codeh += charToInt(c)*16;
				}else if(i==3){
					codeh += charToInt(c);
				}
			}

			String low = codes[1];
			for (int i = 0; i < low.length(); i++) {
				char c = low.charAt(i);
				if(i==0){
					codel += charToInt(c)*16*16*16;
				}else if(i==1){
					codel += charToInt(c)*16*16;
				}else if(i==2){
					codel += charToInt(c)*16;
				}else if(i==3){
					codel += charToInt(c);
				}
			}
		}

		String strcodeh = Integer.toBinaryString(codeh);
		String strcodel = Integer.toBinaryString(codel);
		// System.out.println(strcodeh);
		// System.out.println(strcodel);

		StringBuilder builder = new StringBuilder();
		// 高十位
		for (int i = 0; i < strcodeh.length(); i++) {
			if (i > 5) {
				builder.append(strcodeh.charAt(i));
			}
		}
		// 低十位
		for (int i = 0; i < strcodel.length(); i++) {
			if (i > 5) {
				builder.append(strcodel.charAt(i));
			}
		}

		// 转unicode
		String utf16str = builder.toString();
		// System.out.println(utf16str);
		int count = 0;
		String unicode = "";// 0f604
		for (int i = 0; i < utf16str.length(); i++) {
			if (i % 4 == 0) {
				count = 0;
				count += Integer.parseInt(utf16str.charAt(i) + "") * 8;
			} else if (i % 4 == 1) {
				count += Integer.parseInt(utf16str.charAt(i) + "") * 4;
			} else if (i % 4 == 2) {
				count += Integer.parseInt(utf16str.charAt(i) + "") * 2;
			} else if (i % 4 == 3) {
				count += Integer.parseInt(utf16str.charAt(i) + "") * 1;
				unicode += Integer.toHexString(count);
			}
		}
		System.out.println("all = " + all + ";utf16be = " + utf16be
				+ "; unicode = " + unicode);
	}
	
	public static int charToInt(char c){
		int shi = 0;
		if(c=='a'){
			shi = 10;
		}else if(c=='b'){
			shi = 11;
		}else if(c=='c'){
			shi = 12;
		}else if(c=='d'){
			shi = 13;
		}else if(c=='e'){
			shi = 14;
		}else if(c=='f'){
			shi = 15;
		}else{
			shi = Integer.parseInt(c+"");
		}
		return shi;
	}
}
