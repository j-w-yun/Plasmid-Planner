package org.jaewanyun.plasmidplanner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class Planner {

	private static Enzyme[] enzymes;
	private static Plasmid vector;
	private static Plasmid insert;
	private static int insertStart;
	private static int insertEnd;
	private static int vectorStart;
	private static int vectorEnd;
	private static int insertSize;
	private static int vectorSize;
	private static boolean mustLoopInsert;
	private static boolean mustLoopVector;
	private static HashMap<Integer, HashSet<Overhang>> insertLocationAndOverhang;
	private static HashMap<Integer, HashSet<Overhang>> vectorLocationAndOverhang;
	private static Solution partialSolution;
	private static volatile double digestProgress; // Keeps track of the progress of digesting. Max 1.0
	private static volatile double planningProgress; // Keeps track of the progress of planning. Max 1.0
	private static HashMap<Integer, HashSet<String>> insertLocationAndEnzymeValid;
	private static HashMap<Integer, HashSet<String>> vectorLocationAndEnzymeValid;

	/*
	 * Assumes plasmid
	 * Calculates the possible options for cloning
	 */
	public static synchronized void setPlanningData(int insertStart, int insertEnd, int vectorStart, int vectorEnd) {
		// Identify and throw exceptions
		if(vector == null || insert == null || enzymes == null)
			throw new IllegalStateException();
		if(insertStart < 0 || insertStart > insert.getLength())
			throw new IllegalArgumentException();
		if(insertEnd < 0 || insertEnd > insert.getLength())
			throw new IllegalArgumentException();
		if(vectorStart < 0 || vectorStart > vector.getLength())
			throw new IllegalArgumentException();
		if(vectorEnd < 0 || vectorEnd > vector.getLength())
			throw new IllegalArgumentException();

		// For visibility
		Planner.insertStart = insertStart;
		Planner.insertEnd = insertEnd;
		Planner.vectorStart = vectorStart;
		Planner.vectorEnd = vectorEnd;
		Planner.insertSize = insert.getLength();
		Planner.vectorSize = vector.getLength();
		Planner.mustLoopInsert = insertStart < insertEnd;
		Planner.mustLoopVector = vectorStart > vectorEnd;
	}

	private static void autoPlan() {

	}

	public static HashMap<Integer, HashSet<String>> findInsertValid(int insertStart, int insertEnd) {
		// Plan using digested information
		insertLocationAndOverhang = insert.getLocationAndOverhang();
		insertLocationAndEnzymeValid = new HashMap<>();
		HashMap<String, HashSet<Integer>> insertEnzymeAndLocation = insert.getEnzymeAndLocation();
		Set<String> insertEnzymeSet = insertEnzymeAndLocation.keySet();
		String[] insertEnzymes = insertEnzymeSet.toArray(new String[insertEnzymeSet.size()]);
		// Insert validity test
		// Run through each enzymes
		for(int j = 0; j < insertEnzymes.length; j++) {
			HashSet<Integer> insertLocationSet = insertEnzymeAndLocation.get(insertEnzymes[j]);
			int cursor = insertEnd;
			boolean valid = true;
			// Run through each location from that enzyme
			while(cursor != insertStart) {
				if(cursor == 0)
					cursor = insertSize - 1;
				if(insertLocationSet.contains(cursor)) {
					valid = false;
					break;
				}
				cursor--;
			}
			// If valid, enter into hashmap
			if(valid) {
				Integer[] insertLocations = insertLocationSet.toArray(new Integer[insertLocationSet.size()]);
				for(int k = 0; k < insertLocations.length; k++) {
					if(insertLocationAndEnzymeValid.containsKey(insertLocations[k])) {
						Set<String> enzymeSetValid = insertLocationAndEnzymeValid.get(insertLocations[k]);
						enzymeSetValid.add(insertEnzymes[j]);
					}
					else {
						HashSet<String> enzymeSetValid = new HashSet<>();
						enzymeSetValid.add(insertEnzymes[j]);
						insertLocationAndEnzymeValid.put(insertLocations[k], enzymeSetValid);
					}
					System.out.println(insertLocations[k] + " " + insertEnzymes[j]);
				}
			}
		}
		return insertLocationAndEnzymeValid;
	}

	public static HashMap<Integer, HashSet<String>> findVectorValid(int vectorStart, int vectorEnd) {
		vectorLocationAndOverhang = vector.getLocationAndOverhang();
		vectorLocationAndEnzymeValid = new HashMap<>();
		HashMap<String, HashSet<Integer>> vectorEnzymeAndLocation = vector.getEnzymeAndLocation();
		Set<String> vectorEnzymeSet = vectorEnzymeAndLocation.keySet();
		String[] vectorEnzymes = vectorEnzymeSet.toArray(new String[vectorEnzymeSet.size()]);
		// Vector validity test
		for(int j = 0; j < vectorEnzymes.length; j++) {
			HashSet<Integer> vectorLocationSet = vectorEnzymeAndLocation.get(vectorEnzymes[j]);
			int cursor = vectorStart;
			boolean valid = true;
			// Run through each location from that enzyme
			while(cursor != vectorEnd) {
				if(cursor == 0)
					cursor = vectorSize - 1;
				if(vectorLocationSet.contains(cursor)) {
					valid = false;
					break;
				}
				cursor--;
			}
			// If valid, enter into hashmap
			if(valid) {
				Integer[] vectorLocations = vectorLocationSet.toArray(new Integer[vectorLocationSet.size()]);
				for(int k = 0; k < vectorLocations.length; k++) {
					if(vectorLocationAndEnzymeValid.containsKey(vectorLocations[k])) {
						Set<String> enzymeSetValid = vectorLocationAndEnzymeValid.get(vectorLocations[k]);
						enzymeSetValid.add(vectorEnzymes[j]);
					}
					else {
						HashSet<String> enzymeSetValid = new HashSet<>();
						enzymeSetValid.add(vectorEnzymes[j]);
						vectorLocationAndEnzymeValid.put(vectorLocations[k], enzymeSetValid);
					}
				}
			}
		}
		return vectorLocationAndEnzymeValid;
	}

	/*
	 * Loads an insert from String info
	 */
	public static void setInsert(String insertSequence) {
		insert = new Plasmid(insertSequence);
	}

	/*
	 * Loads a vector from String info
	 */
	public static void setVector(String vectorSequence) {
		vector = new Plasmid(vectorSequence);
	}

	/*
	 * Loads an insert from Plasmid info
	 */
	public static void setInsert(Plasmid insert) {
		Planner.insert = insert;
	}

	/*
	 * Loads a vector from Plasmid info
	 */
	public static void setVector(Plasmid vector) {
		Planner.vector = vector;
	}

	/*
	 * Sets enzymes from an existing array
	 */
	public static void setEnzymes(Enzyme[] enzymes) {
		Planner.enzymes = enzymes;
	}

	/*
	 * Getters
	 */
	public static Enzyme[] getEnzymes() {
		return enzymes;
	}

	public static Plasmid getVector() {
		return vector;
	}

	public static Plasmid getInsert() {
		return insert;
	}

	//	public static double getDigestProgress() {
	//		return digestProgress;
	//	}
	//
	//	public static double getPlanningProgress() {
	//		return planningProgress;
	//	}
}
