package com.fawry.adapter_template.model.dto.res;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String statusCode;
    private String serviceCode;
    private String statusMessage;
    private String loanStatus;
    private String purchaseId;
    private String purchaseReceiptData;
}
