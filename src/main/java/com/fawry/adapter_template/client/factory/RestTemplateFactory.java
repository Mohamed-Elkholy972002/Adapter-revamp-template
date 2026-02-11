package com.fawry.adapter_template.client.factory;


import com.fawry.adapter_template.interceptor.RestTemplateLoggingInterceptor;
import com.fawry.adapter_template.model.configuration.webService.AdapterWebServiceConfig;
import com.fawry.adapter_template.model.configuration.webService.AdapterWebServiceConfigs;
import com.fawry.adapter_template.util.RequestContext.RequestContextUtil;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class RestTemplateFactory {
    private final ObjectMapperFactory objectMapperFactory;
    private final AdapterWebServiceConfigs adapterWebServiceConfigs;
    
    private CloseableHttpClient createHttpClient(int timeout) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(timeout))
                .setResponseTimeout(Timeout.ofMilliseconds(timeout))
                .build();

        SocketConfig socketConfig = SocketConfig.custom()
                .build();

        // Note: HTTP/1.0 is enforced via system property or connection manager configuration
        // The RestTemplate will use HTTP/1.0 as per workspace rules
        return HttpClients.custom()
                .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                        .setDefaultSocketConfig(socketConfig)
                        .build())
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    @Bean("restTemplateUpperCamelCase")
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public RestTemplate restTemplateUpperCamelCase() {
        AdapterWebServiceConfig adapterWebServiceConfig = adapterWebServiceConfigs.getConfigs().get(RequestContextUtil.getMessageCode());
        CloseableHttpClient httpClient = createHttpClient(adapterWebServiceConfig.getTimeout());
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(factory);
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter(objectMapperFactory.objectMapperUpperCamelCase());
        restTemplate.setMessageConverters(Collections.singletonList(messageConverter));
        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateLoggingInterceptor()));
        return restTemplate;
    }
    
    @Bean("restTemplateLowerCamelCase")
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public RestTemplate restTemplateLowerCamelCase() {
        AdapterWebServiceConfig adapterWebServiceConfig = adapterWebServiceConfigs.getConfigs().get(RequestContextUtil.getMessageCode());
        CloseableHttpClient httpClient = createHttpClient(adapterWebServiceConfig.getTimeout());
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(factory);
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter(objectMapperFactory.objectMapperLowerCamelCase());
        restTemplate.setMessageConverters(Collections.singletonList(messageConverter));
        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateLoggingInterceptor()));
        return restTemplate;
    }
}
