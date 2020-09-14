package com.app.common.utils;

import android.text.TextUtils;

import java.util.HashSet;
import java.util.Set;

public class IdNumVerifyUtil {
    private static final int[] wi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    private static final char[] vi = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    private static Set<String> areaCode = new HashSet<String>();

    private static final int LENGTH_15 = 15;
    private static final int LENGTH_18 = 18;

    public static boolean idNumValid(String idNum) {
        if (TextUtils.isEmpty(idNum) == true) {
            return false;
        }

        if (checkLength(idNum) == false) {
            return false;
        }

        if (checkIsAllNum(idNum) == false) {
            return false;
        }

        if(charAreaCode(idNum) == false) {
            return false;
        }

        // only 18bit has v code
        if (idNum.length() == LENGTH_18) {
            if (checkMod(idNum) == false) {
                return false;
            }
        }

        return true;
    }

    private static boolean charAreaCode(String code) {
        String areaCode = code.substring(0, 2);
        return areaCode.contains(areaCode);
    }

    private static boolean checkLength(String idNum) {
        int length = idNum.length();
        return !(length != LENGTH_15 && length != LENGTH_18);
    }

    private static boolean checkIsAllNum(String idNum) {
        for (int i = 0; i < idNum.length(); i++) {
            if (i == LENGTH_18 - 1) {
                char last = Character.toUpperCase(idNum.charAt(17));
                if (last == 'X') {
                    return true;
                }
            }

            if (Character.isDigit(idNum.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    private static char getVerifyCode(String cardNum) {
        String eightcardid = cardNum.substring(0, cardNum.length() - 1);

        int ai[] = new int[LENGTH_18];
        int sum = 0;
        for (int i = 0; i < eightcardid.length(); i++) {
            char k = eightcardid.charAt(i);
            ai[i] = Integer.parseInt(String.valueOf(k));
        }

        for (int i = 0; i < 17; i++) {
            sum = sum + wi[i] * ai[i];
        }
        int remaining = sum % 11;

        return vi[remaining];
    }

    private static boolean checkMod(String idNum) {
        // only 18bit hsa verify code
        if (idNum.length() == LENGTH_15) {
            return true;
        }

        char vCodeFromIdNum = Character.toUpperCase(idNum.charAt(idNum.length() - 1));
        char vCodeFromCal = getVerifyCode(idNum);
        return vCodeFromCal == vCodeFromIdNum;
    }

    static {
        areaCode.add("11");
        areaCode.add("12");
        areaCode.add("13");
        areaCode.add("14");
        areaCode.add("15");

        areaCode.add("21");
        areaCode.add("22");
        areaCode.add("23");

        areaCode.add("31");
        areaCode.add("32");
        areaCode.add("33");
        areaCode.add("34");
        areaCode.add("35");
        areaCode.add("36");
        areaCode.add("37");

        areaCode.add("41");
        areaCode.add("42");
        areaCode.add("43");
        areaCode.add("44");
        areaCode.add("45");
        areaCode.add("46");

        areaCode.add("50");
        areaCode.add("51");
        areaCode.add("52");
        areaCode.add("53");
        areaCode.add("54");

        areaCode.add("61");
        areaCode.add("62");
        areaCode.add("63");
        areaCode.add("64");
        areaCode.add("65");

        areaCode.add("71");

        areaCode.add("81");
        areaCode.add("82");

        areaCode.add("91");
    }
}
