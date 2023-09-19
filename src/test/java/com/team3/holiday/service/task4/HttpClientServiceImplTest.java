package com.team3.holiday.service.task4;

import com.team3.holiday.service.client.HttpClientService;
import com.team3.holiday.tasks.TaskFourHtml;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HttpClientServiceImplTest {

    @Autowired
    private HttpClientService service;

    @Test
    void decodeMessage_whenStringIsIncorrectCoded_thenReturnDecodedString() {
       String decoded =  service.decodeCongratulations(TaskFourHtml.getTempFourTask());
       assertEquals("Широкая электрификация", decoded);
    }
}