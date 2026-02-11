package com.fawry.adapter_template.model.fawryType.extract;

import com.fawry.adapter_template.model.fawryType.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class FawryTypeExtraction {

    public Optional<String> extractCustomPropertyTypeValue(List<CustomPropertyType> customPropertyTypes, String Key) {
        return customPropertyTypes.stream()
                .filter(customPropertyType -> customPropertyType.getKey().equals(Key))
                .findFirst()
                .map(CustomPropertyType::getValue);
    }


    public Optional<String> extractCustomPropertyTypeValue(List<CustomPropertyType> customPropertyTypes, String... keys) {
        if (keys == null || keys.length == 0) {
            return Optional.empty();
        }
        
        return Arrays.stream(keys)
                .map(key -> extractCustomPropertyTypeValue(customPropertyTypes, key))
                .filter(Optional::isPresent)
                .findFirst()
                .flatMap(opt -> opt);
    }

    public Optional<BigDecimal> extractCompositeCurAmtTypeValue(List<CompositeCurAmtType> compositeCurAmtTypeList, String Key) {
        return compositeCurAmtTypeList.stream()
                .filter(compositeCurAmtType -> compositeCurAmtType.getCompositeCurAmtType().equals(Key))
                .findFirst()
                .map(CompositeCurAmtType::getCurAmt)
                .map(CurAmtType::getAmt);
    }

    public Optional<String> extractPmtTransIdTypeValue(List<PmtTransIdType> pmtTransIdTypesList, PmtldTypeEnum Key) {
        return pmtTransIdTypesList.stream()
                .filter(pmtTransIdType -> pmtTransIdType.getPmtIdType().equals(Key))
                .findFirst()
                .map(PmtTransIdType::getPmtId);
    }

    public Optional<PmtTransIdType> extractPmtIdType(List<PmtTransIdType> pmtTransIdTypesList, PmtldTypeEnum Key) {
        return pmtTransIdTypesList.stream()
                .filter(pmtTransIdType -> pmtTransIdType.getPmtIdType().equals(Key))
                .findFirst();
    }

}
