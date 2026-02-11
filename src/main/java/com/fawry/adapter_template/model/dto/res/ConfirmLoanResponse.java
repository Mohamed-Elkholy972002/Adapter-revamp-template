package com.fawry.adapter_template.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmLoanResponse {
    private Status status;
    private TransactionReceipt transactionReceipt;
    private List<IssuedLoan> issuedLoans;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Status {
        private Integer code;
        private String message;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionReceipt {
        private KeyValue id;
        private KeyValue date;
        private KeyValue time;
        private KeyValue type;
        private KeyValue status;
        private KeyValue financingAmount;
        private KeyValue admissionFees;
        private KeyValue downPayment;
        private KeyValue installmentPeriod;
        private KeyValue monthlyInstallment;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyValue {
        private String key;
        private Object value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IssuedLoan {
        private String applicationId;
        private String loanTransactionId;
    }
}

