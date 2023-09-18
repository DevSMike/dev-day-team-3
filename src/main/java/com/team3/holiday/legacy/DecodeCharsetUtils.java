package com.team3.holiday.legacy;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.nio.charset.Charset;

public class DecodeCharsetUtils {

    public static Charset detectCharset(byte[] data) {
        CharsetDetector detector = new CharsetDetector();
        detector.setText(data);
        CharsetMatch match = detector.detect();

        if (match != null) {
            return Charset.forName(match.getName());
        } else {
            return null;
        }
    }

}
