package com.fawry.adapter_template.model.configuration.BTC;

import lombok.Data;

@Data
public class BtcConfiguration {
    private String integratorName;
    private String serverKey;
    private String publicKeyPath;
    private ExtraBillInfoPrefixes extraBillInfoPrefixes;
    
    @Data
    public static class ExtraBillInfoPrefixes {
        private String prefix1;  // For transaction_receipt_ID
        private String prefix2;  // For installment_period
        private String prefix3;  // For monthlyInstallmentValue
        private String prefix4;  // For financing_amount
        private String prefix5;  // For admission_fees
        private String prefix6;  // For down_payment
        private String prefix7;  // For admission_fees + down_payment
    }
}
