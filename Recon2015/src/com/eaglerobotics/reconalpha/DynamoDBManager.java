/*
 * Copyright 2010-2013 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.eaglerobotics.reconalpha;

import java.util.ArrayList;

import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;


public class DynamoDBManager {


	/*
	 * Retrieves the table description and returns the table status as a string.
	 */
	public static String getTableStatus() {

		try {
			AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();

			DescribeTableRequest request = new DescribeTableRequest()
					.withTableName(PropertyLoader.getInstance()
							.getTableName());
			DescribeTableResult result = ddb.describeTable(request);

			String status = result.getTable().getTableStatus();
			return status == null ? "" : status;

		} catch (ResourceNotFoundException e) {
		} catch (AmazonServiceException ex) {
			MainActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}

		return "";
	}


	/*
	 * Scans the table and returns the list of teams/matches.
	 */
	public static ArrayList<TeamMatch> getMatchList(String eventID) {

		AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("eventID", 
                new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(new AttributeValue().withS(eventID)));
		try {
			PaginatedScanList<TeamMatch> result = mapper.scan(
					TeamMatch.class, scanExpression);

			ArrayList<TeamMatch> resultList = new ArrayList<TeamMatch>();
			for (TeamMatch up : result) {
				resultList.add(up);
			}

			return resultList;

		} catch (AmazonServiceException ex) {
			MainActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}

		return null;
	}
	/*
	 * Scans the table and returns the list of matches for a specific team.
	 */
	public static ArrayList<TeamMatch> getTeamList(String eventID, String team) {

		AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		
        scanExpression.addFilterCondition("teamNum", 
                new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(new AttributeValue().withS(team)));
        scanExpression.addFilterCondition("eventID", 
                new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(new AttributeValue().withS(eventID)));
		try {
			PaginatedScanList<TeamMatch> result = mapper.scan(
					TeamMatch.class, scanExpression);

			ArrayList<TeamMatch> resultList = new ArrayList<TeamMatch>();
			for (TeamMatch up : result) {
				resultList.add(up);
			}

			return resultList;

		} catch (AmazonServiceException ex) {
			MainActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}

		return null;
	}

	/*
	 * Scans the table and returns the list of teams for a specific match.
	 */
	public static ArrayList<TeamMatch> getTeamsforMatch(String eventID, String match) {

		AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("matchNum", 
                new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(new AttributeValue().withS(match)));
        scanExpression.addFilterCondition("eventID", 
                new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(new AttributeValue().withS(eventID)));
		try {
			PaginatedScanList<TeamMatch> result = mapper.scan(
					TeamMatch.class, scanExpression);

			ArrayList<TeamMatch> resultList = new ArrayList<TeamMatch>();
			for (TeamMatch up : result) {
				resultList.add(up);
			}

			return resultList;

		} catch (AmazonServiceException ex) {
			MainActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}

		return null;
	}
	/*
	 * Retrieves all of the attribute/value pairs for the specified team/match
	 */
	public static TeamMatch getTeamMatch(String eventID, String team, String match) {

		AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		try {
			TeamMatch obj = mapper.load(TeamMatch.class,team, match);


			return obj;

		} catch (AmazonServiceException ex) {
			MainActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}

		return null;
	}

	/*
	 * Updates one team/match.
	 */
	public static void updateTeamMatch(TeamMatch u_teamMatch) {

		AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		try {
			mapper.save(u_teamMatch);

		} catch (AmazonServiceException ex) {
			MainActivity.clientManager.wipeCredentialsOnAuthError(ex);
			Log.i("DDB Mgr",ex.getMessage());
		}
	}

	/*
	 * Deletes the specified team/match and all of its attribute/value pairs.
	 */
	public static void deleteTeamMatch(TeamMatch d_teamMatch) {

		AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		try {
			mapper.delete(d_teamMatch);

		} catch (AmazonServiceException ex) {
			MainActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}
	}

	/*
	 * Deletes the table and all of its team/matches and their attribute/value
	 * pairs.
	 */
/*	public static void cleanUp() {

		AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();

		DeleteTableRequest request = new DeleteTableRequest()
				.withTableName(PropertyLoader.getInstance().getTableName());
		try {
			ddb.deleteTable(request);
			
		} catch (AmazonServiceException ex) {
			MainActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}
	}
*/	
	

	@DynamoDBTable(tableName = "Recon2015Match")
	public static class TeamMatch {
		private String reconKey;
		private String teamNum;
		private String matchNum;
		private int totes;
		private int bins;
		private int noodles;
		private String comments;
		private String createTime;
		private String alliance;
		private String creator;
		private String eventID;
		private Boolean autoMove;
		private Boolean autoTote;
		private Boolean autoBin;
		private Boolean autoStack;
		private Boolean fast;
		private Boolean stackTote;
		private Boolean stackBin;
		private Boolean driver;
		private Boolean carry;
		private Boolean noodleBin;
		private Boolean coopTote;
		private Boolean coopStack;
		private Boolean noodleFloor;
		private Boolean noodleThrow;
		private Boolean died;
		private Boolean pickable;
		

		@DynamoDBIndexRangeKey(attributeName = "reconKey")
		public String getKey() {
			return reconKey;
		}

		public void setKey(String reconKey) {
			this.reconKey = reconKey;
		}

		@DynamoDBHashKey(attributeName = "teamNum")
		public String getTeamNum() {
			return teamNum;
		}

		public void setTeamNum(String teamNum) {
			this.teamNum = teamNum;
		}

		@DynamoDBRangeKey(attributeName = "matchNum")
		public String getMatchNum() {
			return matchNum;
		}

		public void setMatchNum(String matchNum) {
			this.matchNum = matchNum;
		}

		@DynamoDBAttribute(attributeName = "totes")
		public int getTotes() {
			return totes;
		}

		public void setTotes(int totes) {
			this.totes = totes;
		}
		
		@DynamoDBAttribute(attributeName = "noodles")
		public int getNoodles() {
			return noodles;
		}

		public void setNoodles(int noodles) {
			this.noodles = noodles;
		}

		@DynamoDBAttribute(attributeName = "bins")
		public int getBins() {
			return bins;
		}

		public void setBins(int bins) {
			this.bins = bins;
		}


		@DynamoDBAttribute(attributeName = "comments")
		public String getComments() {
			return comments;
		}

		public void setComments(String comments) {
			this.comments = comments;
		}

		@DynamoDBIndexRangeKey(attributeName = "createTime")
		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		@DynamoDBAttribute(attributeName = "alliance")
		public String getAlliance() {
			return alliance;
		}

		public void setAlliance(String alliance) {
			this.alliance = alliance;
		}

		@DynamoDBAttribute(attributeName = "creator")
		public String getCreator() {
			return creator;
		}

		public void setCreator(String creator) {
			this.creator = creator;
		}

		@DynamoDBIndexRangeKey(attributeName = "eventID")
		public String getEventID() {
			return eventID;
		}

		public void setEventID(String eventID) {
			this.eventID = eventID;
		}
		
		@DynamoDBAttribute(attributeName = "autoMove")
		public Boolean getAutoMove() {
			return autoMove;
		}

		public void setAutoMove(Boolean autoMove) {
			this.autoMove = autoMove;
		}
		
		@DynamoDBAttribute(attributeName = "autoTote")
		public Boolean getAutoTote() {
			return autoTote;
		}

		public void setAutoTote(Boolean autoTote) {
			this.autoTote = autoTote;
		}
		
		@DynamoDBAttribute(attributeName = "autoBin")
		public Boolean getAutoBin() {
			return autoBin;
		}

		public void setAutoBin(Boolean autoBin) {
			this.autoBin = autoBin;
		}
		
		@DynamoDBAttribute(attributeName = "autoStack")
		public Boolean getAutoStack() {
			return autoStack;
		}

		public void setAutoStack(Boolean autoStack) {
			this.autoStack = autoStack;
		}
		
		@DynamoDBAttribute(attributeName = "fast")
		public Boolean getFast() {
			return fast;
		}

		public void setFast(Boolean fast) {
			this.fast = fast;
		}
		
		@DynamoDBAttribute(attributeName = "stackTote")
		public Boolean getStackTote() {
			return stackTote;
		}

		public void setStackTote(Boolean stackTote) {
			this.stackTote = stackTote;
		}
		
		@DynamoDBAttribute(attributeName = "stackBin")
		public Boolean getStackBin() {
			return stackBin;
		}

		public void setStackBin(Boolean stackBin) {
			this.stackBin = stackBin;
		}
		
		@DynamoDBAttribute(attributeName = "driver")
		public Boolean getDriver() {
			return driver;
		}

		public void setDriver(Boolean driver) {
			this.driver = driver;
		}
		
		@DynamoDBAttribute(attributeName = "carry")
		public Boolean getCarry() {
			return carry;
		}

		public void setCarry(Boolean carry) {
			this.carry = carry;
		}
		
		@DynamoDBAttribute(attributeName = "noodleBin")
		public Boolean getNoodleBin() {
			return noodleBin;
		}

		public void setNoodleBin(Boolean noodleBin) {
			this.noodleBin = noodleBin;
		}
		
		@DynamoDBAttribute(attributeName = "coopTote")
		public Boolean getCoopTote() {
			return coopTote;
		}

		public void setCoopTote(Boolean coopTote) {
			this.coopTote = coopTote;
		}
		
		@DynamoDBAttribute(attributeName = "coopStack")
		public Boolean getCoopStack() {
			return coopStack;
		}

		public void setCoopStack(Boolean coopStack) {
			this.coopStack = coopStack;
		}
		
		@DynamoDBAttribute(attributeName = "noodleFloor")
		public Boolean getNoodleFloor() {
			return noodleFloor;
		}

		public void setNoodleFloor(Boolean noodleFloor) {
			this.noodleFloor = noodleFloor;
		}
		
		
		@DynamoDBAttribute(attributeName = "noodleThrow")
		public Boolean getNoodleThrow() {
			return noodleThrow;
		}

		public void setNoodleThrow(Boolean noodleThrow) {
			this.noodleThrow = noodleThrow;
		}

		@DynamoDBAttribute(attributeName = "died")
		public Boolean getDied() {
			return died;
		}

		public void setDied(Boolean died) {
			this.died = died;
		}
		
		@DynamoDBAttribute(attributeName = "pickable")
		public Boolean getPickable() {
			return pickable;
		}

		public void setPickable(Boolean pickable) {
			this.pickable = pickable;
		}
		
	}

	
	
	/*
	 * Retrieves all of the attribute/value pairs for the specified match schedule
	 */
	public static MatchSched getSchedMatch(String event, String match) {

		AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		try {
			MatchSched obj = mapper.load(MatchSched.class, match, event);


			return obj;

		} catch (AmazonServiceException ex) {
			MainActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}

		return null;
	}

	/*
	 * Updates one match schedule row.
	 */
	public static void updateMatchSched(MatchSched u_matchSched) {

		AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		try {
			mapper.save(u_matchSched);

		} catch (AmazonServiceException ex) {
			MainActivity.clientManager.wipeCredentialsOnAuthError(ex);
			Log.i("DDB Mgr",ex.getMessage());
		}
	}

	/*
	 * Scans the table and returns the list of all matches.
	 */
	public static ArrayList<MatchSched> getSchedList(String eventID) {

		AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("eventID", 
                new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(new AttributeValue().withS(eventID)));

		
		try {
			PaginatedScanList<MatchSched> result = mapper.scan(
					MatchSched.class, scanExpression);

			ArrayList<MatchSched> resultList = new ArrayList<MatchSched>();
			for (MatchSched up : result) {
				resultList.add(up);
			}

			return resultList;

		} catch (AmazonServiceException ex) {
			MainActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}

		return null;
	}
	
	@DynamoDBTable(tableName = "Recon2015Sched")
	public static class MatchSched {
		private String matchNum;
		private String eventID;
		private String matchType;
		private String red1;
		private String red2;
		private String red3;
		private String blue1;
		private String blue2;
		private String blue3;
		private int redTotal;
		private int redAuto;
		private int redFoul;
		private int redTele;
//		private int redEndGame;
		private int blueTotal;
		private int blueAuto;
		private int blueFoul;
		private int blueTele;
//		private int blueEndGame;
		private String createTime;


		@DynamoDBHashKey(attributeName = "matchNum")
		public String getMatchNum() {
			return matchNum;
		}

		public void setMatchNum(String matchNum) {
			this.matchNum = matchNum;
		}
		
		@DynamoDBRangeKey(attributeName = "eventID")
		public String getEvent() {
			return eventID;
		}

		public void setEvent(String eventID) {
			this.eventID = eventID;
		}

		@DynamoDBAttribute(attributeName = "matchType")
		public String getMatchType() {
			return matchType;
		}

		public void setMatchType(String matchType) {
			this.matchType = matchType;
		}
	
		@DynamoDBAttribute(attributeName = "red1")
		public String getRed1() {
			return red1;
		}

		public void setRed1(String red1) {
			this.red1 = red1;
		}
		
		@DynamoDBAttribute(attributeName = "red2")
		public String getRed2() {
			return red2;
		}

		public void setRed2(String red2) {
			this.red2 = red2;
		}
		@DynamoDBAttribute(attributeName = "red3")
		public String getRed3() {
			return red3;
		}

		public void setRed3(String red3) {
			this.red3 = red3;
		}
		@DynamoDBAttribute(attributeName = "blue1")
		public String getBlue1() {
			return blue1;
		}

		public void setBlue1(String blue1) {
			this.blue1 = blue1;
		}
		@DynamoDBAttribute(attributeName = "blue2")
		public String getBlue2() {
			return blue2;
		}

		public void setBlue2(String blue2) {
			this.blue2 = blue2;
		}
		@DynamoDBAttribute(attributeName = "blue3")
		public String getBlue3() {
			return blue3;
		}

		public void setBlue3(String blue3) {
			this.blue3 = blue3;
		}

		@DynamoDBAttribute(attributeName = "redTotal")
		public int getRedTotal() {
			return redTotal;
		}

		public void setRedTotal(int redTotal) {
			this.redTotal = redTotal;

		}

		@DynamoDBAttribute(attributeName = "redAuto")
		public int getRedAuto() {
			return redAuto;
		}

		public void setRedAuto(int redAuto) {
			this.redAuto = redAuto;

		}

		@DynamoDBAttribute(attributeName = "redFoul")
		public int getRedFoul() {
			return redFoul;
		}

		public void setRedFoul(int redFoul) {
			this.redFoul = redFoul;

		}

		@DynamoDBAttribute(attributeName = "redTele")
		public int getRedTele() {
			return redTele;
		}

		public void setRedTele(int redTele) {
			this.redTele = redTele;

		}

//		@DynamoDBAttribute(attributeName = "redEndGame")
//		public int getRedEndGame() {
//			return redEndGame;
//		}

//		public void setRedEndGame(int redEndGame) {
//			this.redEndGame = redEndGame;

//		}


		@DynamoDBAttribute(attributeName = "blueTotal")
		public int getBlueTotal() {
			return blueTotal;
		}

		public void setBlueTotal(int blueTotal) {
			this.blueTotal = blueTotal;

		}

		@DynamoDBAttribute(attributeName = "blueAuto")
		public int getBlueAuto() {
			return blueAuto;
		}

		public void setBlueAuto(int blueAuto) {
			this.blueAuto = blueAuto;

		}

		@DynamoDBAttribute(attributeName = "blueFoul")
		public int getBlueFoul() {
			return blueFoul;
		}

		public void setBlueFoul(int blueFoul) {
			this.blueFoul = blueFoul;

		}

		@DynamoDBAttribute(attributeName = "blueTele")
		public int getBlueTele() {
			return blueTele;
		}

		public void setBlueTele(int blueTele) {
			this.blueTele = blueTele;

		}

//		@DynamoDBAttribute(attributeName = "blueEndGame")
//		public int getBlueEndGame() {
//			return blueEndGame;
//		}

//		public void setBlueEndGame(int blueEndGame) {
//			this.blueEndGame = blueEndGame;

//		}

		@DynamoDBAttribute(attributeName = "createTime")
		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
	}
	
//	area for team object
	
	public static TeamData getTeamData(String event, String team) {

		AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		try {
			TeamData obj = mapper.load(TeamData.class, team, event);


			return obj;

		} catch (AmazonServiceException ex) {
			MainActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}

		return null;
	}

	/*
	 * Updates one team row.
	 */
	public static void updateTeamData(TeamData u_teamData) {

		AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		try {
			mapper.save(u_teamData);

		} catch (AmazonServiceException ex) {
			MainActivity.clientManager.wipeCredentialsOnAuthError(ex);
			Log.i("DDB Mgr",ex.getMessage());
		}
	}
	
	/*
	 * Scans the table and returns the list of all teams.
	 */
	public static ArrayList<TeamData> getTeamList(String eventID) {

		AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("eventID", 
                new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(new AttributeValue().withS(eventID)));

		
		try {
			PaginatedScanList<TeamData> result = mapper.scan(
					TeamData.class, scanExpression);

			ArrayList<TeamData> resultList = new ArrayList<TeamData>();
			for (TeamData up : result) {
				resultList.add(up);
			}

			return resultList;

		} catch (AmazonServiceException ex) {
			MainActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}

		return null;
	}
	
	
	@DynamoDBTable(tableName = "Recon2015Team")
	public static class TeamData {
		private String teamNum;
		private String createTime;
		private String creator;
		private String eventID;
		private float goodPct;
		private int totes;
		private int bins;
		private int noodles;
		private int autoMove;
		private int autoTote;
		private int autoBin;
		private int autoStack;
		private int fast;
		private int stackTote;
		private int stackBin;
		private int driver;
		private int carry;
		private int noodleBin;
		private int coopTote;
		private int coopStack;
		private int noodleFloor;
		private int noodleThrow;
		private int died;
		private int pickable;
		private int numMatches;
		private String teamName;
		private String s3Bucket;
		private String teamPhoto;
		private String teamVideo;
		private int oAuto;
		private int oTele;
		private int oFoul;
		private int oTotal;
		private int dAuto;
		private int dTele;
		private int dFoul;
		private int dTotal;
		private int numScores;
		private int chosen;

		


		@DynamoDBHashKey(attributeName = "teamNum")
		public String getTeamNum() {
			return teamNum;
		}

		public void setTeamNum(String teamNum) {
			this.teamNum = teamNum;
		}
		
		@DynamoDBAttribute(attributeName = "goodPct")
		public float getGoodPct() {
			return goodPct;
		}

		public void setGoodPct(float goodPct) {
			this.goodPct = goodPct;
		}
		@DynamoDBAttribute(attributeName = "totes")
		public int getTotes() {
			return totes;
		}

		public void setTotes(int totes) {
			this.totes = totes;
		}


		@DynamoDBAttribute(attributeName = "bins")
		public int getBins() {
			return bins;
		}

		public void setBins(int bins) {
			this.bins = bins;
		}


		@DynamoDBAttribute(attributeName = "noodles")
		public int getNoodles() {
			return noodles;
		}

		public void setNoodles(int noodles) {
			this.noodles = noodles;
		}
		
		@DynamoDBIndexRangeKey(attributeName = "createTime")
		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		@DynamoDBAttribute(attributeName = "creator")
		public String getCreator() {
			return creator;
		}

		public void setCreator(String creator) {
			this.creator = creator;
		}

		@DynamoDBRangeKey(attributeName = "eventID")
		public String getEventID() {
			return eventID;
		}

		public void setEventID(String eventID) {
			this.eventID = eventID;
		}
		
		@DynamoDBAttribute(attributeName = "autoMove")
		public int getAutoMove() {
			return autoMove;
		}

		public void setAutoMove(int autoMove) {
			this.autoMove = autoMove;
		}
		
		@DynamoDBAttribute(attributeName = "autoTote")
		public int getAutoTote() {
			return autoTote;
		}

		public void setAutoTote(int autoTote) {
			this.autoTote = autoTote;
		}
		
		@DynamoDBAttribute(attributeName = "autoBin")
		public int getAutoBin() {
			return autoBin;
		}

		public void setAutoBin(int autoBin) {
			this.autoBin = autoBin;
		}
		
		@DynamoDBAttribute(attributeName = "autoStack")
		public int getAutoStack() {
			return autoStack;
		}

		public void setAutoStack(int autoStack) {
			this.autoStack = autoStack;
		}
		
		@DynamoDBAttribute(attributeName = "fast")
		public int getFast() {
			return fast;
		}

		public void setFast(int fast) {
			this.fast = fast;
		}
		
		@DynamoDBAttribute(attributeName = "stackTote")
		public int getStackTote() {
			return stackTote;
		}

		public void setStackTote(int stackTote) {
			this.stackTote = stackTote;
		}
		
		@DynamoDBAttribute(attributeName = "stackBin")
		public int getStackBin() {
			return stackBin;
		}

		public void setStackBin(int stackBin) {
			this.stackBin = stackBin;
		}
		
		@DynamoDBAttribute(attributeName = "driver")
		public int getDriver() {
			return driver;
		}

		public void setDriver(int driver) {
			this.driver = driver;
		}
		
		@DynamoDBAttribute(attributeName = "carry")
		public int getCarry() {
			return carry;
		}

		public void setCarry(int carry) {
			this.carry = carry;
		}
		
		@DynamoDBAttribute(attributeName = "noodleBin")
		public int getNoodleBin() {
			return noodleBin;
		}

		public void setNoodleBin(int noodleBin) {
			this.noodleBin = noodleBin;
		}
		
		@DynamoDBAttribute(attributeName = "coopTote")
		public int getCoopTote() {
			return coopTote;
		}

		public void setCoopTote(int coopTote) {
			this.coopTote = coopTote;
		}
		
		@DynamoDBAttribute(attributeName = "coopStack")
		public int getCoopStack() {
			return coopStack;
		}

		public void setCoopStack(int coopStack) {
			this.coopStack = coopStack;
		}
		
		@DynamoDBAttribute(attributeName = "noodleFloor")
		public int getNoodleFloor() {
			return noodleFloor;
		}

		public void setNoodleFloor(int noodleFloor) {
			this.noodleFloor = noodleFloor;
		}
		
		
		@DynamoDBAttribute(attributeName = "noodleThrow")
		public int getNoodleThrow() {
			return noodleThrow;
		}

		public void setNoodleThrow(int noodleThrow) {
			this.noodleThrow = noodleThrow;
		}

		@DynamoDBAttribute(attributeName = "died")
		public int getDied() {
			return died;
		}

		public void setDied(int died) {
			this.died = died;
		}
		
		@DynamoDBAttribute(attributeName = "pickable")
		public int getPickable() {
			return pickable;
		}

		public void setPickable(int pickable) {
			this.pickable = pickable;
		}
		
		@DynamoDBAttribute(attributeName = "numMatches")
		public int getNumMatches() {
			return numMatches;
		}

		public void setNumMatches(int numMatches) {
			this.numMatches = numMatches;
		}
		
		@DynamoDBAttribute(attributeName = "teamName")
		public String getTeamName() {
			return teamName;
		}

		public void setTeamName(String teamName) {
			this.teamName = teamName;
		}

		@DynamoDBAttribute(attributeName = "s3Bucket")
		public String getS3Bucket() {
			return s3Bucket;
		}

		public void setS3Bucket(String s3Bucket) {
			this.s3Bucket = s3Bucket;
		}

		@DynamoDBAttribute(attributeName = "teamPhoto")
		public String getTeamPhoto() {
			return teamPhoto;
		}

		public void setTeamPhoto(String teamPhoto) {
			this.teamPhoto = teamPhoto;
		}

		@DynamoDBAttribute(attributeName = "teamVideo")
		public String getTeamVideo() {
			return teamVideo;
		}

		public void setTeamVideo(String teamVideo) {
			this.teamVideo = teamVideo;
		}
		
		@DynamoDBAttribute(attributeName = "oAuto")
		public int getOAuto() {
			return oAuto;
		}

		public void setOAuto(int oAuto) {
			this.oAuto = oAuto;
		}
		
		@DynamoDBAttribute(attributeName = "oTele")
		public int getOTele() {
			return oTele;
		}

		public void setOTele(int oTele) {
			this.oTele = oTele;
		}
		
		@DynamoDBAttribute(attributeName = "oFoul")
		public int getOFoul() {
			return oFoul;
		}

		public void setOFoul(int oFoul) {
			this.oFoul = oFoul;
		}
		
		@DynamoDBAttribute(attributeName = "oTotal")
		public int getOTotal() {
			return oTotal;
		}

		public void setOTotal(int oTotal) {
			this.oTotal = oTotal;
		}
		
		@DynamoDBAttribute(attributeName = "dAuto")
		public int getDAuto() {
			return dAuto;
		}

		public void setDAuto(int dAuto) {
			this.dAuto = dAuto;
		}
		
		@DynamoDBAttribute(attributeName = "dTele")
		public int getDTele() {
			return dTele;
		}

		public void setDTele(int dTele) {
			this.dTele = dTele;
		}
		
		@DynamoDBAttribute(attributeName = "dFoul")
		public int getDFoul() {
			return dFoul;
		}

		public void setDFoul(int dFoul) {
			this.dFoul = dFoul;
		}
		
		@DynamoDBAttribute(attributeName = "dTotal")
		public int getDTotal() {
			return dTotal;
		}

		public void setDTotal(int dTotal) {
			this.dTotal = dTotal;
		}

		@DynamoDBAttribute(attributeName = "numScores")
		public int getNumScores() {
			return numScores;
		}

		public void setNumScores(int numScores) {
			this.numScores = numScores;
		}

		@DynamoDBAttribute(attributeName = "chosen")
		public int getChosen() {
			return chosen;
		}

		public void setChosen(int chosen) {
			this.chosen = chosen;
		}
	}


}
