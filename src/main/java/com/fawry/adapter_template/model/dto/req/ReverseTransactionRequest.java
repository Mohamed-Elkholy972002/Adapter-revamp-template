package com.fawry.adapter_template.model.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReverseTransactionRequest {
    private String serviceCode;
    private String language;
    private String transactionId;
    private String clientId;
    private String sessionId;
    private String purchaseRefNo;
    private String requestTime;
    private String signature;
}
