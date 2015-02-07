package com.eaglerobotics.reconalpha;

import java.util.Comparator;

import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;

public class byTimeComparator implements Comparator<TeamMatch> {
    public int compare(TeamMatch p1, TeamMatch p2) {
        return p1.getCreateTime().compareTo(p2.getCreateTime());
    }
}
