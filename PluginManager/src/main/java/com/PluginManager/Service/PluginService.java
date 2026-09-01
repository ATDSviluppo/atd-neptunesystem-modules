package com.PluginManager.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface PluginService {
    ResponseEntity<String> installPlugin(
            MultipartFile plugin
    ) throws IOException;
}
