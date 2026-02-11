package com.fawry.adapter_template.adapterFramework.transformer.Impl;

import com.fawry.adapter_template.Exceptions.customExceptions.BusinessException;
import com.fawry.adapter_template.adapterFramework.transformer.IAdapterTransformer;
import com.fawry.adapter_template.model.configuration.statusCode.AdapterStatusCodeConfig;
import com.fawry.adapter_template.model.configuration.statusCode.AdapterStatusCodeConfigs;
import com.fawry.adapter_template.model.dto.req.ReverseLoanRequest;
import com.fawry.adapter_template.model.dto.res.ReverseLoanResponse;
import com.fawry.adapter_template.model.fawryType.*;
import com.fawry.adapter_template.model.fawryType.extract.FawryTypeExtraction;
import com.fawry.adapter_template.model.fawryType.injection.FawryTypeInjection;
import com.fawry.adapter_template.util.adapterUtils.AdapterTransformerHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component("DebitRevRequestTransformerImpl")
public class DebitRevRequestTransformerImpl implements IAdapterTransformer {

    private final FawryTypeExtraction fawryTypeExtraction;
    private final AdapterTransformerHelper adapterTransformerHelper;
    private final AdapterStatusCodeConfigs adapterStatusCodeConfigs;
    private final FawryTypeInjection fawryTypeInjection;

    @Override
    public Object transformRequest(FAWRYType request, FAWRYType response) {
        ReverseLoanRequest reverseLoanRequest = ReverseLoanRequest.builder().build();
        
        RequestType requestType = request.getRequest();
        BankSvcRqType bankSvcRqType = requestType.getBankSvcRq();
        MsgRqHdrType msgRqHdr = bankSvcRqType.getMsgRqHdr();
        CustomPropertiesType customPropertiesType = msgRqHdr.getCustomProperties();
        List<CustomPropertyType> customPropertyTypeList = customPropertiesType.getCustomProperty();

        // Extract transactionId from CustomProperties
        String transactionId = fawryTypeExtraction.extractCustomPropertyTypeValue(customPropertyTypeList, "transactionId", "PREV_transactionId")
                .orElseThrow(() -> new BusinessException("transactionId not found"));
        reverseLoanRequest.setTransactionId(transactionId);

        // Extract BTC code from DebitRevRq -> DebitMsgRqInfo -> DebitAddRq -> DebitInfo
        if (bankSvcRqType.getDebitRevRq() != null && 
            bankSvcRqType.getDebitRevRq().getDebitMsgRqInfo() != null &&
            bankSvcRqType.getDebitRevRq().getDebitMsgRqInfo().getDebitAddRq() != null) {
            DebitAddRqType debitAddRqType = bankSvcRqType.getDebitRevRq().getDebitMsgRqInfo().getDebitAddRq();
            adapterTransformerHelper.saveBtcAtContextUtil(debitAddRqType);
        }

        log.info("ReverseLoanRequest: {}", reverseLoanRequest);
        return reverseLoanRequest;
    }


    @Override
    public FAWRYType transformResponse(Object response, FAWRYType request) {
        ReverseLoanResponse responseDto = (ReverseLoanResponse) response;

        // Map status code
        Integer statusCode = responseDto.getStatus() != null ? responseDto.getStatus().getCode() : null;
        String statusKey = statusCode != null ? String.valueOf(statusCode) : null ;
        
        Map<String, AdapterStatusCodeConfig> adapterStatusCodeConfigMap = adapterStatusCodeConfigs.getConfigs();
        AdapterStatusCodeConfig adapterStatusCodeConfig = adapterStatusCodeConfigMap.getOrDefault(
                statusKey, 
                adapterStatusCodeConfigMap.get("default")
        );

        // Create base response
        return adapterTransformerHelper.createDummyResponse(
                request,
                Long.parseLong(adapterStatusCodeConfig.getFawryCode()),
                adapterStatusCodeConfig.getStatusDesc()
        );
    }
}
