package com.fawry.adapter_template.adapterFramework.connector.Impl;

import com.fawry.adapter_template.Exceptions.customExceptions.BusinessException;
import com.fawry.adapter_template.adapterFramework.connector.IAdapterConnector;
import com.fawry.adapter_template.client.factory.RestTemplateFactory;
import com.fawry.adapter_template.model.configuration.BTC.BtcConfiguration;
import com.fawry.adapter_template.model.configuration.BTC.BtcConfigurations;
import com.fawry.adapter_template.model.configuration.webService.AdapterWebServiceConfig;
import com.fawry.adapter_template.model.configuration.webService.AdapterWebServiceConfigs;
import com.fawry.adapter_template.model.dto.req.ReverseLoanRequest;
import com.fawry.adapter_template.model.dto.res.ReverseLoanResponse;
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
@Component("DebitRevConnectorImpl")
public class DebitRevConnectorImpl implements IAdapterConnector {
    private final AdapterWebServiceConfigs adapterWebServiceConfigs;
    private final RestTemplateFactory restTemplateFactory;
    private final BtcConfigurations btcConfigurations;
    private final SecurityUtil securityUtil;

    @Override
    public Object sendReceive(Object request) {
        ReverseLoanRequest reverseLoanRequest = (ReverseLoanRequest) request;
        AdapterWebServiceConfig adapterWebServiceConfig = adapterWebServiceConfigs
                            .getConfigs().get(RequestContextUtil.getMessageCode());

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

        HttpEntity<ReverseLoanRequest> entity = new HttpEntity<>(reverseLoanRequest, headers);
        ResponseEntity<ReverseLoanResponse> responseEntity = restTemplateFactory.restTemplateLowerCamelCase()
                .exchange(url, HttpMethod.POST, entity, ReverseLoanResponse.class);

        return responseEntity.getBody();
    }
}
