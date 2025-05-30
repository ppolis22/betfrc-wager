package com.buzz.bbbet.external;

import java.util.List;

public class OddsValidateResponseDto {
    private List<String> staleOdds;
    private List<String> missingOdds;

    public OddsValidateResponseDto() { }

    public OddsValidateResponseDto(List<String> staleOdds, List<String> missingOdds) {
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
