package com.eaglerobotics.reconalpha.frcAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class FRCRankings {

    private List<Ranking> Rankings = new ArrayList<Ranking>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The Rankings
     */
    public List<Ranking> getRankings() {
        return Rankings;
    }

    /**
     *
     * @param Rankings
     * The Rankings
     */
    public void setRankings(List<Ranking> Rankings) {
        this.Rankings = Rankings;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}