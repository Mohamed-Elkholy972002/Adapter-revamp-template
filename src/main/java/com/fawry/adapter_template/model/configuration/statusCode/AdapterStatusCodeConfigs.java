package com.fawry.adapter_template.model.configuration.statusCode;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "adapters-status-code-configs")
public class AdapterStatusCodeConfigs {
    Map<String, AdapterStatusCodeConfig> Configs=new HashMap<>();
}
