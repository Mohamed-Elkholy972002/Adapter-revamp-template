package com.fawry.adapter_template.adapterFramework.transformer.Impl;

import com.fawry.adapter_template.Exceptions.customExceptions.BusinessException;
import com.fawry.adapter_template.adapterFramework.transformer.IAdapterTransformer;
import com.fawry.adapter_template.model.configuration.statusCode.AdapterStatusCodeConfig;
import com.fawry.adapter_template.model.configuration.statusCode.AdapterStatusCodeConfigs;
import com.fawry.adapter_template.model.dto.req.ConfirmLoanRequest;
import com.fawry.adapter_template.model.dto.res.ConfirmLoanResponse;
import com.fawry.adapter_template.model.fawryType.*;
import com.fawry.adapter_template.model.fawryType.extract.FawryTypeExtraction;
import com.fawry.adapter_template.model.fawryType.injection.FawryTypeInjection;
import com.fawry.adapter_template.util.RequestContext.RequestContextUtil;
import com.fawry.adapter_template.util.adapterUtils.AdapterTransformerHelper;
import com.fawry.adapter_template.util.adapterUtils.ExtraBillInfoBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component("DebitAddRequestTransformerImpl")
public class DebitAddRequestTransformerImpl implements IAdapterTransformer {
    private final FawryTypeExtraction fawryTypeExtraction;
    private final AdapterTransformerHelper adapterTransformerHelper;
    private final AdapterStatusCodeConfigs adapterStatusCodeConfigs;
    private final FawryTypeInjection fawryTypeInjection;
    private final ExtraBillInfoBuilder extraBillInfoBuilder;

    @Override
    public Object transformRequest(FAWRYType request, FAWRYType response) {
        ConfirmLoanRequest confirmLoanRequest = ConfirmLoanRequest.builder().build();

        RequestType requestType = request.getRequest();
        BankSvcRqType bankSvcRqType = requestType.getBankSvcRq();
        MsgRqHdrType msgRqHdr = bankSvcRqType.getMsgRqHdr();
        CustomPropertiesType customPropertiesType = msgRqHdr.getCustomProperties();
        List<CustomPropertyType> customPropertyTypeList = customPropertiesType.getCustomProperty();
        SignonRqType signonRqType = requestType.getSignonRq();

        // Extract transactionId from CustomProperties
        String transactionId = fawryTypeExtraction.extractCustomPropertyTypeValue(customPropertyTypeList, "transactionId", "PREV_transactionId")
                .orElseThrow(() -> new BusinessException("transactionId not found"));
        confirmLoanRequest.setTransactionId(transactionId);

        // Extract OTP from SignonPswd/CustPswd
        SignonPswdType signonPswd = signonRqType.getSignonPswd();
        if (signonPswd == null || signonPswd.getCustPswd() == null || signonPswd.getCustPswd().trim().isEmpty()) {
            throw new BusinessException("OTP not found in SignonPswd/CustPswd");
        }
        confirmLoanRequest.setOtp(signonPswd.getCustPswd());

        // Extract BTC code from DebitInfo
        DebitAddRqType debitAddRqType = bankSvcRqType.getDebitAddRq();
        if (debitAddRqType != null) {
            adapterTransformerHelper.saveBtcAtContextUtil(debitAddRqType);
        }

        // Extract Location_Lng and Location_Lat from CustomProperties
        String locationLng = fawryTypeExtraction.extractCustomPropertyTypeValue(customPropertyTypeList, "Location_Lng")
                .orElse("0");
        RequestContextUtil.setLocationLng(locationLng);

        String locationLat = fawryTypeExtraction.extractCustomPropertyTypeValue(customPropertyTypeList, "Location_Lat")
                .orElse("0");
        RequestContextUtil.setLocationLat(locationLat);

        log.info("ConfirmLoanRequest: {}", confirmLoanRequest);
        return confirmLoanRequest;
    }

    @Override
    public FAWRYType transformResponse(Object response, FAWRYType request) {
        ConfirmLoanResponse responseDto = (ConfirmLoanResponse) response;

        // Map status code
        Integer statusCode = responseDto.getStatus() != null ? responseDto.getStatus().getCode() : null;
        String statusKey = statusCode != null  ? String.valueOf(statusCode) : null;

        Map<String, AdapterStatusCodeConfig> adapterStatusCodeConfigMap = adapterStatusCodeConfigs.getConfigs();
        AdapterStatusCodeConfig adapterStatusCodeConfig = adapterStatusCodeConfigMap.getOrDefault(
                statusKey, 
                adapterStatusCodeConfigMap.get("default")
        );

        // Create base response
        FAWRYType fawryTypeResponse = adapterTransformerHelper.createDummyResponse(
                request,
                Long.parseLong(adapterStatusCodeConfig.getFawryCode()),
                adapterStatusCodeConfig.getStatusDesc()
        );

        ResponseType responseType = fawryTypeResponse.getResponse();
        BankSvcRsType bankSvcRs = responseType.getBankSvcRs();
        DebitAddRsType debitAddRsType = bankSvcRs.getDebitAddRs();
        List<DebitInfoType> debitInfoTypes = debitAddRsType.getDebitInfo();
        DebitInfoType debitInfoType = debitInfoTypes.get(0);
        MsgRqHdrType msgRqHdr = bankSvcRs.getMsgRqHdr();
        CustomPropertiesType customPropertiesType = msgRqHdr.getCustomProperties();
        List<CustomPropertyType> customPropertyTypeList = customPropertiesType.getCustomProperty();

        if (responseDto.getTransactionReceipt() == null ) {
            throw new BusinessException("TransactionReceipt can't be null.");
        }
        ConfirmLoanResponse.TransactionReceipt receipt = responseDto.getTransactionReceipt();

        // Map transaction receipt fields to custom properties
        if (receipt.getId() != null && receipt.getId().getValue() != null) {
            fawryTypeInjection.injectCustomProperty(customPropertyTypeList, "transaction_receipt_Id",
                    String.valueOf(receipt.getId().getValue()));
        }else {
            throw new BusinessException("Transaction Receipt ID or it's value can't be null.");
        }

        if (receipt.getDate() != null && receipt.getDate().getValue() != null) {
            fawryTypeInjection.injectCustomProperty(customPropertyTypeList, "transaction_date",
                    String.valueOf(receipt.getDate().getValue()));
        }else {
            throw new BusinessException("Transaction Receipt date or it's value can't be null.");
        }

        if (receipt.getTime() != null && receipt.getTime().getValue() != null) {
            fawryTypeInjection.injectCustomProperty(customPropertyTypeList, "transaction_time",
                    String.valueOf(receipt.getTime().getValue()));
        }else {
            throw new BusinessException("Transaction Receipt time or it's value can't be null.");
        }

        if (receipt.getType() != null && receipt.getType().getValue() != null) {
            debitInfoType.setPmtType(String.valueOf(receipt.getType().getValue()));
        }else {
            throw new BusinessException("Type or it's value can't be null.");
        }

        if (receipt.getStatus() != null && receipt.getStatus().getValue() != null) {
            StatusType statusType =new StatusType();
            statusType.setStatusCode(fawryTypeResponse.getResponse().getBankSvcRs().getStatus().getStatusCode());
            statusType.setSeverity(fawryTypeResponse.getResponse().getBankSvcRs().getStatus().getSeverity());
            statusType.setStatusDesc(String.valueOf(receipt.getStatus().getValue()));
            debitInfoType.setStatus(statusType);
        }else {
            throw new BusinessException("Transaction Receipt status or it's value can't be null.");
        }

        if (receipt.getFinancingAmount() != null && receipt.getFinancingAmount().getValue() != null) {
            fawryTypeInjection.injectCustomProperty(customPropertyTypeList, "financingAmount",
                    String.valueOf(receipt.getFinancingAmount().getValue()));
        }else {
            throw new BusinessException("Financing amount or it's value can't be null.");
        }

        if (receipt.getAdmissionFees() != null && receipt.getAdmissionFees().getValue() != null) {
            fawryTypeInjection.injectCustomProperty(customPropertyTypeList, "PurchaseAdminFees",
                    String.valueOf(receipt.getAdmissionFees().getValue()));
        }else {
            throw new BusinessException("Admission fees or it's value can't be null.");
        }

        if (receipt.getDownPayment() != null && receipt.getDownPayment().getValue() != null) {
            fawryTypeInjection.injectCustomProperty(customPropertyTypeList, "downPayment",
                    String.valueOf(receipt.getDownPayment().getValue()));
        }else {
            throw new BusinessException("Down payment or it's value can't be null.");
        }

        if (receipt.getInstallmentPeriod() != null && receipt.getInstallmentPeriod().getValue() != null) {
            fawryTypeInjection.injectCustomProperty(customPropertyTypeList, "installmentPeriod",
                    String.valueOf(receipt.getInstallmentPeriod().getValue()));
        }else {
            throw new BusinessException("Installment period or it's value can't be null.");
        }

        if (receipt.getMonthlyInstallment() != null && receipt.getMonthlyInstallment().getValue() != null) {
            fawryTypeInjection.injectCustomProperty(customPropertyTypeList, "monthlyInstallmentValue",
                    String.valueOf(receipt.getMonthlyInstallment().getValue()));
        }else {
            throw new BusinessException("Monthly installment or it's value can't be null.");
        }

        // Map applicationId from issuedLoans
        if (responseDto.getIssuedLoans() != null && !responseDto.getIssuedLoans().isEmpty()) {
            ConfirmLoanResponse.IssuedLoan issuedLoan = responseDto.getIssuedLoans().get(0);
            if (issuedLoan.getApplicationId() != null) {
                fawryTypeInjection.injectCustomProperty(customPropertyTypeList, "applicationId",
                        issuedLoan.getApplicationId());
            }else{
                throw new BusinessException("application Id from issued loans value can't be null.");
            }
        }

        // Build ExtraBillInfo
        String btcCode = RequestContextUtil.getBillTypeCode();
        String extraBillInfo = extraBillInfoBuilder.buildExtraBillInfo(responseDto, btcCode);
        debitInfoType.setExtraBillInfo(extraBillInfo);

        return fawryTypeResponse;
    }
}
