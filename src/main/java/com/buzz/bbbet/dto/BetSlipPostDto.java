package com.buzz.bbbet.dto;

public class BetSlipPostDto {
    private String propId;  // FK into props, for example BAE Qual Match 10 Red Alliance Spread
    private String propValue;   // for example, +7.5
    private String propOdds;    // for example, -110

    public BetSlipPostDto() {}

    public BetSlipPostDto(String propId, String propValue, String propOdds) {
        this.propId = propId;
        this.propValue = propValue;
        this.propOdds = propOdds;
    }

    public String getPropId() {
        return propId;
    }

    public void setPropId(String propId) {
        this.propId = propId;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public String getPropOdds() {
        return propOdds;
    }

    public void setPropOdds(String propOdds) {
        this.propOdds = propOdds;
    }
}
