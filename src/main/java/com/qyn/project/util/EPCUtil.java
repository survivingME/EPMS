package com.qyn.project.util;

import com.qyn.project.entity.Production;
import org.epctagcoder.option.SGTIN.SGTINExtensionDigit;
import org.epctagcoder.option.SGTIN.SGTINFilterValue;
import org.epctagcoder.option.SGTIN.SGTINTagSize;
import org.epctagcoder.parse.SGTIN.ParseSGTIN;
import org.epctagcoder.result.SGTIN;
import org.epctagcoder.util.Converter;

public class EPCUtil {
    public static String generateEPCBinCode(String companyCode, String itemCode, String itemSerial) {
        try {
            ParseSGTIN parseSGTIN = ParseSGTIN.Builder()
                    .withCompanyPrefix(companyCode)
                    .withExtensionDigit(SGTINExtensionDigit.EXTENSION_3)
                    .withItemReference(itemCode)
                    .withSerial(itemSerial)
                    .withTagSize(SGTINTagSize.BITS_96)
                    .withFilterValue(SGTINFilterValue.ALL_OTHERS_0)
                    .build();
            SGTIN sgtin = parseSGTIN.getSGTIN();
            return sgtin.getBinary();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Code.ERROR;
        }
    }

    public static Production decodeEPCBin(String bin) {
        String rfid = Converter.binToHex(bin);
        try {
            ParseSGTIN parseSGTIN = ParseSGTIN.Builder().withRFIDTag(rfid).build();
            SGTIN sgtin = parseSGTIN.getSGTIN();
            Production production = new Production();
            production.setCompanyCode(sgtin.getCompanyPrefix());
            production.setNameCode(sgtin.getItemReference());
            production.setSerial(sgtin.getSerial());
            production.setMapFlag(false);
            return production;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
