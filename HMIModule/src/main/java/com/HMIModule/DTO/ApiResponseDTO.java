package com.HMIModule.DTO;

import lombok.Data;

@Data
public class ApiResponseDTO {
    private String type;
    private String data;

    // Costruttore predefinito
    public ApiResponseDTO() {}

    // Costruttori sovraccarichi per diversi scenari
    public ApiResponseDTO(String type, String data) {
        this.type = type;
        this.data = data;
    }
}
