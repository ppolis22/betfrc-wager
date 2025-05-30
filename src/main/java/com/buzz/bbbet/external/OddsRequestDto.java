package com.buzz.bbbet.external;

import com.buzz.bbbet.dto.WagerLeg;

import java.util.List;

public class OddsRequestDto {
    private String propId;
    private String value;

    public OddsRequestDto() {}

    public OddsRequestDto(String propId, String value) {
        this.propId = propId;
        this.value = value;
    }

    public String getPropId() {
        return propId;
    }

    public void setPropId(String propId) {
        this.propId = propId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
