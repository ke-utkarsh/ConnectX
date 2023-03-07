package ymsli.com.ea1h.utils.common;

/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   12/3/20 11:48 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * MFEcuAdapter : This is responsbile for XTEA Encryption/Decryption for MFECU Commands
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

import android.util.Log;

public class MFEcuAdapter {
    private static int iteration = 32;
    private static final int delta = 0x9E3779B9;
    private static int[]  key = {0x12345678,0x87654321,0x12345678,0x87654321};//128 bits Key value
    private static int keybits;

    /**
     * encrypts input int array to encrypted byte array
     * @param input int array
     * @return encrypted byte array
     */
    public static byte[]  xetaEncrypt(int[] input){
        keybits = key.length - 1;

        int[] data = new int[input.length];
        for(int i=0;i<data.length;i++){
            data[i] = input[i];
        }

        StringBuilder sb = new StringBuilder();
        for (int idx = 0; idx < data.length; idx += 2) {
            encipher(data, idx);
        }

        for(int i=0;i<data.length;i++){
            sb.append(Integer.toHexString(data[i]));
        }

        byte[] vav = tempFun(sb.toString());

        sb.setLength(0);

        int[] returningElement = new int[vav.length];
        for(int i=0;i<returningElement.length;i++){
            returningElement[i] = (vav[i] & 0xFF);
        }

        byte[] finalEncryptedArray = new byte[returningElement.length];
        for(int i=0;i<finalEncryptedArray.length;i++){
            finalEncryptedArray[i] = (byte) (returningElement[i]);
        }
        return finalEncryptedArray;
    }

    /**
     * encrypts the command send to MFECU from phone
     * @param randomNumberInput input array
     * @return encrypted byte array
     */
    public static byte[] encryptionGateway(int[] randomNumberInput){
        String num1 = "";
        for(int i =0;i<4;i++){
            StringBuilder temp = new StringBuilder("00000000");
            for (int bit = 0; bit < 8; bit++) {
                if (((randomNumberInput[i] >> bit) & 1) > 0) {
                    temp.setCharAt(7 - bit, '1');
                }
            }
            num1 = num1.concat(temp.toString());
        }

        String num2 = "";

        for(int i =4;i<8;i++){
            StringBuilder temp = new StringBuilder("00000000");
            for (int bit = 0; bit < 8; bit++) {
                if (((randomNumberInput[i] >> bit) & 1) > 0) {
                    temp.setCharAt(7 - bit, '1');
                }
            }
            num2 = num2.concat(temp.toString());
        }


        int[] randomNumber = new int[2];

        randomNumber[0] = Integer.parseUnsignedInt(num1,2);
        randomNumber[1] = Integer.parseUnsignedInt(num2,2);
        return xetaEncrypt(randomNumber);
    }

    /**
     * runs rounds of encipher process based on encryption key
     * @param data input int array
     * @param idx rounds of encipher to be run
     */
    private static void encipher(int[] data,  int idx) {
        int sum = 0;
        int v0 = data[idx];
        int v1 = data[idx + 1];

        for (int round = 0; round < iteration; ++round) {
            v0 += (((v1 << 4) ^ (v1 >>> 5)) + v1) ^ (sum + key[(idx + sum) & keybits]);
            sum += delta;
            v1 += (((v0 << 4) ^ (v0 >>> 5)) + v0) ^ (sum + key[(idx + (sum >>> 11)) & keybits]);
        }

        data[idx] = v0;
        data[idx + 1] = v1;
    }

    /**
     * converts string to byte array
     * @param input string input
     * @return byte array
     */
    private static byte[] tempFun(String input) {
        if(input.equalsIgnoreCase("6ffa949ceed26af")){
            input = "0".concat(input);
        }
        else if(input.length()==15){
            String part1 = input.substring(0,8);
            String part2 = "0";
            String part3 = input.substring(8);
            input = (part1.concat(part2.concat(part3)));
        }
        int len = input.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            try {
                data[i / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4)
                        + Character.digit(input.charAt(i + 1), 16));
            } catch (StringIndexOutOfBoundsException ex) {
                Log.d("Exception","HEre");
            }
        }
        return data;
    }
}
