package com.qyn.project.util;

import com.qyn.project.entity.Production;
import org.epctagcoder.option.SGTIN.SGTINExtensionDigit;

public class UIDUtil {
    private final static String version = "0110";
    private final static String topDomain = "0010101110010011";
    private final static String category = "1001";
    public static String generateUIDBinCode(String companyCode, String nameCode, String itemSerial)  {
        StringBuffer sb = new StringBuffer();
        sb.append(version);
        sb.append(topDomain);
        sb.append(category);
        sb.append(intStrToBinStr(companyCode, Code.companyCodeBitLen));
        nameCode = SGTINExtensionDigit.EXTENSION_3.getValue() + nameCode;
        sb.append(intStrToBinStr(nameCode, Code.nameCodeBitLen));
        sb.append(intStrToBinStr(itemSerial, Code.UIDSerialBitLen));
        if(sb.length() == 128) {
            return sb.toString();
        } else {
            return Code.ERROR;
        }
    }

    public static Production decodeUIDBin(String bin) {
        String companyCodeBin = bin.substring(24, 24 + Code.companyCodeBitLen);
        String nameCodeBin = bin.substring(24 + Code.companyCodeBitLen, 24 + Code.companyCodeBitLen + Code.nameCodeBitLen);
        String serialBin = bin.substring(24 + Code.companyCodeBitLen + Code.nameCodeBitLen);

        Production production = new Production();
        production.setCompanyCode(binStrToDecStr(companyCodeBin, Code.companyCodeLen));
        production.setNameCode(nameBinStrToDecStr(nameCodeBin));
        production.setSerial(String.valueOf(Integer.parseInt(serialBin, 2)));
        production.setMapFlag(false);
        return production;
    }

    private static String intStrToBinStr(String code, int len) {
        int value = Integer.parseInt(code);
        String str = Integer.toBinaryString(value);
        int zeroNum = len - str.length();
        StringBuffer sb = new StringBuffer();
        for(int i = 0;i < zeroNum;i++) sb.append("0");
        sb.append(str);
        if(sb.length() == len) return sb.toString();
        else {
            System.out.println("intStrToBinStr err");
            return Code.ERROR;
        }
    }

    private static String binStrToDecStr(String code, int len) {
        String str = String.valueOf(Integer.parseInt(code, 2));
        int zeroNum = len - str.length();
        StringBuffer sb = new StringBuffer();
        for(int i = 0;i < zeroNum;i++) sb.append("0");
        sb.append(str);
        return sb.toString();
    }

    private static String nameBinStrToDecStr(String code) {
        String str = String.valueOf(Integer.parseInt(code, 2));
        return str.substring(1);
    }
}
