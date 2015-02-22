
package com.eaglerobotics.reconalpha.frcAPI;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Ranking {

    private int autoPoints;
    private int containerPoints;
    private int coopertitionPoints;
    private int dq;
    private int litterPoints;
    private int losses;
    private int matchesPlayed;
    private float qualAverage;
    private int rank;
    private int teamNumber;
    private int ties;
    private int totePoints;
    private int wins;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The autoPoints
     */
    public int getAutoPoints() {
        return autoPoints;
    }

    /**
     *
     * @param autoPoints
     * The autoPoints
     */
    public void setAutoPoints(int autoPoints) {
        this.autoPoints = autoPoints;
    }

    /**
     *
     * @return
     * The containerPoints
     */
    public int getContainerPoints() {
        return containerPoints;
    }

    /**
     *
     * @param containerPoints
     * The containerPoints
     */
    public void setContainerPoints(int containerPoints) {
        this.containerPoints = containerPoints;
    }

    /**
     *
     * @return
     * The coopertitionPoints
     */
    public int getCoopertitionPoints() {
        return coopertitionPoints;
    }

    /**
     *
     * @param coopertitionPoints
     * The coopertitionPoints
     */
    public void setCoopertitionPoints(int coopertitionPoints) {
        this.coopertitionPoints = coopertitionPoints;
    }

    /**
     *
     * @return
     * The dq
     */
    public int getDq() {
        return dq;
    }

    /**
     *
     * @param dq
     * The dq
     */
    public void setDq(int dq) {
        this.dq = dq;
    }

    /**
     *
     * @return
     * The litterPoints
     */
    public int getLitterPoints() {
        return litterPoints;
    }

    /**
     *
     * @param litterPoints
     * The litterPoints
     */
    public void setLitterPoints(int litterPoints) {
        this.litterPoints = litterPoints;
    }

    /**
     *
     * @return
     * The losses
     */
    public int getLosses() {
        return losses;
    }

    /**
     *
     * @param losses
     * The losses
     */
    public void setLosses(int losses) {
        this.losses = losses;
    }

    /**
     *
     * @return
     * The matchesPlayed
     */
    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    /**
     *
     * @param matchesPlayed
     * The matchesPlayed
     */
    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    /**
     *
     * @return
     * The qualAverage
     */
    public float getQualAverage() {
        return qualAverage;
    }

    /**
     *
     * @param qualAverage
     * The qualAverage
     */
    public void setQualAverage(float qualAverage) {
        this.qualAverage = qualAverage;
    }

    /**
     *
     * @return
     * The rank
     */
    public int getRank() {
        return rank;
    }

    /**
     *
     * @param rank
     * The rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     *
     * @return
     * The teamNumber
     */
    public int getTeamNumber() {
        return teamNumber;
    }

    /**
     *
     * @param teamNumber
     * The teamNumber
     */
    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    /**
     *
     * @return
     * The ties
     */
    public int getTies() {
        return ties;
    }

    /**
     *
     * @param ties
     * The ties
     */
    public void setTies(int ties) {
        this.ties = ties;
    }

    /**
     *
     * @return
     * The totePoints
     */
    public int getTotePoints() {
        return totePoints;
    }

    /**
     *
     * @param totePoints
     * The totePoints
     */
    public void setTotePoints(int totePoints) {
        this.totePoints = totePoints;
    }

    /**
     *
     * @return
     * The wins
     */
    public int getWins() {
        return wins;
    }

    /**
     *
     * @param wins
     * The wins
     */
    public void setWins(int wins) {
        this.wins = wins;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
