package com.eaglerobotics.reconalpha.frcAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class FRCSchedule {

    private List<Schedule> Schedule = new ArrayList<Schedule>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The Schedule
     */
    public List<Schedule> getSchedule() {
        return Schedule;
    }

    /**
     *
     * @param Schedule
     * The Schedule
     */
    public void setSchedule(List<Schedule> Schedule) {
        this.Schedule = Schedule;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
