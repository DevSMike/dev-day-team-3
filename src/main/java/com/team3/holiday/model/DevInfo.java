package com.team3.holiday.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevInfo {

    String name;
    String token;
    String nextTaskUrl;
}
