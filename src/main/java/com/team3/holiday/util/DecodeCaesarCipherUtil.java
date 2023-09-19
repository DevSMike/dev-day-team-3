package com.team3.holiday.util;

public class DecodeCaesarCipherUtil {

    public static String decodeCaesarCipher(String encodedText, int offset) {
        StringBuilder decodedText = new StringBuilder();

        for (char character : encodedText.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = Character.isLowerCase(character) ? 'a' : 'A';
                char decodedChar = (char) (((character - base - offset + 26) % 26) + base);
                decodedText.append(decodedChar);
            } else {
                // Если символ не буква, добавляем его без изменений
                decodedText.append(character);
            }
        }
        return decodedText.toString();
    }
}
