package com.fawry.adapter_template.adapterFramework.adapter;
import com.fawry.adapter_template.model.fawryType.FAWRYType;

public interface IAdapter {

    FAWRYType execute(FAWRYType request, FAWRYType response);
}
