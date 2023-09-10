package com.team3.holiday.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dev {

    private String email;
    private String cohort;
    private String firstName;
    private String lastName;
}
