package com.fawry.adapter_template.adapterFramework.adapter.factory;

import com.fawry.adapter_template.adapterFramework.adapter.IAdapter;
import com.fawry.adapter_template.adapterFramework.adapter.Impl.Adapter;
import com.fawry.adapter_template.adapterFramework.connector.IAdapterConnector;
import com.fawry.adapter_template.adapterFramework.transformer.IAdapterTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("adapterFactory")
@RequiredArgsConstructor
public class AdapterFactory {
    private final ApplicationContext applicationContext;

    public IAdapter createAdapter(String transformType, String connectorType) {
        IAdapterTransformer transformer = applicationContext.getBean(transformType, IAdapterTransformer.class);
        IAdapterConnector connector = applicationContext.getBean(connectorType,IAdapterConnector.class);
        return new Adapter(transformer, connector);
    }

}
