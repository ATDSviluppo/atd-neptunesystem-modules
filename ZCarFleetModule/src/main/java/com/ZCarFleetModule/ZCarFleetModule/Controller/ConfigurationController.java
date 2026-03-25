package com.ZCarFleetModule.ZCarFleetModule.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ConfigurationController {
    @GetMapping("/StartConfigurationSetting")
    public ResponseEntity<String> StartConfigurationSetting() {
        log.info("StartConfigurationSetting chiamata");
        return ResponseEntity.ok("StartConfigurationSetting executed successfully");
    }

    @GetMapping("/EndConfigurationSession")
    public ResponseEntity<String> EndConfigurationSession() {
        log.info("EndConfigurationSession chiamata");
        return ResponseEntity.ok("EndConfigurationSession executed successfully");
    }

}
