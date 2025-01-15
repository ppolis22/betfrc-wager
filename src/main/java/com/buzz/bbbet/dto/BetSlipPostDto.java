package com.buzz.bbbet.dto;

public class BetSlipPostDto {
    private String propId;  // FK into props, for example BAE Qual Match 10 Red Alliance Spread
    private String propValue;   // for example, +7.5
    private Integer propOdds;    // for example, -110

    public BetSlipPostDto() {}

    public BetSlipPostDto(String propId, String propValue, Integer propOdds) {
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

    public Integer getPropOdds() {
        return propOdds;
    }

    public void setPropOdds(Integer propOdds) {
        this.propOdds = propOdds;
    }
}
