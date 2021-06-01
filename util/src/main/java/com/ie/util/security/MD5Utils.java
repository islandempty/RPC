package com.ie.util.security;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author islandempty
 * @since 2021/6/1
 **/


//MD5
public class MD5Utils {
    private static final String MD5_ALGORITHM = "MD5";

    /**
     * 16进制字符
     */
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


    public static String strToMD5(String str) {
        if (StringUtils.isBlank(str)) {
            throw new NullPointerException();
        }

        return bytesToMD5(StringUtils.bytes(str));
    }

    // MD5将任意长度的“字节串”变换成一个128bit的大整数
    public static String bytesToMD5(byte[] bytes) {
        try {

            var messageDigest = MessageDigest.getInstance(MD5_ALGORITHM);
            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(bytes);

            // 转换并返回结果，也是字节数组，包含16个元素
            // 字符数组转换成字符串返回，MD5将任意长度的字节数组变换成一个16个字节，128bit的大整数
            return byteArrayToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD加密出现未知异常", e);
        }
    }

    //下面这个函数用于将字节数组换成成16进制的字符串
    private static String byteArrayToHex(byte[] bytes) {
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        var resultCharArray = new char[bytes.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        var index = 0;
        for (var b : bytes) {
            resultCharArray[index++] = HEX_CHARS[b >>> 4 & 0xf];
            resultCharArray[index++] = HEX_CHARS[b & 0xf];
        }

        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }
}

