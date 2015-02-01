package com.eaglerobotics.reconalpha;

import java.util.Comparator;

import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.eaglerobotics.reconalpha.util.TeamInfo;

public class byRatingComparator implements Comparator<TeamInfo> {
    public int compare(TeamInfo p1, TeamInfo p2) {
        return p2.getRating() - (p1.getRating());
    }
}
