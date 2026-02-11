package com.fawry.adapter_template.adapterFramework.adapter.Impl;

import com.fawry.adapter_template.adapterFramework.adapter.IAdapter;
import com.fawry.adapter_template.adapterFramework.connector.IAdapterConnector;
import com.fawry.adapter_template.adapterFramework.transformer.IAdapterTransformer;


import com.fawry.adapter_template.model.fawryType.FAWRYType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class Adapter implements IAdapter {
    private IAdapterTransformer transformer;
    private IAdapterConnector connector;

    @Override
    public FAWRYType execute(FAWRYType request, FAWRYType response)  {
        Object transformRequest = transformer.transformRequest(request, response);
        Object responseObj = connector.sendReceive(transformRequest);
        return transformer.transformResponse(responseObj, request);
    }
}
