package com.eaglerobotics.reconalpha.frcAPI;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Event {

    private String code;
    private Object divisionCode;
    private String name;
    private String type;
    private Object districtCode;
    private String venue;
    private String city;
    private String stateprov;
    private String country;
    private String dateStart;
    private String dateEnd;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The code
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     * The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return
     * The divisionCode
     */
    public Object getDivisionCode() {
        return divisionCode;
    }

    /**
     *
     * @param divisionCode
     * The divisionCode
     */
    public void setDivisionCode(Object divisionCode) {
        this.divisionCode = divisionCode;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The districtCode
     */
    public Object getDistrictCode() {
        return districtCode;
    }

    /**
     *
     * @param districtCode
     * The districtCode
     */
    public void setDistrictCode(Object districtCode) {
        this.districtCode = districtCode;
    }

    /**
     *
     * @return
     * The venue
     */
    public String getVenue() {
        return venue;
    }

    /**
     *
     * @param venue
     * The venue
     */
    public void setVenue(String venue) {
        this.venue = venue;
    }

    /**
     *
     * @return
     * The city
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     * The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     * The stateprov
     */
    public String getStateprov() {
        return stateprov;
    }

    /**
     *
     * @param stateprov
     * The stateprov
     */
    public void setStateprov(String stateprov) {
        this.stateprov = stateprov;
    }

    /**
     *
     * @return
     * The country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     * The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     * The dateStart
     */
    public String getDateStart() {
        return dateStart;
    }

    /**
     *
     * @param dateStart
     * The dateStart
     */
    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    /**
     *
     * @return
     * The dateEnd
     */
    public String getDateEnd() {
        return dateEnd;
    }

    /**
     *
     * @param dateEnd
     * The dateEnd
     */
    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
