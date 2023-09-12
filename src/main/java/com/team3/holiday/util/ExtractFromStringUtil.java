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
}
