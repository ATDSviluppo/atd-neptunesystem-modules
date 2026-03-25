package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service;


import com.CommonModule.CommonModule.Entity.EnumDeviceType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public interface EnumDeviceTypeService {
    List<EnumDeviceType> getEnumDeviceType();

    @Transactional
    void addEnumDeviceType(List<Map<String, Object>> payloadList);

    @Transactional
    void updateEnumDeviceType(List<Map<String, Object>> payloadList);

    @Transactional
    void deleteEnumDeviceTypes(List<Map<String, Object>> payloadList);
}
