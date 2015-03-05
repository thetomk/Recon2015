
package com.eaglerobotics.reconalpha.frcAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class FRCEvents {

    private List<Event> Events = new ArrayList<Event>();
    private int eventCount;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The Events
     */
    public List<Event> getEvents() {
        return Events;
    }

    /**
     *
     * @param Events
     * The Events
     */
    public void setEvents(List<Event> Events) {
        this.Events = Events;
    }

    /**
     *
     * @return
     * The eventCount
     */
    public int getEventCount() {
        return eventCount;
    }

    /**
     *
     * @param eventCount
     * The eventCount
     */
    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
