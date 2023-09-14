package com.team3.holiday.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractFromStringUtil {

    public static String extractValueFromResponse(String responseBody, String key) {
        Document document = Jsoup.parse(responseBody);
        Elements elements = document.select("code span");

        for (Element element : elements) {
            String spanText = element.text();
            if (spanText.contains("\"" + key + "\"")) {
                int startIndex = spanText.indexOf(":") + 2;
                int endIndex = spanText.length() - 2;
                return spanText.substring(startIndex, endIndex);
            }
        }

        return "";
    }

    public static String extractValueFromCodeSpan(String responseBody) {
        Document document = Jsoup.parse(responseBody);
        Elements elements = document.select("code span");

        for (Element element : elements) {
            return element.text();
        }

        return "";
    }

    public static String extractValueFromDecoderResponse(String responseBody) {
        Document document = Jsoup.parse(responseBody);
        Elements elements = document.select("mark");
        int i = 0;
        for (Element element : elements) {
            i++;
            if (i == 2) {
                return element.text();
            }
        }
        return "";
    }

    public static String extractValueWithAnswerFromCoder(String responseBody, String key) {
        Document document = Jsoup.parse(responseBody);
        Elements elements = document.select("option");
        for (Element element : elements) {
            String spanText = element.toString();
            if (spanText.contains(key) && !spanText.contains("selected")) {
                return element.text();
            }
        }

        return "";
    }
}
