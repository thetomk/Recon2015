package com.eaglerobotics.reconalpha.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.eaglerobotics.reconalpha.DynamoDBManager;
import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;


public class Team {
    static HashMap<String, Integer> countMap;
    
 
	

	
	public static TeamInfo getTeamSynopsis(String ev, String team) {
		
		TeamInfo currTeam;
		int mcount = 0;
		float tp, bp, np;
		int gpct = 0;
		ArrayList<TeamMatch> matchList;
		String[] commentStrings;
		int cpct;
		String allcomm;
		String synComments;

	
		
		mcount = 0;
		cpct = 0;
		allcomm = "";
		currTeam = new TeamInfo();
		currTeam.setTeamNum(team);
	
		// go get team data here and add to object
		matchList = new ArrayList<TeamMatch>();
		matchList = DynamoDBManager.getTeamList(ev,team);
		
		for (TeamMatch curritem : matchList) {
			
			tp = (float) curritem.getTotes();
			bp = (float) curritem.getBins();
			np = (float) curritem.getNoodles();
			gpct = (int) ((bp+np / (tp+bp+np))*100);
			cpct = cpct + gpct;
			mcount = mcount + 1;
			allcomm = allcomm + curritem.getComments();
		}
		currTeam.setAllComments(allcomm);
		if (mcount > 0) {
			currTeam.setGoodPct((int) ((cpct/mcount)));
		}
        commentStrings = allcomm.split("; "); 
        countMap = new HashMap<String, Integer>();
        countStringOccurences(commentStrings);

        synComments = "";
        int maxsofar = 0;
        String currComment = "";
        int maxitems;
        
        if (countMap.size()>4) {
        	maxitems = 4;
        } else {
        		maxitems = countMap.size();
        }
        
        for (int i = 0; i<maxitems; i++) {

			for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
	       		if(maxsofar < entry.getValue()) {
	       			maxsofar = entry.getValue();
	       			currComment = entry.getKey();
	       		}
	       	}
			
	   		synComments = synComments + "/" + currComment;
	   		countMap.put(currComment, -1);
	       	maxsofar = 0;
        }
        
        currTeam.setSynopsis(synComments);

		
		return currTeam;
		

		
	}
	
	private static void countStringOccurences(String[] strArray) {
        for (String string : strArray) {
            if (!countMap.containsKey(string)) {
                countMap.put(string, 1);
            } else {
                Integer count = countMap.get(string);
                count = count + 1;
                countMap.put(string, count);
            }
        }
		
	}


}
