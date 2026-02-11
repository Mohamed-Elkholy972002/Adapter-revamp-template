package com.fawry.adapter_template.model.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApproveTransactionRequest {
    private String serviceCode;
    private String language;
    private String sessionId;
    private String clientMobileNumber;
    private String clientId;
    private String loanAmount;
    private String downPayment;
    private String productAmount;
    private String tenor;
    private String installmentValue;
    private String adminFeesValue;
    private String transactionId;
    private String purchaseRefNo;
    private String requestTime;
    private String signature;
}
