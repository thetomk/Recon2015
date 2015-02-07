package com.eaglerobotics.reconalpha;

import java.util.Comparator;
import java.util.regex.Pattern;


import com.eaglerobotics.reconalpha.DynamoDBManager.MatchSched;

public class byNaturalMatchTypeComparator implements Comparator<MatchSched> {
	
	private static Pattern BOUNDARYSPLIT = Pattern.compile("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
	
    public int compare(MatchSched p1, MatchSched p2) {
    	int val1 = p2.getMatchType().compareTo(p1.getMatchType());
    	if (val1 == 0) {
    		
//    		return p1.getMatchNum().compareTo(p2.getMatchNum());
    		String s1 = p1.getMatchNum();
    		String s2 = p2.getMatchNum();
    		
            String[] s1Parts = BOUNDARYSPLIT.split(s1);
            String[] s2Parts = BOUNDARYSPLIT.split(s2);

            int i = 0;
            while(i < s1Parts.length && i < s2Parts.length){

                //if parts are the same
                if(s1Parts[i].compareTo(s2Parts[i]) == 0){
                    //go to next part
                    ++i;
                }else{
                    //check parts are both numeric
                    if(s1Parts[i].charAt(0) >= '0' && s1Parts[i].charAt(0) <= '9' 
                            && s2Parts[i].charAt(0) >= '0' && s2Parts[i].charAt(0) <= '9'){
                        try{

                            int intS1 = Integer.parseInt(s1Parts[i]);
                            int intS2 = Integer.parseInt(s2Parts[i]);

                            int diff = intS1 - intS2;

                            if(diff == 0){
                                ++i;
                            }else{
                                return diff;
                            }
                        }catch(Exception ex){
                            //'should' never reach but ...
                            return s1Parts[i].compareTo(s2Parts[i]);
                        }
                    }else{
                        //string compare if neither are numeric
                        return s1Parts[i].compareTo(s2Parts[i]);
                    }
                }
            }

            //Handle if one string is a prefix of the other.
            // nothing comes before something.
            if(s1.length() < s2.length()){
                return -1;
            }else if(s1.length() > s2.length()){
                return 1;
            }else{
                return 0;
            }
    	} else {
    		return val1;
    	}
    }
}
