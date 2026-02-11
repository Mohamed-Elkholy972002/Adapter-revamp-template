package com.fawry.adapter_template.model.configuration.adapter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "adaptersconfigs")
public class AdaptersConfigs {
    Map<String, List<AdapterConfig>> configs=new HashMap<>();
}
