package com.eaglerobotics.reconalpha;

import java.util.Comparator;

import com.eaglerobotics.reconalpha.DynamoDBManager.MatchSched;

public class byMatchTypeComparator implements Comparator<MatchSched> {
    public int compare(MatchSched p1, MatchSched p2) {
    	int val1 = p2.getMatchType().compareTo(p1.getMatchType());
    	if (val1 == 0) {
    		return p1.getMatchNum().compareTo(p2.getMatchNum());
    	} else {
    		return val1;
    	}
    }
}
