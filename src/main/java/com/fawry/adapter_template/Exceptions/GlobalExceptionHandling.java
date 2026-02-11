package com.fawry.adapter_template.Exceptions;

import com.fawry.adapter_template.model.fawryType.FAWRYType;
import com.fawry.adapter_template.model.fawryType.Process;
import com.fawry.adapter_template.model.fawryType.ProcessResponse;
import com.fawry.adapter_template.model.configuration.statusCode.AdapterStatusCodeConfigs;
import com.fawry.adapter_template.util.adapterUtils.AdapterTransformerHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalExceptionHandling {
    private final AdapterStatusCodeConfigs adapterStatusCodeConfigs;
    private final AdapterTransformerHelper adapterTransformerHelper;

    @Around("execution(* com.fawry.adapter.controller.SOFBusinessFacadeEndpointSOAP.Process(..)) && args(processRequest)")
    public ProcessResponse handleProcessException(ProceedingJoinPoint joinPoint, Process processRequest) {
        ProcessResponse processResponse = new ProcessResponse();

        try {
            return (ProcessResponse) joinPoint.proceed();
        } catch (Throwable exception) {
            log.error("print Stack Exception {}", exception);
            FAWRYType response = null;

            if (exception instanceof ResourceAccessException) {
                response = handleException(processRequest.getArg0(), "timeout");

            } else {
                response = handleException(processRequest.getArg0(), "default");
            }

            processResponse.setReturn(response);
        }
        return processResponse;
    }


    private FAWRYType handleException(FAWRYType request, String errorCode) {
        return adapterTransformerHelper.createDummyResponse(
                request,
                Long.parseLong(adapterStatusCodeConfigs.getConfigs().get(errorCode).getFawryCode()),
                adapterStatusCodeConfigs.getConfigs().get(errorCode).getStatusDesc()
        );
    }

}