package com.fawry.adapter_template.util.adapterUtils;

import com.fawry.adapter_template.model.fawryType.*;
import com.fawry.adapter_template.util.RequestContext.RequestContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * Service component for transforming FAWRY request/response objects.
 * Handles various transformation operations for bank services, debit operations, and reverse operations.
 */
@Slf4j
@Component
public class AdapterTransformerHelper {

    private static final String DEFAULT_NON_REQ_ID = "f0000001-0000-0000-c002-10000000004b";
    private static final String DEFAULT_LANGUAGE = "ar-eg";
    private final DateMapper dateMapper;


    public AdapterTransformerHelper(DateMapper dateMapper) {
        this.dateMapper = dateMapper;
    }


     public void saveBtcAtContextUtil(DebitAddRqType debitAddRqType) {
        List<DebitInfoType> debitInfos = debitAddRqType.getDebitInfo();
        if (debitInfos != null && !debitInfos.isEmpty()) {
            DebitInfoType debitInfo = debitInfos.get(0);
            if (debitInfo.getBillTypeCode() != null) {
                RequestContextUtil.setBillTypeCode(String.valueOf(debitInfo.getBillTypeCode()));
            }
        }
    }

    /**
     * Transforms response for bank service operations.
     *
     * @param originalResponse The original response to transform
     * @param originalRequest The original request for context
     * @return Transformed FAWRY response
     */
    public FAWRYType transformResponseForBank(FAWRYType originalResponse, FAWRYType originalRequest) {
        log.debug("Transforming response for bank service. Request: {}, Response: {}",
                 originalRequest, originalResponse);

        validateInputs(originalResponse, originalRequest);

        boolean isEchoEnabled = isEchoEnabled(originalRequest.getRequest().getSignonRq());
        BankSvcRsType bankSvcRs = originalResponse.getResponse().getBankSvcRs();

        setAsyncRqUID(originalRequest, bankSvcRs);
        setRequestHeaders(originalRequest, bankSvcRs, isEchoEnabled);
        setRequestUID(originalRequest, bankSvcRs);

        originalResponse.getResponse().setBankSvcRs(bankSvcRs);

        log.debug("Bank service response transformation completed: {}", originalResponse);
        return originalResponse;
    }

    /**
     * Checks if echo is enabled based on signon request.
     *
     * @param signonRq The signon request
     * @return true if echo is enabled, false otherwise
     */
    public boolean isEchoEnabled(SignonRqType signonRq) {
        log.debug("Checking echo enabled status for signon request: {}", signonRq);

        boolean result = !Boolean.TRUE.equals(signonRq.isSuppressEcho());

        log.debug("Echo enabled status: {}", result);
        return result;
    }


    /**
     * Creates a dummy response for error scenarios that echoes back request elements.
     *
     * @param request The original request
     * @param statusCode The status code
     * @param statusDescription The status description
     * @return Dummy FAWRY response with echoed elements
     */
    public FAWRYType createDummyResponse(FAWRYType request, long statusCode, String statusDescription) {
        log.debug("Creating dummy response with status code: {} and description: {}", 
                 statusCode, statusDescription);
        
        FAWRYType fawryType = new FAWRYType();
        ResponseType response = new ResponseType();
        StatusType status = createStatus(statusCode, statusDescription);
        
        fillSignonElements(request.getRequest(), response);
        fillEchoedElements(response, request.getRequest());
        
        // Echo service-specific elements based on request type
        echoServiceSpecificElements(request, response, status);
        
        fawryType.setResponse(response);
        return fawryType;
    }

    /**
     * Fills signon elements in the response.
     *
     * @param ifxRequest The request
     * @param ifxResponse The response to fill
     */
    public void fillSignonElements(RequestType ifxRequest, ResponseType ifxResponse) {
        log.debug("Filling signon elements for request: {}", ifxRequest);

        String custLangPref = ifxRequest.getSignonRq().getCustLangPref();
        ifxResponse.setSignonRs(new SignonRsType());

        setServerDateTime(ifxResponse);
        setLanguagePreferences(ifxResponse, custLangPref);
        setClientDateTime(ifxResponse, ifxRequest);
        setSignonProfile(ifxResponse, ifxRequest);

        setServiceSpecificElements(ifxRequest, ifxResponse);
    }

    /**
     * Fills echoed elements in the response.
     *
     * @param ifxResponse The response
     * @param ifxRequest The request
     */
    public void fillEchoedElements(ResponseType ifxResponse, RequestType ifxRequest) {
        log.debug("Filling echoed elements");

        ifxResponse.getSignonRs().setClientDt(ifxRequest.getSignonRq().getClientDt());
        ifxResponse.getSignonRs().setCustLangPref(
            StringUtils.hasText(ifxRequest.getSignonRq().getCustLangPref())
                ? ifxRequest.getSignonRq().getCustLangPref()
                : DEFAULT_LANGUAGE
        );

        if (ifxResponse.getBankSvcRs() != null) {
            ifxResponse.getBankSvcRs().setMsgRqHdr(ifxRequest.getBankSvcRq().getMsgRqHdr());
        }
    }

    // Private helper methods

    private void validateInputs(FAWRYType originalResponse, FAWRYType originalRequest) {
        if (originalResponse == null || originalRequest == null) {
            throw new IllegalArgumentException("Original response and request cannot be null");
        }
        if (originalResponse.getResponse() == null || originalRequest.getRequest() == null) {
            throw new IllegalArgumentException("Response and request objects cannot be null");
        }
    }

    private void setAsyncRqUID(FAWRYType originalRequest, BankSvcRsType bankSvcRs) {
        Optional.ofNullable(originalRequest.getRequest().getPaySvcRq())
                .map(PaySvcRqType::getAsyncRqUID)
                .filter(StringUtils::hasText)
                .ifPresentOrElse(
                    bankSvcRs::setAsyncRqUID,
                    () -> bankSvcRs.setAsyncRqUID(DEFAULT_NON_REQ_ID)
                );
    }

    private void setRequestHeaders(FAWRYType originalRequest, BankSvcRsType bankSvcRs, boolean isEchoEnabled) {
        Optional.ofNullable(originalRequest.getRequest().getPaySvcRq())
                .filter(paySvcRq -> isEchoEnabled)
                .map(PaySvcRqType::getMsgRqHdr)
                .ifPresent(bankSvcRs::setMsgRqHdr);
    }

    private void setRequestUID(FAWRYType originalRequest, BankSvcRsType bankSvcRs) {
        Optional.ofNullable(originalRequest.getRequest().getPaySvcRq())
                .map(PaySvcRqType::getRqUID)
                .filter(StringUtils::hasText)
                .ifPresentOrElse(
                    bankSvcRs::setRqUID,
                    () -> bankSvcRs.setRqUID(DEFAULT_NON_REQ_ID)
                );
    }


    private StatusType createStatus(long statusCode, String statusDescription) {
        StatusType status = new StatusType();
        status.setSeverity(statusCode != 200 ? SeverityEnum.ERROR : SeverityEnum.INFO);
        status.setStatusCode(statusCode);
        status.setStatusDesc(statusDescription);
        return status;
    }

    private void setServerDateTime(ResponseType ifxResponse) {
        ifxResponse.getSignonRs().setServerDt(
            dateMapper.calendarToXmlGregorianCalendar(Calendar.getInstance())
        );
    }

    private void setLanguagePreferences(ResponseType ifxResponse, String custLangPref) {
        String language = StringUtils.hasText(custLangPref) ? custLangPref : DEFAULT_LANGUAGE;
        ifxResponse.getSignonRs().setLanguage(language);
        ifxResponse.getSignonRs().setCustLangPref(custLangPref);
    }

    private void setClientDateTime(ResponseType ifxResponse, RequestType ifxRequest) {
        ifxResponse.getSignonRs().setClientDt(ifxRequest.getSignonRq().getClientDt());
    }

    private void setSignonProfile(ResponseType ifxResponse, RequestType ifxRequest) {
        SignonProfileType signonProfile = new SignonProfileType();
        SignonProfileType requestProfile = ifxRequest.getSignonRq().getSignonProfile();

        signonProfile.setSender(requestProfile.getReceiver());
        signonProfile.setReceiver(requestProfile.getSender());
        signonProfile.setVersion(requestProfile.getVersion());

        ifxResponse.getSignonRs().setSignonProfile(signonProfile);
    }

    private void setServiceSpecificElements(RequestType ifxRequest, ResponseType ifxResponse) {
        if (ifxRequest.getRegSvcRq() != null) {
            setRegistrationServiceElements(ifxRequest, ifxResponse);
        }

        if (ifxRequest.getPaySvcRq() != null) {
            setPaymentServiceElements(ifxRequest, ifxResponse);
        }

        if (ifxRequest.getBankSvcRq() != null) {
            setBankServiceElements(ifxRequest, ifxResponse);
        }
    }

    private void setRegistrationServiceElements(RequestType ifxRequest, ResponseType ifxResponse) {
        ifxResponse.getSignonRs().getSignonProfile().setMsgCode(Messages.ManageAccount.RESPONSE_CODE);
        ifxResponse.setRegSvcRs(new RegSvcRsType());

        RegSvcRsType regSvcRs = ifxResponse.getRegSvcRs();
        RegSvcRqType regSvcRq = ifxRequest.getRegSvcRq();

        regSvcRs.setMsgRqHdr(regSvcRq.getMsgRqHdr());
        regSvcRs.setRqUID(regSvcRq.getRqUID());
        regSvcRs.setAsyncRqUID(regSvcRq.getAsyncRqUID());
    }

    private void setPaymentServiceElements(RequestType ifxRequest, ResponseType ifxResponse) {
        ifxResponse.getSignonRs().getSignonProfile().setMsgCode(Messages.PaymentNotification.RESPONSE_CODE);
        ifxResponse.setPaySvcRs(new PaySvcRsType());

        PaySvcRsType paySvcRs = ifxResponse.getPaySvcRs();
        PaySvcRqType paySvcRq = ifxRequest.getPaySvcRq();

        paySvcRs.setMsgRqHdr(paySvcRq.getMsgRqHdr());
        paySvcRs.setRqUID(paySvcRq.getRqUID());
        paySvcRs.setAsyncRqUID(paySvcRq.getAsyncRqUID());
    }

    private void setBankServiceElements(RequestType ifxRequest, ResponseType ifxResponse) {
        String msgCode = determineBankServiceMessageCode(ifxRequest);
        ifxResponse.getSignonRs().getSignonProfile().setMsgCode(msgCode);

        ifxResponse.setBankSvcRs(new BankSvcRsType());

        BankSvcRsType bankSvcRs = ifxResponse.getBankSvcRs();
        BankSvcRqType bankSvcRq = ifxRequest.getBankSvcRq();

        bankSvcRs.setMsgRqHdr(bankSvcRq.getMsgRqHdr());
        bankSvcRs.setRqUID(bankSvcRq.getRqUID());
        bankSvcRs.setAsyncRqUID(bankSvcRq.getAsyncRqUID());
    }

    private String determineBankServiceMessageCode(RequestType ifxRequest) {
        String msgCode = ifxRequest.getSignonRq().getSignonProfile().getMsgCode();

        if (ifxRequest.getBankSvcRq().getDebitAddRq() != null) {
            if ("DebitAddRq".equalsIgnoreCase(msgCode)) {
                return Messages.DebitRq.RESPONSE_CODE;
            } else if ("DebitRefundRq".equalsIgnoreCase(msgCode)) {
                return Messages.DebitRefund.RESPONSE_CODE;
            }
        }

        return Messages.DebitReverse.RESPONSE_CODE;
    }

    /**
     * Echoes service-specific elements from request to response.
     *
     * @param request The original request
     * @param response The response to fill
     * @param status The status to set
     */
    private void echoServiceSpecificElements(FAWRYType request, ResponseType response, StatusType status) {
        RequestType ifxRequest = request.getRequest();
        
        // Handle PresSvcRq (Presentation Service Request)
        if (ifxRequest.getPresSvcRq() != null) {
            echoPresSvcElements(ifxRequest, response, status);
        }
        
        // Handle BankSvcRq (Bank Service Request)
        if (ifxRequest.getBankSvcRq() != null) {
            echoBankSvcElements(ifxRequest, response, status);
        }
        
        // Handle PaySvcRq (Payment Service Request)
        if (ifxRequest.getPaySvcRq() != null) {
            echoPaySvcElements(ifxRequest, response, status);
        }
        
        // Handle RegSvcRq (Registration Service Request)
        if (ifxRequest.getRegSvcRq() != null) {
            echoRegSvcElements(ifxRequest, response, status);
        }
    }

    /**
     * Echoes presentation service elements from request to response.
     */
    private void echoPresSvcElements(RequestType ifxRequest, ResponseType response, StatusType status) {
        PresSvcRqType presSvcRq = ifxRequest.getPresSvcRq();
        PresSvcRsType presSvcRs = new PresSvcRsType();
        
        // Echo common elements
        presSvcRs.setRqUID(presSvcRq.getRqUID());
        presSvcRs.setAsyncRqUID(presSvcRq.getAsyncRqUID());
        presSvcRs.setMsgRqHdr(presSvcRq.getMsgRqHdr());
        
        // Echo specific request elements based on type
        if (presSvcRq.getBalanceInqRq() != null) {
            echoBalanceInquiryElements(presSvcRq, presSvcRs);
        }
        
        if (presSvcRq.getBillInqRq() != null) {
            echoBillInquiryElements(presSvcRq, presSvcRs);
        }
        
        if (presSvcRq.getBillerInqRq() != null) {
            echoBillerInquiryElements(presSvcRq, presSvcRs);
        }
        
        // Set status and add to response
        presSvcRs.setStatus(status);
        response.setPresSvcRs(presSvcRs);
    }

    /**
     * Echoes bank service elements from request to response.
     */
    private void echoBankSvcElements(RequestType ifxRequest, ResponseType response, StatusType status) {
        BankSvcRqType bankSvcRq = ifxRequest.getBankSvcRq();
        BankSvcRsType bankSvcRs = new BankSvcRsType();
        
        // Echo common elements
        bankSvcRs.setRqUID(bankSvcRq.getRqUID());
        bankSvcRs.setAsyncRqUID(bankSvcRq.getAsyncRqUID());
        bankSvcRs.setMsgRqHdr(bankSvcRq.getMsgRqHdr());
        
        // Echo specific request elements
        if (bankSvcRq.getDebitAddRq() != null) {
            echoDebitAddElements(bankSvcRq, bankSvcRs);
        }
        
        if (bankSvcRq.getDebitRevRq() != null) {
            echoDebitRevElements(bankSvcRq, bankSvcRs);
        }
        
        // Set status and add to response
        bankSvcRs.setStatus(status);
        response.setBankSvcRs(bankSvcRs);
    }

    /**
     * Echoes payment service elements from request to response.
     */
    private void echoPaySvcElements(RequestType ifxRequest, ResponseType response, StatusType status) {
        PaySvcRqType paySvcRq = ifxRequest.getPaySvcRq();
        PaySvcRsType paySvcRs = new PaySvcRsType();
        
        // Echo common elements
        paySvcRs.setRqUID(paySvcRq.getRqUID());
        paySvcRs.setAsyncRqUID(paySvcRq.getAsyncRqUID());
        paySvcRs.setMsgRqHdr(paySvcRq.getMsgRqHdr());
        
        // Echo specific payment elements if they exist
        if (paySvcRq.getPmtAddRq() != null) {
            echoPaymentAddElements(paySvcRq, paySvcRs);
        }
        
        if (paySvcRq.getPmtAdvRq() != null) {
            echoPaymentAdviceElements(paySvcRq, paySvcRs);
        }
        
        if (paySvcRq.getPmtNotifyRq() != null) {
            echoPaymentNotificationElements(paySvcRq, paySvcRs);
        }
        
        // Set status and add to response
        paySvcRs.setStatus(status);
        response.setPaySvcRs(paySvcRs);
    }

    /**
     * Echoes registration service elements from request to response.
     */
    private void echoRegSvcElements(RequestType ifxRequest, ResponseType response, StatusType status) {
        RegSvcRqType regSvcRq = ifxRequest.getRegSvcRq();
        RegSvcRsType regSvcRs = new RegSvcRsType();
        
        // Echo common elements
        regSvcRs.setRqUID(regSvcRq.getRqUID());
        regSvcRs.setAsyncRqUID(regSvcRq.getAsyncRqUID());
        regSvcRs.setMsgRqHdr(regSvcRq.getMsgRqHdr());
        
        // Echo customer data if present
        if (regSvcRq.getCustData() != null) {
            regSvcRs.setCustData(regSvcRq.getCustData());
        }
        
        // Set status and add to response
        regSvcRs.setStatus(status);
        response.setRegSvcRs(regSvcRs);
    }

    /**
     * Echoes balance inquiry elements from request to response.
     */
    private void echoBalanceInquiryElements(PresSvcRqType presSvcRq, PresSvcRsType presSvcRs) {
        BalanceInqRqType balanceInqRq = presSvcRq.getBalanceInqRq();
        BalanceInqRsType balanceInqRs = new BalanceInqRsType();
        
        // Echo account information
        if (balanceInqRq.getAcctInfo() != null) {
            balanceInqRs.getAcctInfo().add(balanceInqRq.getAcctInfo());
        }
        
        // Echo other elements
        balanceInqRs.setCSP(balanceInqRq.getCSP());
        balanceInqRs.setIncludeSubAccts(balanceInqRq.isIncludeSubAccts());
        balanceInqRs.setInquiredAcctInfo(balanceInqRq.getInquiredAcctInfo());
        balanceInqRs.setIncludeFinanceProgram(balanceInqRq.isIncludeFinanceProgram());
        
        presSvcRs.setBalanceInqRs(balanceInqRs);
    }

    /**
     * Echoes bill inquiry elements from request to response.
     */
    private void echoBillInquiryElements(PresSvcRqType presSvcRq, PresSvcRsType presSvcRs) {
        BillInqRqType billInqRq = presSvcRq.getBillInqRq();
        BillInqRsType billInqRs = new BillInqRsType();
        
        // Echo customer IDs
        if (!billInqRq.getCustId().isEmpty()) {
            billInqRs.getCustId().addAll(billInqRq.getCustId());
        }
        
        // Echo other elements that exist in both request and response
        billInqRs.setRecCtrlOut(billInqRq.getRecCtrlIn() != null ? new RecCtrlOutType() : null);
        billInqRs.setSelRangeDt(billInqRq.getSelRangeDt());
        billInqRs.setIncOpenAmt(billInqRq.isIncOpenAmt());
        billInqRs.setPmtType(billInqRq.getPmtType());
        billInqRs.setServiceType(billInqRq.getServiceType());
        billInqRs.setDeliveryMethod(billInqRq.getDeliveryMethod());
        billInqRs.setProfileCode(billInqRq.getProfileCode());
        
        presSvcRs.setBillInqRs(billInqRs);
    }

    /**
     * Echoes biller inquiry elements from request to response.
     */
    private void echoBillerInquiryElements(PresSvcRqType presSvcRq, PresSvcRsType presSvcRs) {
        BillerInqRqType billerInqRq = presSvcRq.getBillerInqRq();
        BillerInqRsType billerInqRs = new BillerInqRsType();
        
        // Echo customer IDs
        if (!billerInqRq.getCustId().isEmpty()) {
            billerInqRs.getCustId().addAll(billerInqRq.getCustId());
        }
        
        // Echo other elements that exist in both request and response
        billerInqRs.setRecCtrlOut(billerInqRq.getRecCtrlIn() != null ? new RecCtrlOutType() : null);
        billerInqRs.setPmtType(billerInqRq.getPmtType());
        billerInqRs.setServiceType(billerInqRq.getServiceType());
        billerInqRs.setDeliveryMethod(billerInqRq.getDeliveryMethod());
        billerInqRs.setProfileCode(billerInqRq.getProfileCode());
        
        presSvcRs.setBillerInqRs(billerInqRs);
    }

    /**
     * Echoes debit add elements from request to response.
     */
    private void echoDebitAddElements(BankSvcRqType bankSvcRq, BankSvcRsType bankSvcRs) {
        DebitAddRqType debitAddRq = bankSvcRq.getDebitAddRq();
        DebitAddRsType debitAddRs = new DebitAddRsType();
        
        // Echo customer IDs
        if (!debitAddRq.getCustId().isEmpty()) {
            debitAddRs.getCustId().addAll(debitAddRq.getCustId());
        }
        
        // Echo debit information
        if (!debitAddRq.getDebitInfo().isEmpty()) {
            debitAddRs.getDebitInfo().addAll(debitAddRq.getDebitInfo());
        }
        
        bankSvcRs.setDebitAddRs(debitAddRs);
    }

    /**
     * Echoes debit reverse elements from request to response.
     */
    private void echoDebitRevElements(BankSvcRqType bankSvcRq, BankSvcRsType bankSvcRs) {
        DebitRevRqType debitRevRq = bankSvcRq.getDebitRevRq();
        DebitRevRsType debitRevRs = new DebitRevRsType();
        
        // Echo customer IDs
        if (!debitRevRq.getCustId().isEmpty()) {
            debitRevRs.getCustId().addAll(debitRevRq.getCustId());
        }
        
        // Echo reverse reason code
        debitRevRs.setRevReasonCode(debitRevRq.getRevReasonCode());
        
        // Echo debit message request info if present
        if (debitRevRq.getDebitMsgRqInfo() != null) {
            debitRevRs.setDebitMsgRqInfo(debitRevRq.getDebitMsgRqInfo());
        }
        
        bankSvcRs.setDebitRevRs(debitRevRs);
    }

    /**
     * Echoes payment add elements from request to response.
     */
    private void echoPaymentAddElements(PaySvcRqType paySvcRq, PaySvcRsType paySvcRs) {
        PmtAddRqType pmtAddRq = paySvcRq.getPmtAddRq();
        PmtAddRsType pmtAddRs = new PmtAddRsType();
        
        // Echo customer IDs
        if (!pmtAddRq.getCustId().isEmpty()) {
            pmtAddRs.getCustId().addAll(pmtAddRq.getCustId());
        }
        
        // Note: PmtAddRqType doesn't have getDiscountInfo() method
        // PmtAddRsType has PmtInfoVal (not PmtInfo), so we can't directly echo
        // The response structure is different from the request structure
        
        paySvcRs.setPmtAddRs(pmtAddRs);
    }

    /**
     * Echoes payment advice elements from request to response.
     */
    private void echoPaymentAdviceElements(PaySvcRqType paySvcRq, PaySvcRsType paySvcRs) {
        PmtAdvRqType pmtAdvRq = paySvcRq.getPmtAdvRq();
        PmtAdvRsType pmtAdvRs = new PmtAdvRsType();
        
        // Echo customer IDs
        if (!pmtAdvRq.getCustId().isEmpty()) {
            pmtAdvRs.getCustId().addAll(pmtAdvRq.getCustId());
        }
        
        // Echo discount info if present (PmtAdvRqType does have getDiscountInfo())
        if (pmtAdvRq.getDiscountInfo() != null) {
            pmtAdvRs.setDiscountInfo(pmtAdvRq.getDiscountInfo());
        }
        
        // Note: PmtAdvRsType has PmtStatusRec (not PmtRec), so we can't directly echo
        // The response structure is different from the request structure
        
        paySvcRs.setPmtAdvRs(pmtAdvRs);
    }

    /**
     * Echoes payment notification elements from request to response.
     */
    private void echoPaymentNotificationElements(PaySvcRqType paySvcRq, PaySvcRsType paySvcRs) {
        PmtNotifyRqType pmtNotifyRq = paySvcRq.getPmtNotifyRq();
        PmtNotifyRsType pmtNotifyRs = new PmtNotifyRsType();
        
        // Echo CustId elements if present
        if (pmtNotifyRq.getCustId() != null && !pmtNotifyRq.getCustId().isEmpty()) {
            pmtNotifyRs.getCustId().addAll(pmtNotifyRq.getCustId());
        }
        
        // Echo DiscountInfo if present
        if (pmtNotifyRq.getDiscountInfo() != null) {
            pmtNotifyRs.setDiscountInfo(pmtNotifyRq.getDiscountInfo());
        }
        
        // Echo PmtRec elements as PmtStatusRec elements
        if (pmtNotifyRq.getPmtRec() != null && !pmtNotifyRq.getPmtRec().isEmpty()) {
            for (PmtRecType pmtRec : pmtNotifyRq.getPmtRec()) {
                PmtStatusRecType pmtStatusRec = new PmtStatusRecType();
                
                // Echo PmtTransId elements
                if (pmtRec.getPmtTransId() != null && !pmtRec.getPmtTransId().isEmpty()) {
                    pmtStatusRec.getPmtTransId().addAll(pmtRec.getPmtTransId());
                }
                
                // Add the PmtStatusRec to the response
                pmtNotifyRs.getPmtStatusRec().add(pmtStatusRec);
            }
        }
        
        paySvcRs.setPmtNotifyRs(pmtNotifyRs);
    }
}
