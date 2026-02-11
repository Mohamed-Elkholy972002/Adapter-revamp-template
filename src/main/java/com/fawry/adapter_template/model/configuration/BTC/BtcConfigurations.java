package com.fawry.adapter_template.model.configuration.BTC;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "btc-configurations")
public class BtcConfigurations {
    Map<String, BtcConfiguration> Configs=new HashMap<>();
}
