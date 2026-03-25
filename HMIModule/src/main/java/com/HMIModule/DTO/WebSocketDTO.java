package com.HMIModule.DTO;

import lombok.Data;

@Data
public class WebSocketDTO {
    private String keyword;
    private String machineId;

    public WebSocketDTO(String keyword, String machineId) {
        this.keyword = keyword;
        this.machineId = machineId;
    }
}
