package SHA256;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.math.BigInteger;


import jcuda.*;
import jcuda.runtime.*;

/**
 * @author Cosmo
 * @date 2021/9/15 19:40
 */
public class SHA256 {
    public static void main(String[] args) {
        long startTime =  System.currentTimeMillis();
        int digits = 2;  //输入字符串的位数
        while (true) {
            digits++;
            byte[] input = new byte[digits];
            while (true) {
                int index = 0;
                while (index < digits) {
                    if (input[index] == 127) {
                        input[index] = 0;
                        index++;
                    } else {
                        input[index]++;
                        break;
                    }
                }
                if (index == digits) {
                    break;
                }
                if (zeroJudge(input) != 0){
                    for (byte b : input) {
                        System.out.printf("%s", (char)b);
                    }
                    System.out.println();
                    System.out.println(zeroJudge(input));
                    byte[] inputB = getSHA256(input);
                    for (byte b : inputB) {
                        System.out.printf("%x", b);
                    }
                    System.out.println();
                    long endTime =  System.currentTimeMillis();
                    long usedTime = (endTime-startTime)/1000;
                    System.out.println(usedTime);
                    System.exit(0);
                }
            }
        }
    }

    /**
     * 判断输入的字符串的Hash值前几位是否为0
     * @param bytes 输入的字符串
     * @return int Hash值前面多少位为0
     */
    private static int zeroJudge(byte[] bytes){
        byte[] SHA = Arrays.copyOfRange(getSHA256(bytes), 0, 4);
        String h2bStr = new BigInteger(1, SHA).toString(2);
        int zeroDigits = 32 - h2bStr.length();
        int satisfy_digit = 0;
        if (h2bStr.equals("0")) {
            //前32位是0
            satisfy_digit = 32;
        } else if (zeroDigits >= 31) {
            //前31位是0
            satisfy_digit = 31;
        } else if (zeroDigits == 30) {
            //前30位是0
            satisfy_digit = 30;
        }  //若均不满足则返回0
        return satisfy_digit;
    }

    /**
     * 利用java原生的类实现SHA256加密
     * @param bytes 输入
     * @return byte[] 输入的SHA-256哈希值
     */
    public static byte[] getSHA256(byte[] bytes){
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(bytes);
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

}