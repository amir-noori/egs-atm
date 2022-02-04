package com.egs.BankService.util;

public class FingerPrintUtil {

    public static boolean matchFingerPrints(String actualFingerPrint, String requestedFingerPrint) {
        if (requestedFingerPrint == null || requestedFingerPrint.equals("")) return false;
        return true;
    }

}
