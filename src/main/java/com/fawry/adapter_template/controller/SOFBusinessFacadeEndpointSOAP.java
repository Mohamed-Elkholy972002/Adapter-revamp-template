package com.fawry.adapter_template.controller;

import com.fawry.adapter_template.model.fawryType.FAWRYType;
import com.fawry.adapter_template.model.fawryType.Process;
import com.fawry.adapter_template.model.fawryType.ProcessResponse;
import com.fawry.adapter_template.service.IAdapterExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Slf4j
@Endpoint
@RequiredArgsConstructor
public class SOFBusinessFacadeEndpointSOAP {
    private final IAdapterExecutor adapterExecutor;

    @PayloadRoot(namespace = "http://ejb.sof.ebpp.fawryis.com/", localPart = "process")
    @ResponsePayload
    public ProcessResponse Process(@RequestPayload Process processRequest)  {

        FAWRYType response = adapterExecutor.executeFramework(processRequest.getArg0());

        ProcessResponse processResponse = new ProcessResponse();

        processResponse.setReturn(response);

        return processResponse;
    }

}