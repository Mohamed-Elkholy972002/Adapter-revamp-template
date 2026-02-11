package com.fawry.adapter_template;

import com.fawry.adapter_template.model.configuration.BTC.BtcConfigurations;
import com.fawry.adapter_template.model.configuration.adapter.AdaptersConfigs;
import com.fawry.adapter_template.model.configuration.general.GeneralConfigsAdapter;
import com.fawry.adapter_template.model.configuration.statusCode.AdapterStatusCodeConfigs;
import com.fawry.adapter_template.model.configuration.webService.AdapterWebServiceConfigs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AdaptersConfigs.class, AdapterStatusCodeConfigs.class, AdapterWebServiceConfigs.class, BtcConfigurations.class, GeneralConfigsAdapter.class})
public class AdapterApplication {

    public static void main(String[] args) {

        SpringApplication.run(AdapterApplication.class, args);
    }


}
