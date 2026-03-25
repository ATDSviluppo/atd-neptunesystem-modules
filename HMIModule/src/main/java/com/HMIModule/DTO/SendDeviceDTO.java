package com.HMIModule.DTO;

import com.CommonModule.CommonModule.DTO.DeviceDTO;
import lombok.Data;

@Data
public class SendDeviceDTO {
    ApiResponseDTO apiResponseDTO;
    DeviceDTO deviceDTO;

    public SendDeviceDTO(ApiResponseDTO apiResponseDTO, DeviceDTO deviceDTO) {
        this.apiResponseDTO = apiResponseDTO;
        this.deviceDTO = deviceDTO;
    }

    public SendDeviceDTO() {
    }
}
