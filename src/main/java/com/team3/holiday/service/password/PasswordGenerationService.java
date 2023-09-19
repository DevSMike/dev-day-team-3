package com.team3.holiday.service.password;

public interface PasswordGenerationService {

    Integer tryLocalPass(String password);

    Integer tryPass(String password);
}
