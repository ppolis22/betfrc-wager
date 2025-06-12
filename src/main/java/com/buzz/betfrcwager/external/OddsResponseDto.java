package com.buzz.betfrcwager.external;

import java.util.List;

public class OddsResponseDto {
    private List<String> staleOdds;
    private List<String> missingOdds;

    public OddsResponseDto() { }

    public OddsResponseDto(List<String> staleOdds, List<String> missingOdds) {
        this.staleOdds = staleOdds;
        this.missingOdds = missingOdds;
    }

    public List<String> getStaleOdds() {
        return staleOdds;
    }

    public void setStaleOdds(List<String> staleOdds) {
        this.staleOdds = staleOdds;
    }

    public List<String> getMissingOdds() {
        return missingOdds;
    }

    public void setMissingOdds(List<String> missingOdds) {
        this.missingOdds = missingOdds;
    }
}
