package com.buzz.bbbet.external;

import java.util.List;

public class PropQueryDto {
    private List<String> propIds;

    public PropQueryDto() { }

    public PropQueryDto(List<String> propIds) {
        this.propIds = propIds;
    }

    public List<String> getPropIds() {
        return propIds;
    }

    public void setPropIds(List<String> propIds) {
        this.propIds = propIds;
    }
}
