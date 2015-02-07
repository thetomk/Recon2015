package com.eaglerobotics.reconalpha;

import java.util.Comparator;

import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;

public class byMatchComparator implements Comparator<TeamMatch> {
    public int compare(TeamMatch p1, TeamMatch p2) {
    	
//    	int val1 = p1.getMatchNum().compareTo(p2.getMatchNum());
    	int val1 = (Integer.parseInt(p2.getMatchNum())>Integer.parseInt(p1.getMatchNum()) ? 1 : (Integer.parseInt(p1.getMatchNum())==Integer.parseInt(p2.getMatchNum()) ? 0 : -1));
    	if (val1 == 0) {
    		return p1.getTeamNum().compareTo(p2.getTeamNum());
    	} else {
    		return val1;
    	}

    }
}
