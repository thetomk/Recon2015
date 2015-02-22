package com.eaglerobotics.reconalpha.frcAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Schedule {

    private String description;
    private String level;
    private String startTime;
    private int matchNumber;
    private int scoreRedFinal;
    private int scoreRedFoul;
    private int scoreRedAuto;
    private int scoreBlueFinal;
    private int scoreBlueFoul;
    private int scoreBlueAuto;
    private List<SchedTeam> Teams = new ArrayList<SchedTeam>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The level
     */
    public String getLevel() {
        return level;
    }

    /**
     *
     * @param level
     * The level
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     *
     * @return
     * The startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     *
     * @param startTime
     * The startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     *
     * @return
     * The matchNumber
     */
    public int getMatchNumber() {
        return matchNumber;
    }

    /**
     *
     * @param matchNumber
     * The matchNumber
     */
    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
    }

    /**
     *
     * @return
     * The scoreRedFinal
     */
    public int getScoreRedFinal() {
        return scoreRedFinal;
    }

    /**
     *
     * @param scoreRedFinal
     * The scoreRedFinal
     */
    public void setScoreRedFinal(int scoreRedFinal) {
        this.scoreRedFinal = scoreRedFinal;
    }

    /**
     *
     * @return
     * The scoreRedFoul
     */
    public int getScoreRedFoul() {
        return scoreRedFoul;
    }

    /**
     *
     * @param scoreRedFoul
     * The scoreRedFoul
     */
    public void setScoreRedFoul(int scoreRedFoul) {
        this.scoreRedFoul = scoreRedFoul;
    }

    /**
     *
     * @return
     * The scoreRedAuto
     */
    public int getScoreRedAuto() {
        return scoreRedAuto;
    }

    /**
     *
     * @param scoreRedAuto
     * The scoreRedAuto
     */
    public void setScoreRedAuto(int scoreRedAuto) {
        this.scoreRedAuto = scoreRedAuto;
    }

    /**
     *
     * @return
     * The scoreBlueFinal
     */
    public int getScoreBlueFinal() {
        return scoreBlueFinal;
    }

    /**
     *
     * @param scoreBlueFinal
     * The scoreBlueFinal
     */
    public void setScoreBlueFinal(int scoreBlueFinal) {
        this.scoreBlueFinal = scoreBlueFinal;
    }

    /**
     *
     * @return
     * The scoreBlueFoul
     */
    public int getScoreBlueFoul() {
        return scoreBlueFoul;
    }

    /**
     *
     * @param scoreBlueFoul
     * The scoreBlueFoul
     */
    public void setScoreBlueFoul(int scoreBlueFoul) {
        this.scoreBlueFoul = scoreBlueFoul;
    }

    /**
     *
     * @return
     * The scoreBlueAuto
     */
    public int getScoreBlueAuto() {
        return scoreBlueAuto;
    }

    /**
     *
     * @param scoreBlueAuto
     * The scoreBlueAuto
     */
    public void setScoreBlueAuto(int scoreBlueAuto) {
        this.scoreBlueAuto = scoreBlueAuto;
    }

    /**
     *
     * @return
     * The Teams
     */
    public List<SchedTeam> getTeams() {
        return Teams;
    }

    /**
     *
     * @param Teams
     * The Teams
     */
    public void setTeams(List<SchedTeam> Teams) {
        this.Teams = Teams;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
