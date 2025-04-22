package com.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AgeOfWar {

	private static final String SEMICOLON = ";";
	private static final String HASH = "#";
	private static final String COMMA = ",";
	private List<String> list;
	private Map<String, List<String>> l_mapAdvantageList = new HashMap<>();

	public AgeOfWar() {
		l_mapAdvantageList.put("Militia", getList("Spearmen,LightCavalry"));
		l_mapAdvantageList.put("Spearmen", getList("LightCavalry,HeavyCavalry"));
		l_mapAdvantageList.put("LightCavalry", getList("FootArcher,CavalryArcher"));
		l_mapAdvantageList.put("HeavyCavalry", getList("Militia,FootArcher,LightCavalry"));
		l_mapAdvantageList.put("CavalryArcher", getList("Spearmen,HeavyCavalry"));
		l_mapAdvantageList.put("FootArcher", getList("Militia,CavalryArcher"));
	}

	private List<String> getList(String p_sSoldierClass) {
		list = new ArrayList<>();
		if (p_sSoldierClass == null || p_sSoldierClass.isEmpty()) {
			return list;
		}
		for (String l_sSoldier : p_sSoldierClass.split(COMMA)) {
			list.add(l_sSoldier);
		}
		return list;
	}

	private boolean isAbleToWin(String p_sOwnSoldierClass, int p_iOwnSoldierCount, String p_sOpponentSoldierClass,
			int p_iOpponentSoldierCount) throws Exception {
		if (!l_mapAdvantageList.get(p_sOwnSoldierClass).contains(p_sOpponentSoldierClass)
				&& p_iOwnSoldierCount > p_iOpponentSoldierCount) {
			if (l_mapAdvantageList.get(p_sOpponentSoldierClass).contains(p_sOwnSoldierClass)
					&& p_iOwnSoldierCount > (p_iOpponentSoldierCount * 2)) {
				return true;
			} else if (!l_mapAdvantageList.get(p_sOpponentSoldierClass).contains(p_sOwnSoldierClass)
					&& p_iOwnSoldierCount > p_iOpponentSoldierCount) {
				return true;
			}
		} else if (l_mapAdvantageList.get(p_sOwnSoldierClass).contains(p_sOpponentSoldierClass)
				&& (p_iOwnSoldierCount * 2) > p_iOpponentSoldierCount) {
			return true;
		}
		return false;
	}

	private String getSoldierClass(String p_sPlatoonInfo) throws Exception {
		return p_sPlatoonInfo.split(HASH)[0];
	}

	private int getSoldierCount(String p_sPlatoonInfo) throws Exception {
		return Integer.valueOf(p_sPlatoonInfo.split(HASH)[1]);
	}

	private boolean isWinnableBattle(String[] p_sOwnPlatoonList, String[] p_sOpponentPlatoonList, int p_iBattleNo)
			throws Exception {
		String l_sOwnSoldierClass = getSoldierClass(p_sOwnPlatoonList[p_iBattleNo]);
		int l_iOwnSoldierCount = getSoldierCount(p_sOwnPlatoonList[p_iBattleNo]);
		String l_sOpponentSoldierClass = getSoldierClass(p_sOpponentPlatoonList[p_iBattleNo]);
		int l_iOpponentSoldierCount = getSoldierCount(p_sOpponentPlatoonList[p_iBattleNo]);

		if (isAbleToWin(l_sOwnSoldierClass, l_iOwnSoldierCount, l_sOpponentSoldierClass, l_iOpponentSoldierCount)) {
			return true;
		}
		return false;
	}

	private String toString(String[] p_sList) {
		String l_sResult = "";
		for (String l_sStr : p_sList) {
			l_sResult = l_sResult.concat(l_sStr).concat(SEMICOLON);
		}
		return l_sResult;

	}

	private String shuffleAndGetWinningArrangement(String p_sOwnPlatoon, String p_sOpponentPlatoon) throws Exception {
		String[] l_sOwnPlatoonList = p_sOwnPlatoon.split(SEMICOLON);
		String[] l_sOpponentPlatoonList = p_sOpponentPlatoon.split(SEMICOLON);
		if (l_sOwnPlatoonList.length != 5 || l_sOpponentPlatoonList.length != 5) {
			return "Both players should have 5 Platoons to proceed";
		}

		// Conditional block will not run since, above condition checks both players
		// should have 5 platoons.
//		if (l_sOwnPlatoonList.length != l_sOpponentPlatoonList.length) {
//			return "Own and opponent platoon list length should be equal";
//		}

		String l_sWinningArrangement = "";
		int l_iWinningBattleCount = 0;

		int l_iLoseBattleIndex = -1;
		for (int i = 0; i < l_sOwnPlatoonList.length - 1; i++) {
			int j = i + 1;
			if(l_iLoseBattleIndex>=0) {
				j = l_iLoseBattleIndex;
			}
			while (j < l_sOwnPlatoonList.length && !isWinnableBattle(l_sOwnPlatoonList, l_sOpponentPlatoonList, i)) {
				String temp = l_sOwnPlatoonList[i];
				l_sOwnPlatoonList[i] = l_sOwnPlatoonList[j];
				l_sOwnPlatoonList[j] = temp;
				j++;
			}
			if (isWinnableBattle(l_sOwnPlatoonList, l_sOpponentPlatoonList, i)) {
				l_iWinningBattleCount++;
				if (l_iWinningBattleCount >= 3) {
					break;
				}
			}else {
				l_iLoseBattleIndex = i;
			}

		}

		if (l_iWinningBattleCount >= 3) {
			l_sWinningArrangement = toString(l_sOwnPlatoonList);
		} else {
			l_sWinningArrangement = "There is no chance of winning";
		}

		return l_sWinningArrangement;
	}

	public static void main(String[] args) {
		AgeOfWar aow = new AgeOfWar();
		Scanner sc = new Scanner(System.in);
		try {
			System.out.println("Enter Platoon details of Both Players:");
			String l_sOwnPlatoon = sc.nextLine();
			String l_sOpponentPlatoon = sc.nextLine();
			String l_sWinningArrangement = aow.shuffleAndGetWinningArrangement(l_sOwnPlatoon, l_sOpponentPlatoon);
			System.out.println("\nWinning Arrangement/Chance:");
			System.out.println(l_sWinningArrangement);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			sc.close();
		}

	}

}
