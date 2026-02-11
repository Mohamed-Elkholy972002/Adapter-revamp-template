package com.fawry.adapter_template.util.adapterUtils;


public class Messages {

    public static class BillersInquiry {
        public static final String REQUEST_CODE = "BillerInqRq";
        public static final String RESPONSE_CODE = "BillerInqRs";
        public static final String VERSION1 = "V1.0";
    }

    /**
     * @author ahmed_msadek Hold the constants related to bill inquiry process.
     */
    public static class BillInquiry {

        /** bill request code. */
        public static final String REQUEST_CODE = "BillInqRq";

        /** bill response code. */
        public static final String RESPONSE_CODE = "BillInqRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * @author jihan_ragy Hold the constants related to payment process.
     */
    public static class PaymentAdd {

        /** payment request code. */
        public static final String REQUEST_CODE = "PmtAddRq";

        /** payment response code. */
        public static final String RESPONSE_CODE = "PmtAddRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";

        public static final String REQUEST_RETRY_CODE = "PmtAddRetryRq";

        public static final String RESPONSE_RETRY_CODE = "PmtAddRetryRs";

        public static final String REQUEST_REVERSE_CODE = "PmtAddRevRq";

        public static final String RESPONSE_REVERSE_CODE = "PmtAddRevRs";

    }

    /**
     * @author mostafa_sibrahim Hold the constants related to payment advice
     *         process.
     */
    public static class PaymentAdvice {

        /** payment advice request code. */
        public static final String REQUEST_CODE = "PmtAdvRq";

        /** payment advice response code. */
        public static final String RESPONSE_CODE = "PmtAdvRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * @author jihan_ragy Hold the constants related to payment advice Confirmation
     *         process.
     */
    public static class PaymentAdviceConfirumation {

        /** payment advice request code. */
        public static final String REQUEST_CODE = "PmtAdvConfRq";

        /** payment advice response code. */
        public static final String RESPONSE_CODE = "PmtAdvConfRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * @author mostafa_sibrahim Hold the constants related to debit reverse process.
     */
    public static class DebitReverse {

        /** payment debit reverse request code. */
        public static final String REQUEST_CODE = "DebitRevRq";

        /** payment debit reverse response code. */
        public static final String RESPONSE_CODE = "DebitRevRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class DebitRq {
        /** payment debit request code. */
        public static final String REQUEST_CODE = "DebitAddRq";

        /** payment debit response code. */
        public static final String RESPONSE_CODE = "DebitAddRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class RecoCutoffAddRecon {

        /** payment debit reverse request code. */
        public static final String REQUEST_CODE = "ReconCutOffRq";

        /** payment debit reverse response code. */
        public static final String RESPONSE_CODE = "ReconCutOffRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * @author mohamed_talaat Hold the constants related to auto payment processes.
     */

    public static class ManageCustomer {
        /** manage customer request code. */
        public static final String REQUEST_CODE = "MngCustRq";

        /** manage customer response code. */
        public static final String RESPONSE_CODE = "MngCustRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class ManageAccount {
        /** manage Account request code. */
        public static final String REQUEST_CODE = "MngAcctRq";

        /** manage Account response code. */
        public static final String RESPONSE_CODE = "MngAcctRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class SearchCustomerSOFs {
        /** manage customer request code. */
        public static final String REQUEST_CODE = "GetCustSOFsRq";

        /** manage customer response code. */
        public static final String RESPONSE_CODE = "GetCustSOFsRs";

        public static final String VERSION_1 = "V1.0";

        /** version number. */
    }

    public static class GetCustomer {
        /** get customer request code. */
        public static final String REQUEST_CODE = "GetCustRq";

        /** get customer response code. */
        public static final String RESPONSE_CODE = "GetCustRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class BillEnrollment {
        /** Bill enrollment request code. */
        public static final String REQUEST_CODE = "BillEnrRq";

        /** Bill enrollment response code. */
        public static final String RESPONSE_CODE = "BillEnrRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class PaymentMandate {
        /** payment mandate request code. */
        public static final String REQUEST_CODE = "PayMandRq";

        /** payment mandate response code. */
        public static final String RESPONSE_CODE = "PayMandRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class GetEnrollment {
        /** get enrollment request code. */
        public static final String REQUEST_CODE = "GetEnrRq";

        /** get enrollment response code. */
        public static final String RESPONSE_CODE = "GetEnrRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * Lookup types message.
     *
     * @author mohamed_sami.
     */
    public static class Lookup {

        /** get Lookup request code. */
        public static final String REQUEST_CODE = "LookupRq";

        /** get Lookup response code. */
        public static final String RESPONSE_CODE = "LookupRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";

    }

    /**
     * @author ahmed_msadek
     *         <p>
     *         Codes of the manage mandate process
     */
    public static class ManageMandate {
        /** Manage Mandate request code. */
        public static final String REQUEST_CODE = "PayMandRq";

        /** Manage Mandate response code. */
        public static final String RESPONSE_CODE = "PayMandRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * Payment history message.
     *
     * @author mohamed_sami.
     */
    public static class PaymentHistory {
        /** Payment History request code. */
        public static final String REQUEST_CODE = "PmtHistoryRq";

        /** Payment History response code. */
        public static final String RESPONSE_CODE = "PmtHistoryRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * Batch message.
     *
     * @author Mohamed.Talaat.
     */
    public static class Batch {
        /** Batch request code. */
        public static final String REQUEST_CODE = "BatchRq";

        /** Batch response code. */
        public static final String RESPONSE_CODE = "BatchRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * Payment center message.
     *
     * @author mohamed_sami.
     */
    public static class PaymentCenter {
        /** Payment Center request code. */
        public static final String REQUEST_CODE = "BPmtInstRq";

        /** Payment Center response code. */
        public static final String RESPONSE_CODE = "BPmtInstRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * Get Notification points module
     *
     * @author mohamed_sami.
     */
    public static class GetNotificationPts {
        /** Payment Center request code. */
        public static final String REQUEST_CODE = "GetNotifyModelRq";

        /** Payment Center response code. */
        public static final String RESPONSE_CODE = "GetNotifyModelRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * Get Customer Alerts
     *
     * @author mohamed_sami.
     */
    public static class GetCustAlerts {
        /** Payment Center request code. */
        public static final String REQUEST_CODE = "GetCustAlertsRq";

        /** Payment Center response code. */
        public static final String RESPONSE_CODE = "GetCustAlertsRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * Manage Customer Alerts
     *
     * @author mohamed_sami.
     */
    public static class MngCustAlerts {
        /** Payment Center request code. */
        public static final String REQUEST_CODE = "MngCustAlertsRq";

        /** Payment Center response code. */
        public static final String RESPONSE_CODE = "MngCustAlertsRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * Get Customer Categories
     *
     * @author Ahmed.Samy
     */
    public static class GetCustCategories {
        /** Get Customer Categories request code. */
        public static final String REQUEST_CODE = "GetCustCategorisRq";

        /** Get Customer Categories response code. */
        public static final String RESPONSE_CODE = "GetCustCategorisRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * get Account Type extra details
     *
     * @author Ahmed.Samy
     */
    public static class GetAccountTypeExtraDetails {
        /** get Account Type extra details request code. */
        public static final String REQUEST_CODE = "GetAcctTypeDetailsRq";

        /** get Account Type extra details response code. */
        public static final String RESPONSE_CODE = "GetAcctTypeDetailsRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * get EStatement
     *
     * @author Ahmed.Samy
     */
    public static class EStatement {
        /** get EStatement request code. */
        public static final String REQUEST_CODE = "EStatementRq";

        /** get EStatement response code. */
        public static final String RESPONSE_CODE = "EStatementRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class BatchExecuter {
        /** get EStatement request code. */
        public static final String REQUEST_CODE = "BatchExecuterRq";

        /** get EStatement response code. */
        public static final String RESPONSE_CODE = "BatchExecuterRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class PaymentTransactionInquiry {
        /** get EStatement request code. */
        public static final String REQUEST_CODE = "PmtInqRq";

        /** get EStatement response code. */
        public static final String RESPONSE_CODE = "PmtInqRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class PaymentCorrelation {
        /** get EStatement request code. */
        public static final String REQUEST_CODE = "PmtAddCorrRq";

        /** get EStatement response code. */
        public static final String RESPONSE_CODE = "PmtAddCorrRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class PaymentAdviceCorrelation {
        /** get PaymentAdviceCorrelation request code. */
        public static final String REQUEST_CODE = "PmtAdvCorrRq";

        /** get PaymentAdviceCorrelation response code. */
        public static final String RESPONSE_CODE = "PmtAdvCorrRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class PaymentValidation {

        /** payment request code. */
        public static final String REQUEST_CODE = "PmtValRq";

        /** payment response code. */
        public static final String RESPONSE_CODE = "PmtValRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class InvalidateCache {

        /** Invalidate cache request code. */
        public static final String REQUEST_CODE = "InvalidateCacheRq";

        /** Invalidate cache response code. */
        public static final String RESPONSE_CODE = "InvalidateCacheRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class Cashout {

        /** Invalidate cache request code. */
        public static final String REQUEST_CODE = "CashoutRq";

        /** Invalidate cache response code. */
        public static final String RESPONSE_CODE = "CashoutRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class PaymentReFire {

        /** Invalidate cache request code. */
        public static final String REQUEST_CODE = "PmtReFireRq";

        /** Invalidate cache response code. */
        public static final String RESPONSE_CODE = "PmtReFireRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class MngInbox {

        /** Invalidate cache request code. */
        public static final String REQUEST_CODE = "MngInboxRq";

        /** Invalidate cache response code. */
        public static final String RESPONSE_CODE = "MngInboxRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class GetInbox {

        /** Invalidate cache request code. */
        public static final String REQUEST_CODE = "GetInboxRq";

        /** Invalidate cache response code. */
        public static final String RESPONSE_CODE = "GetInboxRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class GetAccountTypeScheme {

        /** Invalidate cache request code. */
        public static final String REQUEST_CODE = "GetAcctTypeSchemeRq";

        /** Invalidate cache response code. */
        public static final String RESPONSE_CODE = "GetAcctTypeSchemeRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class GenerateOTP {

        /** Generate one time password request code. */
        public static final String REQUEST_CODE = "GenerateOTPRq";

        /** Generate one time password response code. */
        public static final String RESPONSE_CODE = "GenerateOTPRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class ValidateOTP {

        /** Validate one time password request code. */
        public static final String REQUEST_CODE = "ValidateOTPRq";

        /** Validate one time password response code. */
        public static final String RESPONSE_CODE = "ValidateOTPRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class GetTerminalProfile {

        /** Invalidate cache request code. */
        public static final String REQUEST_CODE = "GetTerminalProfileRq";

        /** Invalidate cache response code. */
        public static final String RESPONSE_CODE = "GetTerminalProfileRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class PaymentNotification {
        /** Payment Center request code. */
        public static final String REQUEST_CODE = "PmtNotifyRq";

        /** Payment Center response code. */
        public static final String RESPONSE_CODE = "PmtNotifyRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class BalanceInquiry {

        /** bill request code. */
        public static final String REQUEST_CODE = "BalanceInqRq";

        /** bill response code. */
        public static final String RESPONSE_CODE = "BalanceInqRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class BalanceSheet {

        /** bill request code. */
        public static final String REQUEST_CODE = "BalanceSheetRq";

        /** bill response code. */
        public static final String RESPONSE_CODE = "BalanceSheetRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class ChangePin {

        /** bill request code. */
        public static final String REQUEST_CODE = "ChangePinRq";

        /** bill response code. */
        public static final String RESPONSE_CODE = "ChangePinRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    /**
     * Bill Upload
     *
     * @author Ayman.Hussien
     */
    public static class BillUpload {
        /** Bill Upload request code. */
        public static final String REQUEST_CODE = "BillUploadRq";

        /** Bill Upload response code. */
        public static final String RESPONSE_CODE = "BillUploadRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class PaymentUpdate {
        /** Payment Update request code. */
        public static final String REQUEST_CODE = "PmtUpdateRq";

        /** Payment Update response code. */
        public static final String RESPONSE_CODE = "PmtUpdateRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class KeyExchange {
        /** key Exchange request code. */
        public static final String REQUEST_CODE = "KeyExchangeRq";

        /** key Exchange response code. */
        public static final String RESPONSE_CODE = "KeyExchangeRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class TerminalBatchClose {
        /** BatchClose request code. */
        public static final String REQUEST_CODE = "DebitBatchCloseRq";

        /** BatchClose response code. */
        public static final String RESPONSE_CODE = "DebitBatchCloseRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class TerminalBatchUpload {
        /** BatchUpload request code. */
        public static final String DEBIT_REQUEST_CODE = "DebitBatchUploadRq";

        /** BatchUpload response code. */
        public static final String DEBIT_RESPONSE_CODE = "DebitBatchUploadRs";

        /** BatchUpload request code. */
        public static final String REFUND_REQUEST_CODE = "RefundBatchUploadRq";

        /** BatchUpload response code. */
        public static final String REFUND_RESPONSE_CODE = "RefundBatchUploadRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class Authenticate {
        /** key Exchange request code. */
        public static final String REQUEST_CODE = "AuthenticateRq";

        /** key Exchange response code. */
        public static final String RESPONSE_CODE = "AuthenticateRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class PaymentAuthorization {
        /** Payment Authorization request code. */
        public static final String REQUEST_CODE = "PmtAuthRq";

        /** Payment Authorization response code. */
        public static final String RESPONSE_CODE = "PmtAuthRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class PaymentNotify {
        /** Payment Notify request code. */
        public static final String REQUEST_CODE = "PmtNotifyRq";

        /** Payment Notify response code. */
        public static final String RESPONSE_CODE = "PmtNotifyRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class ManageLookup {

        /** manage Lookup request code. */
        public static final String REQUEST_CODE = "MngLookupRq";

        /** manage Lookup response code. */
        public static final String RESPONSE_CODE = "MngLookupRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";

    }

    public static class GetDashboard {

        /** Get DashBoard request code. */
        public static final String REQUEST_CODE = "GetDashboardRq";

        /** Get DashBoard response code. */
        public static final String RESPONSE_CODE = "GetDashboardRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";

    }

    public static class PromoValidation {

        /** Get DashBoard request code. */
        public static final String REQUEST_CODE = "PromoValRq";

        /** Get DashBoard response code. */
        public static final String RESPONSE_CODE = "PromoValRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";

    }

    public static class PaymentRefund {

        public static final String REQUEST_CODE = "PmtRefundRq";
        public static final String RESPONSE_CODE = "PmtRefundRs";
        public static final String VERSION_1 = "V1.0";

    }

    public static class PaymentRefundAdvice {

        public static final String REQUEST_CODE = "PmtRefundAdvRq";
        public static final String RESPONSE_CODE = "PmtRefundAdvRs";
        public static final String VERSION_1 = "V1.0";

    }

    public static class DebitRefund {
        /** payment debit refund request code. */
        public static final String REQUEST_CODE = "DebitRefundRq";

        /** payment debit refund response code. */
        public static final String RESPONSE_CODE = "DebitRefundRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class InvalidateBillersCache {

        /** Invalidate billers cache request code. */
        public static final String REQUEST_CODE = "InvalidateBillersCacheRq";

        /** Invalidate billers cache response code. */
        public static final String RESPONSE_CODE = "InvalidateBillersCacheRs";

        /** version number. */
        public static final String VERSION_1 = "V1.0";
    }

    public static class FeesInquiry {

        public static final String REQUEST_CODE = "FeesInqRq";
        public static final String RESPONSE_CODE = "FeesInqRs";
        public static final String VERSION_1 = "V1.0";

    }

    public static class ManageTicket {

        public static final String REQUEST_CODE = "ManageTicketRq";
        public static final String RESPONSE_CODE = "ManageTicketRs";
        public static final String VERSION_1 = "V1.0";

    }

    public static class CreateLYA {

        public static final String REQUEST_CODE = "MngAcctRq";
        public static final String RESPONSE_CODE = "MngAcctRs";
        public static final String VERSION_1 = "V1.0";

    }
    public static class ManageLYA {

        public static final String REQUEST_CODE = "MngCustRq";
        public static final String RESPONSE_CODE = "MngCustRs";
        public static final String VERSION_1 = "V1.0";

    }
}

