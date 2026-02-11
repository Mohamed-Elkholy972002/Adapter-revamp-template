package com.fawry.adapter_template.service.Impl;

import com.fawry.adapter_template.adapterFramework.adapter.IAdapter;
import com.fawry.adapter_template.adapterFramework.adapter.factory.AdapterFactory;
import com.fawry.adapter_template.model.fawryType.FAWRYType;
import com.fawry.adapter_template.model.configuration.adapter.AdapterConfig;
import com.fawry.adapter_template.model.configuration.adapter.AdaptersConfigs;
import com.fawry.adapter_template.service.IAdapterExecutor;
import com.fawry.adapter_template.util.RequestContext.RequestContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@RequiredArgsConstructor
@Service
public class AdapterExecutor implements IAdapterExecutor {
    private final AdapterFactory adapterFactory;
    private final AdaptersConfigs configs;


    @Override
    public FAWRYType executeFramework(FAWRYType request) {
        String messageCode = extractMessageCodeRq(request);

        RequestContextUtil.setMessageCode(messageCode);

        return executeAdapters(selectAdapters(messageCode), request);
    }

    private String extractMessageCodeRq(FAWRYType request) {
        return request.getRequest().getSignonRq().getSignonProfile().getMsgCode();
    }

    private List<IAdapter> selectAdapters(String messageCode) {
        List<AdapterConfig> adapterConfigs = configs.getConfigs().get(messageCode);

        List<IAdapter> adapters = new ArrayList<>();

        for (AdapterConfig adapterConfig : adapterConfigs) {
            adapters.add(adapterFactory.createAdapter(adapterConfig.getTransformer(), adapterConfig.getConnector()));
        }
        return adapters;
    }

    private FAWRYType executeAdapters(List<IAdapter> adapters, FAWRYType request) {
        FAWRYType response = null;
        for (IAdapter adapter : adapters) {
            response = adapter.execute(request, response);
        }
        return response;
    }


}
