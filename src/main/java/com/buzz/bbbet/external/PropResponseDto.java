package com.buzz.bbbet.external;

import java.util.List;

public class PropResponseDto {
    private List<Prop> props;
    private List<String> missingIds;

    public PropResponseDto() {
    }

    public PropResponseDto(List<Prop> props, List<String> missingIds) {
        this.props = props;
        this.missingIds = missingIds;
    }

    public List<Prop> getProps() {
        return props;
    }

    public void setProps(List<Prop> props) {
        this.props = props;
    }

    public List<String> getMissingIds() {
        return missingIds;
    }

    public void setMissingIds(List<String> missingIds) {
        this.missingIds = missingIds;
    }
}
