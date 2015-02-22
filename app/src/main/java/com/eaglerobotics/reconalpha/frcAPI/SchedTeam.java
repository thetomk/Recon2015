package com.eaglerobotics.reconalpha.frcAPI;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;


@Generated("org.jsonschema2pojo")
public class SchedTeam {

    private int number;
    private String station;
    private boolean surrogate;
    private boolean dq;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The number
     */
    public int getNumber() {
        return number;
    }

    /**
     *
     * @param number
     * The number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     *
     * @return
     * The station
     */
    public String getStation() {
        return station;
    }

    /**
     *
     * @param station
     * The station
     */
    public void setStation(String station) {
        this.station = station;
    }

    /**
     *
     * @return
     * The surrogate
     */
    public boolean isSurrogate() {
        return surrogate;
    }

    /**
     *
     * @param surrogate
     * The surrogate
     */
    public void setSurrogate(boolean surrogate) {
        this.surrogate = surrogate;
    }

    /**
     *
     * @return
     * The dq
     */
    public boolean isDq() {
        return dq;
    }

    /**
     *
     * @param dq
     * The dq
     */
    public void setDq(boolean dq) {
        this.dq = dq;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}


