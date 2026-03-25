package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service;


import com.CommonModule.CommonModule.Entity.EnumDeviceDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public interface EnumDeviceDetailService {
    List<EnumDeviceDetail> getEnumDeviceDetail();

    @Transactional
    void addEnumDeviceDetail(List<Map<String, Object>> payloadList);

    @Transactional
    void updateEnumDeviceDetail(List<Map<String, Object>> payloadList);

    @Transactional
    void deleteEnumDeviceDetail(List<Map<String, Object>> payloadList);
}
