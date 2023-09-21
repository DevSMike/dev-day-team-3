package com.team3.holiday.service.password;

import com.team3.holiday.util.PasswordGeneratorRequestsUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class PasswordGenerationServiceImpl implements PasswordGenerationService {

    private static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F',
            'a', 'b', 'c', 'd', 'e', 'f'
    };

    @Override
    public String hackLocalPass() {
        long a = 0L;
        long b = passToLong("ffffffff");
        long m = (a + b) / 2;

        while (true) {
            int response = PasswordGeneratorRequestsUtil.tryLocalPass(longToPass(m));

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

    @Override
    public String hackPass() {
        long a = 0L;
        long b = passToLong("ffffffff");
        long m = (a + b) / 2;

        while (true) {
            int response = PasswordGeneratorRequestsUtil.tryPass(longToPass(m));

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

    private long passToLong(String password) {
        int len = password.length();
        long res = 0L;
        long pow = 1;

        for (int i = len - 1; i >= 0; i--) {
            res += Arrays.binarySearch(CHARS, password.charAt(i)) * pow;
            pow *= 22;
        }

        return res;
    }

    private String longToPass(long number) {
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
