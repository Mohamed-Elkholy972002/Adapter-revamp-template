package com.fawry.adapter_template.util.RequestContext;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class RequestContextUtil {
    private static final String MESSAGE_CODE = "MESSAGE_CODE";
    private static final String BILLTYPE_CODE = "BILLTYPE_CODE";
    private static final String LOCATION_LNG = "LOCATION_LNG";
    private static final String LOCATION_LAT = "LOCATION_LAT";

    public static void setBillTypeCode(String billTypeCode) {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        attributes.setAttribute(BILLTYPE_CODE, billTypeCode, RequestAttributes.SCOPE_REQUEST);
    }

    public static String getBillTypeCode() {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        return (String) attributes.getAttribute(BILLTYPE_CODE, RequestAttributes.SCOPE_REQUEST);
    }
    
    public static void setMessageCode(String messageCode) {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        attributes.setAttribute(MESSAGE_CODE, messageCode, RequestAttributes.SCOPE_REQUEST);
    }

    public static String getMessageCode() {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        return (String) attributes.getAttribute(MESSAGE_CODE, RequestAttributes.SCOPE_REQUEST);
    }

    public static void clearMessageCode() {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        attributes.removeAttribute(MESSAGE_CODE, RequestAttributes.SCOPE_REQUEST);
    }

    public static void setLocationLng(String locationLng) {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        attributes.setAttribute(LOCATION_LNG, locationLng, RequestAttributes.SCOPE_REQUEST);
    }

    public static String getLocationLng() {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        String value = (String) attributes.getAttribute(LOCATION_LNG, RequestAttributes.SCOPE_REQUEST);
        return value != null ? value : "0";
    }

    public static void setLocationLat(String locationLat) {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        attributes.setAttribute(LOCATION_LAT, locationLat, RequestAttributes.SCOPE_REQUEST);
    }

    public static String getLocationLat() {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        String value = (String) attributes.getAttribute(LOCATION_LAT, RequestAttributes.SCOPE_REQUEST);
        return value != null ? value : "0";
    }
}
