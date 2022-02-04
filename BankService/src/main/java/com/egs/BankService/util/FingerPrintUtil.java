package com.egs.BankService.util;

/**
 * Mock class for manipulating and validating fingerprints.
 *
 * @author Amir
 */
public class FingerPrintUtil {

    public static boolean matchFingerPrints(String actualFingerPrint, String requestedFingerPrint) {
        if (requestedFingerPrint == null || requestedFingerPrint.equals("")) return false;
        return true;
    }

}
