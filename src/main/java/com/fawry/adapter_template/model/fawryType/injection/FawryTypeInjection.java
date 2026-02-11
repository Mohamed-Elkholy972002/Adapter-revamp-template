package com.fawry.adapter_template.model.fawryType.injection;

import com.fawry.adapter_template.model.fawryType.CustomPropertyType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FawryTypeInjection {
    public void injectCustomProperty(List<CustomPropertyType> list, String key, String value) {
        CustomPropertyType property = new CustomPropertyType();
        property.setKey(key);
        property.setValue(value);
        list.add(property);
    }


}
