package com.fawry.adapter_template.model.configuration.general;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties("general-config")
public class GeneralConfigsAdapter {
    private String language;
}
