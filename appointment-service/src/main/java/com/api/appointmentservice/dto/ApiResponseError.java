package com.api.appointmentservice.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseError {
    private String timestamp;
    private int status;
    private String message;
    private String error;
}