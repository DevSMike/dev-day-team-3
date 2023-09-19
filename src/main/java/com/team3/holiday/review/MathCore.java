package com.team3.holiday.review;

import java.util.Arrays;

//review made
public class MathCore {

    private static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F',
            'a', 'b', 'c', 'd', 'e', 'f'
    };
    
   private static final char[] ANSWER = { '5', '5', 'C', 'f', 'c', 'e', 'f', '9'};


    public  String hackPass() {
        long a = 0L;
        long b = passToLong("ffffffff");
        long m = (a + b) / 2;

        while (true) {
            int response = tryPass(longToPass(m));
            if (response == 1) {
                b = m;
                m = (a + b) / 2;
            } else if (response == -1) {
                a = m;
                m = (a + b) / 2;
            } else if (response == 0) {
                break;
            } else {
                throw new RuntimeException("tryPass(" + longToPass(m) + ") вернул " + response);
            }
        }
        return longToPass(m);
    }

    private  int tryPass(String password) {
        char[] charPass = password.toCharArray();

        for (int i = 0; i < password.length(); i++) {
            if (charPass[i] > ANSWER[i]) {
                return 1;
            } else if (charPass[i] < ANSWER[i]) {
                return -1;
            }

        }
        // вернет 1, если >pass
        // вернет -1, если <pass
        // вернет 0, если code 200
        return 0;
    }

    private  long passToLong(String password) {

        int len = password.length();
        long res = 0L;
        long pow = 1;
        for (int i = len - 1; i >= 0; i--) {
            res += Arrays.binarySearch(CHARS, password.charAt(i)) * pow;
            pow *= 22;
        }
        return res;
    }

    private  String longToPass(long number) {
        int r = 0;
        StringBuilder res = new StringBuilder();
        while (number > 0) {

            r = (int) (number % 22);
            res.insert(0, CHARS[r]);
            number = number / 22;
        }
        return res.toString();
    }

}
