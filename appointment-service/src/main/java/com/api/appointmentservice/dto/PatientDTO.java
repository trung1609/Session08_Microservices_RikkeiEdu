package com.api.appointmentservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDTO {
    private Long id;
    private String fullName;
    private String address;
    private String medicalHistory;
}
