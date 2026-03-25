package com.HMIModule.DTO;

import lombok.Data;

@Data
public class OperationResponseDTO {
    private String status;
    private String note;

    public OperationResponseDTO(String status, String note)
    {
        this.status = status;
        this.note = note;
    }
}
