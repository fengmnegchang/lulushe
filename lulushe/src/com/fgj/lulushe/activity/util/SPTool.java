package com.fgj.lulushe.activity.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPTool {
	private static String SHARE_PRE_NAME = "lulushe_sp";
	private static String masterPassword = "a";
	byte[] text = new byte[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	byte[] password = new byte[] { 'a' };
	private static int JELLY_BEAN_4_2 = 17;

	public static String encrypt(String seed, String cleartext)
			throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] result = encrypt(rawKey, cleartext.getBytes());
		return toHex(result);
	}

	public static String decrypt(String seed, String encrypted)
			throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] enc = toByte(encrypted);
		byte[] result = decrypt(rawKey, enc);
		return new String(result);
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr;
		if (android.os.Build.VERSION.SDK_INT >= JELLY_BEAN_4_2) { // 针对4.2及以上系统版本的兼容
			sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
		} else {
			sr = SecureRandom.getInstance("SHA1PRNG");
		}
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted)
			throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

	public static void putString(Context c, String key, String value) {
		SharedPreferences sp = c.getSharedPreferences(SHARE_PRE_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		try {
			editor.putString(key, encrypt(masterPassword, value));
		} catch (Exception e) {
			e.printStackTrace();
		}
		editor.commit();
	}

	public static String getString(Context c, String key, String defalut) {
		SharedPreferences sp = c.getSharedPreferences(SHARE_PRE_NAME,
				Context.MODE_PRIVATE);
		try {
			String encryptStr = sp.getString(key, defalut);
			if (encryptStr.equals("")) {
				return "";
			} else {
				return decrypt(masterPassword, sp.getString(key, defalut));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void putBoolean(Context c, String key, boolean value) {
		SharedPreferences sp = c.getSharedPreferences(SHARE_PRE_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		try {
			editor.putBoolean(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		editor.commit();
	}

	public static boolean getBoolean(Context c, String key, boolean defalut) {
		SharedPreferences sp = c.getSharedPreferences(SHARE_PRE_NAME,
				Context.MODE_PRIVATE);
		try {
			return sp.getBoolean(key, defalut);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static int getInt(Context c, String key, int defalut){
		SharedPreferences sp = c.getSharedPreferences(SHARE_PRE_NAME,
				Context.MODE_PRIVATE);
		try {
			return sp.getInt(key, defalut);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defalut;
	}
	
	public static void putInt(Context c, String key, int value) {
		SharedPreferences sp = c.getSharedPreferences(SHARE_PRE_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		try {
			editor.putInt(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		editor.commit();
	}

}