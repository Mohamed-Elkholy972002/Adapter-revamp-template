package com.fawry.adapter_template.adapterFramework.transformer;

import com.fawry.adapter_template.model.fawryType.FAWRYType;

public interface IAdapterTransformer {
     Object transformRequest(FAWRYType request , FAWRYType response);

     FAWRYType transformResponse(Object response, FAWRYType request);

}
