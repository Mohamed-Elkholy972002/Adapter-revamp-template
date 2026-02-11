package com.fawry.adapter_template.model.configuration.webService;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "adapter-web-service-configs")
public class AdapterWebServiceConfigs {
    Map<String, AdapterWebServiceConfig> Configs=new HashMap<>();
}