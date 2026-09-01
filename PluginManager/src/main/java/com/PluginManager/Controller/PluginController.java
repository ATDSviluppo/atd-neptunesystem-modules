package com.PluginManager.Controller;

import com.PluginManager.Service.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class PluginController {
    @Autowired
    PluginService pluginService;

    @PostMapping("/install/plugin")
    public ResponseEntity<String> installPlugin(@RequestPart("plugin") MultipartFile plugin) throws IOException {
        return pluginService.installPlugin(plugin);
    }


}
