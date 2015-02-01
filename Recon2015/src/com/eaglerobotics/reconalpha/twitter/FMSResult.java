package com.eaglerobotics.reconalpha.twitter;

public class FMSResult {

	private String event;
	private String match_type;
	private int red_final;
	private int blue_final;
	private String red_1;
	private String red_2;
	private String red_3;
	private String blue_1;
	private String blue_2;
	private String blue_3;
//	private int red_climb;
//	private int blue_climb;
	private int red_foul;
	private int blue_foul;
	private int red_auto;
	private int blue_auto;
	private int red_tele;
	private int blue_tele;
	
	public String getEvent() {
		return event;
	}
	public String getMatchType() {
		return match_type;
	}
	public int getRedFinal() {
		return red_final;
	}
	public int getBlueFinal() {
		return blue_final;
	}
	public String getRed1() {
		return red_1;
	}
	public String getRed2() {
		return red_2;
	}
	public String getRed3() {
		return red_3;
	}
	public String getBlue1() {
		return blue_1;
	}
	public String getBlue2() {
		return blue_2;
	}
	public String getBlue3() {
		return blue_3;
	}
//	public int getRedClimb() {
//		return red_climb;
//	}
//	public int getBlueClimb() {
//		return blue_climb;
//	}
	public int getRedFoul() {
		return red_foul;
	}
	public int getBlueFoul() {
		return blue_foul;
	}
	public int getRedAuto() {
		return red_auto;
	}
	public int getBlueAuto() {
		return blue_auto;
	
	}
	public int getRedTele() {
		return red_tele;
	}
	public int getBlueTele() {
		return blue_tele;
	}
	public void setEvent(String e) {
		 event = e;
	}
	public void setMatchType(String m) {
		match_type = m;
	}
	public void setRedFinal(int rf) {
		red_final = rf;
	}
	public void setBlueFinal(int bf) {
		blue_final = bf;
	}
	public void setRed1(String r1) {
		red_1 = r1;
	}
	public void setRed2(String r2) {
		red_2 = r2;
	}
	public void setRed3(String r3) {
		red_3 = r3;
	}
	public void setBlue1(String b1) {
		blue_1 = b1;
	}
	public void setBlue2(String b2) {
		blue_2 = b2;
	}
	public void setBlue3(String b3) {
		blue_3 = b3;
	}
//	public void setRedClimb(int rc) {
//		red_climb = rc;
//	}
//	public void setBlueClimb(int bc) {
//		blue_climb = bc;
//	}
	public void setRedFoul(int rf) {
		red_foul = rf;
	}
	public void setBlueFoul(int bf) {
		blue_foul = bf;
	}
	public void setRedAuto(int ra) {
		red_auto = ra;
	}
	public void setBlueAuto(int ba) {
		blue_auto = ba;
	}
	public void setRedTele(int rt) {
		red_tele = rt;
	}
	public void setBlueTele(int bt) {
		blue_tele = bt;
	}
}
