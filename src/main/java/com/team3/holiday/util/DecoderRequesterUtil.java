package com.team3.holiday.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class DecoderRequesterUtil {

    private static final HttpClient HTTP_CLIENT = HttpClients.createDefault();

    public static String getDecodedString (String codedString) throws IOException {

        HttpResponse response = makeDecodePostRequest(codedString);
        HttpEntity entity = response.getEntity();
        String responseText = EntityUtils.toString(entity);

        String coder = ExtractFromStringUtil.extractValueFromDecoderResponse(responseText);
        EntityUtils.consume(entity);
        return  ExtractFromStringUtil.extractValueWithAnswerFromCoder(responseText, coder+":ISO-8859-15");
    }

    private static HttpResponse makeDecodePostRequest(String codedString) throws IOException {

        HttpPost httpPost = new HttpPost("https://2cyr.com/decode/?lang=ru");

        httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpPost.setHeader("Accept-Language", "ru,en;q=0.9");
        httpPost.setHeader("Cache-Control", "max-age=0");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Cookie", "PHPSESSID=f6a0a706428c4727411af372d7566ed7");
        httpPost.setHeader("Dnt", "1");
        httpPost.setHeader("Origin", "https://2cyr.com");
        httpPost.setHeader("Referer", "https://2cyr.com/decode/?lang=ru");
        httpPost.setHeader("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"YaBrowser\";v=\"23\"");
        httpPost.setHeader("Sec-Ch-Ua-Mobile", "?1");
        httpPost.setHeader("Sec-Ch-Ua-Platform", "\"Android\"");
        httpPost.setHeader("Sec-Fetch-Dest", "document");
        httpPost.setHeader("Sec-Fetch-Mode", "navigate");
        httpPost.setHeader("Sec-Fetch-Site", "same-origin");
        httpPost.setHeader("Sec-Fetch-User", "?1");
        httpPost.setHeader("Sec-Gpc", "1");
        httpPost.setHeader("Upgrade-Insecure-Requests", "1");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36");

        // Создаем payload
        String payload = "lang=ru&authid=&authpw=&text=" + codedString +"&ident=:WINDOWS-1251:ISO-8859-15&identselected=OK&sample=&src=WINDOWS-1251&dsp=WINDOWS-1252&prf=";

        // Устанавливаем payload как тело запроса
        httpPost.setEntity(new StringEntity(payload));

        // Выполняем запрос и получаем ответ
        return  HTTP_CLIENT.execute(httpPost);
    }
}
