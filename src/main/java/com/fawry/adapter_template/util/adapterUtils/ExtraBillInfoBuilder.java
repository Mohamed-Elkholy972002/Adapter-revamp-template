package com.fawry.adapter_template.util.adapterUtils;

import com.fawry.adapter_template.model.configuration.BTC.BtcConfiguration;
import com.fawry.adapter_template.model.configuration.BTC.BtcConfigurations;
import com.fawry.adapter_template.model.dto.res.ConfirmLoanResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExtraBillInfoBuilder {

    private final BtcConfigurations btcConfigurations;

    /**
     * Builds ExtraBillInfo string using configurable prefixes
     * Format: %Prefix1% + [transaction_receipt_ID] + ";" + %Prefix2% + [installment_period] + ";" + ...
     *
     * @param response The confirm loan response
     * @param btcCode The BTC code
     * @return The formatted ExtraBillInfo string
     */
    public String buildExtraBillInfo(ConfirmLoanResponse response, String btcCode) {
        if (response == null || response.getTransactionReceipt() == null) {
            log.warn("Cannot build ExtraBillInfo: response or transactionReceipt is null");
            return "";
        }

        ConfirmLoanResponse.TransactionReceipt receipt = response.getTransactionReceipt();
        StringBuilder extraBillInfo = new StringBuilder();

        // Get prefixes from BTC configuration (default language is English)
        BtcConfiguration.ExtraBillInfoPrefixes prefixes = getPrefixes(btcCode);
        if (prefixes == null) {
            log.warn("Prefixes not found for BTC: {}", btcCode);
            return "";
        }

        // Extract values
        String transactionReceiptId = getValue(receipt.getId());
        String installmentPeriod = getValue(receipt.getInstallmentPeriod());
        String monthlyInstallment = getValue(receipt.getMonthlyInstallment());
        String financingAmount = getValue(receipt.getFinancingAmount());
        String admissionFees = getValue(receipt.getAdmissionFees());
        String downPayment = getValue(receipt.getDownPayment());

        // Calculate total (admission_fees + down_payment)
        String total = calculateTotal(admissionFees, downPayment);

        // Build ExtraBillInfo string - only append if prefix is defined
        appendIfPrefixExists(extraBillInfo, prefixes.getPrefix1(), transactionReceiptId);
        appendIfPrefixExists(extraBillInfo, prefixes.getPrefix2(), installmentPeriod);
        appendIfPrefixExists(extraBillInfo, prefixes.getPrefix3(), monthlyInstallment);
        appendIfPrefixExists(extraBillInfo, prefixes.getPrefix4(), financingAmount);
        appendIfPrefixExists(extraBillInfo, prefixes.getPrefix5(), admissionFees);
        appendIfPrefixExists(extraBillInfo, prefixes.getPrefix6(), downPayment);
        appendIfPrefixExists(extraBillInfo, prefixes.getPrefix7(), total);

        return extraBillInfo.toString();
    }

    private BtcConfiguration.ExtraBillInfoPrefixes getPrefixes(String btcCode) {
        if (btcConfigurations == null || btcConfigurations.getConfigs() == null) {
            log.warn("BTC configurations not available");
            return null;
        }

        BtcConfiguration btcConfig = btcConfigurations.getConfigs().get(btcCode);
        if (btcConfig == null) {
            log.warn("BTC configuration not found for code: {}", btcCode);
            return null;
        }

        return btcConfig.getExtraBillInfoPrefixes();
    }

    private String getValue(ConfirmLoanResponse.KeyValue keyValue) {
        if (keyValue == null || keyValue.getValue() == null) {
            return "0";
        }
        return String.valueOf(keyValue.getValue());
    }

    private String calculateTotal(String admissionFees, String downPayment) {
        try {
            double fees = Double.parseDouble(admissionFees);
            double down = Double.parseDouble(downPayment);
            return String.valueOf(fees + down);
        } catch (NumberFormatException e) {
            log.warn("Error calculating total: {}", e.getMessage());
            return "0";
        }
    }

    private void appendIfPrefixExists(StringBuilder builder, String prefix, String value) {
        if (prefix != null && !prefix.trim().isEmpty()) {
            builder.append(prefix).append(value).append(";");
        }
    }
}

