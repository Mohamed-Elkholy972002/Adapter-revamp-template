package com.fawry.adapter_template.adapterFramework.connector.Impl;

import com.fawry.adapter_template.Exceptions.customExceptions.BusinessException;
import com.fawry.adapter_template.adapterFramework.connector.IAdapterConnector;
import com.fawry.adapter_template.client.factory.RestTemplateFactory;
import com.fawry.adapter_template.model.configuration.BTC.BtcConfiguration;
import com.fawry.adapter_template.model.configuration.BTC.BtcConfigurations;
import com.fawry.adapter_template.model.configuration.general.GeneralConfigsAdapter;
import com.fawry.adapter_template.model.configuration.webService.AdapterWebServiceConfig;
import com.fawry.adapter_template.model.configuration.webService.AdapterWebServiceConfigs;
import com.fawry.adapter_template.model.dto.req.ConfirmLoanRequest;
import com.fawry.adapter_template.model.dto.res.ConfirmLoanResponse;
import com.fawry.adapter_template.util.RequestContext.RequestContextUtil;
import com.fawry.adapter_template.util.adapterUtils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;

@RequiredArgsConstructor
@Component("DebitAddConnectorImpl")
public class DebitAddConnectorImpl implements IAdapterConnector {
    private final AdapterWebServiceConfigs adapterWebServiceConfigs;
    private final RestTemplateFactory restTemplateFactory;
    private final BtcConfigurations btcConfigurations;
    private final SecurityUtil securityUtil;
    private final GeneralConfigsAdapter generalConfigsAdapter;

    @Override
    public Object sendReceive(Object request) {
        ConfirmLoanRequest confirmLoanRequest = (ConfirmLoanRequest) request;
        AdapterWebServiceConfig adapterWebServiceConfig = adapterWebServiceConfigs.getConfigs().get(RequestContextUtil.getMessageCode());

        String url = adapterWebServiceConfig.getUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        
        // Get BTC configuration for RSA signature
        String btcCode = RequestContextUtil.getBillTypeCode();
        if (btcCode == null || btcCode.trim().isEmpty()) {
            throw new BusinessException("BTC code not found in request");
        }
        
        BtcConfiguration btcConfig = btcConfigurations.getConfigs().get(btcCode);
        if (btcConfig == null) {
            throw new BusinessException("BTC configuration not found for code: " + btcCode);
        }
        
        // Generate RSA signature for Authorization header
        long unixTime = Instant.now().getEpochSecond();
        String signature = securityUtil.generateHalanSignature(
                btcConfig.getIntegratorName(),
                btcConfig.getServerKey(),
                unixTime,
                btcConfig.getPublicKeyPath()
        );
        headers.set("Authorization", signature);
        
        // Set Location headers from CustomProperties
        String locationLng = RequestContextUtil.getLocationLng();
        String locationLat = RequestContextUtil.getLocationLat();
        String language = generalConfigsAdapter.getLanguage();
        headers.set("Location_Lng", locationLng);
        headers.set("Location_Lat", locationLat);
        headers.set("Language", language);
        HttpEntity<ConfirmLoanRequest> entity = new HttpEntity<>(confirmLoanRequest, headers);
        ResponseEntity<ConfirmLoanResponse> responseEntity = restTemplateFactory.restTemplateLowerCamelCase()
                .exchange(url, HttpMethod.POST, entity, ConfirmLoanResponse.class);

        return responseEntity.getBody();
    }
}
