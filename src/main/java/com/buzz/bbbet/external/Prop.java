package com.buzz.bbbet.external;

public class Prop {
    private String id;

    private String parentId;
    private String propValue;
    private Integer odds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public Integer getOdds() {
        return odds;
    }

    public void setOdds(Integer odds) {
        this.odds = odds;
    }
}
