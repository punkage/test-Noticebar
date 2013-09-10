package com.kit.SDK;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class KitCrypto {
	
	private static String key = "";
	
	public KitCrypto(String secretKey) {
		key = secretKey;
	}
	
	public static String encrypt(String message) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(),"AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(message.getBytes());
		String strReturn = new String(Base64_encode(encrypted));
		return strReturn;
	}
	
	public static String encodeURIcomponent(String s)
	{
		String sResult = s.replace("+", "-").replace("/", "_");
	    StringBuilder o = new StringBuilder();
	    for (char ch : sResult.toCharArray()) {
	        if (isUnsafe(ch)) {
	            o.append('%');
	            o.append(toHex(ch / 16));
	            o.append(toHex(ch % 16));
	        }
	        else o.append(ch);
	    }
	    return o.toString();
	}

	private static char toHex(int ch)
	{
	    return (char)(ch < 10 ? '0' + ch : 'A' + ch - 10);
	}

	private static boolean isUnsafe(char ch)
	{
	    if (ch > 128 || ch < 0)
	        return true;
	    return " %$&+,/:;=?@<>#%".indexOf(ch) >= 0;
	}
	
    private static char[] map1 = new char[64];
    static {
            int i = 0;
            for (char c = 'A'; c <= 'Z'; c++) {
                    map1[i++] = c;
            }
            for (char c = 'a'; c <= 'z'; c++) {
                    map1[i++] = c;
            }
            for (char c = '0'; c <= '9'; c++) {
                    map1[i++] = c;
            }
            map1[i++] = '+';
            map1[i++] = '/';
    }


    private static byte[] map2 = new byte[128];
    static {
            for (int i = 0; i < map2.length; i++) {
                    map2[i] = -1;
            }
            for (int i = 0; i < 64; i++) {
                    map2[map1[i]] = (byte) i;
            }
    }
    
    static char[] Base64_encode(byte[] in) {
        return Base64_encode(in, in.length);
    }
    
    static char[] Base64_encode(byte[] in, int iLen) {
        int oDataLen = (iLen * 4 + 2) / 3;
        int oLen = ((iLen + 2) / 3) * 4;
        char[] out = new char[oLen];
        int ip = 0;
        int op = 0;
        while (ip < iLen) {
                int i0 = in[ip++] & 0xff;
                int i1 = ip < iLen ? in[ip++] & 0xff : 0;
                int i2 = ip < iLen ? in[ip++] & 0xff : 0;
                int o0 = i0 >>> 2;
                int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
                int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
                int o3 = i2 & 0x3F;
                out[op++] = map1[o0];
                out[op++] = map1[o1];
                out[op] = op < oDataLen ? map1[o2] : '=';
                op++;
                out[op] = op < oDataLen ? map1[o3] : '=';
                op++;
        }
        return out;
    }
}
