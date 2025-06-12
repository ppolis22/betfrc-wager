package com.buzz.betfrcwager.external;

public class PropResolvedEvent {
    private String propId;
    private String value;
    private String state;

    public PropResolvedEvent() {}

    public PropResolvedEvent(String propId, String value, String state) {
        this.propId = propId;
        this.value = value;
        this.state = state;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
