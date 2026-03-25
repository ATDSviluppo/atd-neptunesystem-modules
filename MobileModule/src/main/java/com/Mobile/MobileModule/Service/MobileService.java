package com.Mobile.MobileModule.Service;

import com.Mobile.MobileModule.DTO.DeviceDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MobileService {
    @Transactional
    List<DeviceDTO> getDevice();
}
