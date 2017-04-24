package org.jaewanyun.plasmidplanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingWorker;

import org.jaewanyun.plasmidplanner.gui.ProgressPanel;
import org.jaewanyun.plasmidplanner.gui.plan.TabbedPlanningPane;
import org.jaewanyun.plasmidplanner.gui.plan.ViewSolutionsPanel;

public final class Planner {

	private static Enzyme[] enzymes;
	private static Plasmid vector;
	private static Plasmid insert;
	private static HashMap<Integer, HashSet<String>> insertLocationAndEnzymeValid;
	private static HashMap<Integer, HashSet<String>> vectorLocationAndEnzymeValid;
	private static Set<Integer> insertLocationSet;
	private static Set<Integer> vectorLocationSet;
	private static ArrayList<Solution> solutionList;

	/*
	 * Calculates the possible options for cloning
	 */
	public static synchronized void plan(int insertStart, int insertEnd, int vectorStart, int vectorEnd) {
		/*
		 * Identify and throw exceptions
		 */
		if(vector == null || insert == null || enzymes == null)
			throw new IllegalStateException();

		solutionList = new ArrayList<>();

		new SwingWorker<Void, Integer>() {
			Integer id = ProgressPanel.createTask("Planning Expression Vector", 100, this);

			@Override
			protected Void doInBackground(){
				/*
				 * Identify and throw exceptions
				 */
				if(insertStart < 0 || insertStart > insert.getLength())
					return null;
				if(insertEnd < 0 || insertEnd > insert.getLength())
					return null;
				if(vectorStart < 0 || vectorStart > vector.getLength())
					return null;
				if(vectorEnd < 0 || vectorEnd > vector.getLength())
					return null;

				/*
				 * Prepare necessary data
				 */
				insertLocationAndEnzymeValid = findEnzymesNotCutting(insert, insertStart, insertEnd);
				vectorLocationAndEnzymeValid = findEnzymesNotCutting(vector, vectorEnd, vectorStart);
				insertLocationSet = insertLocationAndEnzymeValid.keySet();
				vectorLocationSet = vectorLocationAndEnzymeValid.keySet();

				/*
				 * Progress report data
				 */
				int insertSize = 0;
				if(insertStart > insertEnd)
					insertSize = insertStart - insertEnd;
				else
					insertSize = insert.getLength() - (insertEnd - insertStart);
				int progress = 0;

				/*
				 * (CASE LOOP-AROUND)
				 * INSERT				<-3-2-1 |KEEP REGION==========KEEP REGION| 7-8-9->
				 * 						5'----------------------------------------------3'
				 *
				 * (CASE NORMAL)
				 * VECTOR				5'----------------------------------------------3'
				 *						=KEEP REGION| 4-5-6->     <-12-11-10 |KEEP REGION=
				 *
				 * (CASE NORMAL)
				 * INSERT				=KEEP REGION| 7-8-9->        <-3-2-1 |KEEP REGION=
				 * 						5'----------------------------------------------3'
				 *
				 * (CASE LOOP-AROUND)
				 * VECTOR				5'----------------------------------------------3'
				 * 						<-12-11-10 |KEEP REGION=======KEEP REGION| 4-5-6->
				 */

				/*
				 * Read insert right to left
				 */
				int insertCursor1 = insertStart; // TODO: exclusive (insertStart - 1)
				while(insertCursor1 != insertEnd) { // Exclusive
					if(insertCursor1 == -1)
						insertCursor1 = insert.getLength() - 1; // Loop around
					if(insertLocationSet.contains(insertCursor1)) {

						// Get all overhangs at the location from step (1)
						Overhang[] stepOneOverhangs = getOverhangs(insert, insertCursor1, insertLocationAndEnzymeValid.get(insertCursor1));

						/*
						 * Read vector left to right
						 */
						int vectorCursor1 = vectorStart; // Inclusive
						while(vectorCursor1 != vectorEnd) { // Inclusive
							if(vectorCursor1 == vector.getLength())
								vectorCursor1 = 0; // Loop around
							if(vectorLocationSet.contains(vectorCursor1)) {

								/*
								 * Get all overhangs at the location from step (2)
								 * Check if any is compatible with the overhangs from step (1)
								 * If none are compatible, continue without proceeding below
								 */
								Overhang[] stepTwoOverhangs = getOverhangs(vector, vectorCursor1, vectorLocationAndEnzymeValid.get(vectorCursor1));
								if(hasCommon(stepOneOverhangs, stepTwoOverhangs)) {
									// TODO : iterate below for each leftSideOverhangs
									Overhang[] leftSideOverhangs = inCommon(stepOneOverhangs, stepTwoOverhangs);

									/*
									 * Read insert left to right
									 */
									int insertCursor2 = insertEnd; // TODO: exclusive (insertEnd + 1)
									while(insertCursor2 != insertCursor1 + 1) { // Cursors should always be inclusive
										if(insertCursor2 == insert.getLength())
											insertCursor2 = 0; // Loop around
										if(insertLocationSet.contains(insertCursor2)) {

											// Get all overhangs at the location from step (3)
											Overhang[] stepThreeOverhangs = getOverhangs(insert, insertCursor2, insertLocationAndEnzymeValid.get(insertCursor2));

											/*
											 * Read vector right to left
											 */
											int vectorCursor2 = vectorEnd; // Inclusive
											while(vectorCursor2 != vectorCursor1 - 1) { // Cursors should always be inclusive
												if(vectorCursor2 == -1)
													vectorCursor2 = vector.getLength() - 1; // Loop around
												if(vectorLocationSet.contains(vectorCursor2)) {

													/*
													 * Get all overhangs at the location from step (4)
													 * Check if any is compatible with the overhangs from step (3)
													 * If none are compatible, continue without proceeding below
													 */
													Overhang[] stepFourOverhangs = getOverhangs(vector, vectorCursor2, vectorLocationAndEnzymeValid.get(vectorCursor2));
													if(hasCommon(stepThreeOverhangs, stepFourOverhangs)) {

														// TODO : iterate below for each rightSideOverhangs
														Overhang[] rightSideOverhangs = inCommon(stepThreeOverhangs, stepFourOverhangs);

														/*
														 * Found a solution
														 */
														String[] insertLeftEnzymes = getEnzymes(insert, insertCursor1, leftSideOverhangs[0], insertLocationAndEnzymeValid.get(insertCursor1));
														String[] vectorLeftEnzymes = getEnzymes(vector, vectorCursor1, leftSideOverhangs[0], vectorLocationAndEnzymeValid.get(vectorCursor1));
														String[] insertRightEnzymes = getEnzymes(insert, insertCursor2, rightSideOverhangs[0], insertLocationAndEnzymeValid.get(insertCursor2));
														String[] vectorRightEnzymes = getEnzymes(vector, vectorCursor2, rightSideOverhangs[0], vectorLocationAndEnzymeValid.get(vectorCursor2));

														/*
														 * Skip if one enzyme interferes with another's ability to leave a compatible site
														 */
														if(!Planner.haveInterferingSites(insert, insertLeftEnzymes[0], insertCursor1, insertCursor2) &&
																!Planner.haveInterferingSites(insert, insertRightEnzymes[0], insertCursor1, insertCursor2) &&
																!Planner.haveInterferingSites(vector, vectorLeftEnzymes[0], vectorCursor2, vectorCursor1) &&
																!Planner.haveInterferingSites(vector, vectorRightEnzymes[0], vectorCursor2, vectorCursor1)) {
															Solution possibleSolution = new Solution(
																	insertLeftEnzymes[0], vectorLeftEnzymes[0], insertRightEnzymes[0], vectorRightEnzymes[0],
																	insertCursor1, vectorCursor1, insertCursor2, vectorCursor2,
																	leftSideOverhangs[0], leftSideOverhangs[0], rightSideOverhangs[0], rightSideOverhangs[0]);

															/*
															 * Skip if a compatible site is not located on the fragment with the gene specified by the user to save
															 */
															boolean valid = true;
															for(int j = 0; j < solutionList.size(); j++) {
																if(solutionList.get(j).conflicts(possibleSolution))
																	valid = false;
															}
															if(valid) {

																/*
																 * Add to solution
																 */
																solutionList.add(possibleSolution);
															}
														}

													} // End if comparing (3) and (4)

												} // Proceeding from step (4) -> solution
												vectorCursor2--;
											} // End step (4) while

										} // Proceeding from step (3) -> step (4)
										insertCursor2++;
									} // End step (3) while

								} // End if comparing (1) and (2)

							} // Proceeding from step (2) -> step (3)
							vectorCursor1++;
						} // End step (2) while

					} // Proceeding from step (1) -> step (2)
					insertCursor1--;

					// Report progress
					publish((int) (((double) progress++ / insertSize) * 100));

				} // End step (1) while

				return null;
			}

			@Override
			protected void process(List<Integer> progress) {
				if(isCancelled()) {
					ProgressPanel.setDescription(id, "Canceling Task");
					return;
				}
				// Set progress
				ProgressPanel.setProgress(id, progress.get(progress.size() - 1));
			}

			@Override
			public void done() {
				if(!isCancelled()) {
					ProgressPanel.setDescription(id, "Writing Results");

					/*
					 * Update to planning tabs
					 */
					ViewSolutionsPanel.update();

					/*
					 * Goto view
					 */
					TabbedPlanningPane.setSelectedSolutionsPanel();
				}
				ProgressPanel.clear(id);
			}
		}.execute();
	}

	/*
	 * Returns true if one Overhang[] contains an element in the other Overhang[]
	 */
	private static boolean hasCommon(Overhang[] one, Overhang[] two) {
		for(int j = 0; j < one.length; j++) {
			for(int k = 0; k < two.length; k++) {
				if(one[j].equals(two[k]))
					return true;
			}
		}
		return false;
	}

	/*
	 * Returns overhangs that are common
	 */
	private static Overhang[] inCommon(Overhang[] one, Overhang[] two) {
		ArrayList<Overhang> overhangs = new ArrayList<>();
		for(int j = 0; j < one.length; j++) {
			for(int k = 0; k < two.length; k++) {
				if(one[j].equals(two[k]))
					overhangs.add(one[j]);
			}
		}
		return overhangs.toArray(new Overhang[overhangs.size()]);
	}


	/*
	 * Returns all overhangs found at the location
	 * Returns null if no overhangs with the given signature is found
	 */
	private static Overhang[] getOverhangs(Plasmid plasmid, int location, HashSet<String> enzymeSet) {
		String[] enzymes = enzymeSet.toArray(new String[enzymeSet.size()]);

		ArrayList<Overhang> overhangs = new ArrayList<>();

		// Independent of enzymes so only run this once
		HashMap<Integer, HashSet<Overhang>> locationAndOverhang = plasmid.getLocationAndOverhang();
		HashSet<Overhang> overhangSet1 = locationAndOverhang.get(location);
		Overhang[] overhangs1 = overhangSet1.toArray(new Overhang[overhangSet1.size()]);

		for(int j = 0; j < enzymes.length; j++) {
			// Dependent of enzymes so run this for every enzyme
			HashMap<String, HashSet<Overhang>> enzymeAndOverhang = plasmid.getEnzymeAndOverhang();
			HashSet<Overhang> overhangSet2 = enzymeAndOverhang.get(enzymes[j]);
			Overhang[] overhangs2 = overhangSet2.toArray(new Overhang[overhangSet2.size()]);

			// Calculate the overhangs generated from only the valid enzymes
			Overhang[] common = inCommon(overhangs1, overhangs2);

			for(int k = 0; k < common.length; k++) {
				overhangs.add(common[k]);
			}
		}

		return overhangs.toArray(new Overhang[overhangs.size()]);
	}

	/*
	 * Returns all enzymes with the given signature
	 * Returns null if no enzymes with the given signature is found
	 */
	private static String[] getEnzymes(Plasmid plasmid, int location, Overhang overhang, HashSet<String> enzymesSet) {
		HashMap<String, Overhang> enzymeAndLocationAndOverhang = plasmid.getEnzymeAndLocationAndOverhang();

		ArrayList<String> foundEnzymes = new ArrayList<>();

		String[] enzymes = enzymesSet.toArray(new String[enzymesSet.size()]);
		for(int j = 0; j < enzymes.length; j++) {
			Overhang returned = enzymeAndLocationAndOverhang.get(enzymes[j] + Integer.toString(location));

			if(returned.equals(overhang))
				foundEnzymes.add(enzymes[j]);
		}

		return foundEnzymes.toArray(new String[foundEnzymes.size()]);
	}

	/*
	 * Returns true if either enzyme has at least two cutting sites encapsulating the other
	 * Returns false if both enzymes have cutting sites that are all either greater than or less than all the cutting sites of the other
	 */
	private static boolean haveInterferingSites(Plasmid plasmid, String enzyme, int from, int to) {
		HashMap<String, HashSet<Integer>> plasmidEnzymeAndLocation = plasmid.getEnzymeAndLocation();
		HashSet<Integer> locationSet = plasmidEnzymeAndLocation.get(enzyme);
		Integer[] locations = locationSet.toArray(new Integer[locationSet.size()]);

		for(Integer location : locations) {
			if(to > from) {
				if(location > from && location < to)
					return true;
			}
			else if(from > to) {
				if(!(location >= to && location <= from))
					return true;
			} else if(from == to) {
				if(locations.length > 1)
					return true;
			}

		}
		return false;
	}

	/*
	 * Finds the locations that the specified enzyme cuts for insert plasmid
	 */
	public static HashMap<Integer, HashSet<String>> getList(Plasmid plasmid, String find) {
		if(plasmid == null)
			throw new IllegalStateException();
		HashMap<String, HashSet<Integer>> enzymeAndLocation = plasmid.getEnzymeAndLocation();
		Set<String> enzymeSet = enzymeAndLocation.keySet();
		String[] enzymes = enzymeSet.toArray(new String[enzymeSet.size()]);

		HashMap<Integer, HashSet<String>> toReturn = new HashMap<>();

		for(int j = 0; j < enzymes.length; j++) {
			if(enzymes[j].equals(find) && enzymeSet.contains(find)) {

				Set<Integer> locationSet = enzymeAndLocation.get(enzymes[j]);
				Integer[] locations = locationSet.toArray(new Integer[locationSet.size()]);

				for(int k = 0; k < locations.length; k++) {
					if(toReturn.get(locations[k]) == null) {
						HashSet<String> enzymeSet2 = new HashSet<>();
						enzymeSet2.add(enzymes[j]);
						toReturn.put(locations[k], enzymeSet2);
					}
					else {
						HashSet<String> enzymeSet2 = toReturn.get(locations[k]);
						enzymeSet2.add(enzymes[j]);
					}
				}
			}
		}
		return toReturn;
	}

	public static HashMap<Integer, HashSet<String>> findEnzymesNotCutting(Plasmid thisPlasmid, int start, int end) {
		//			HashMap<Integer, HashSet<Overhang>> locationAndOverhang = thisPlasmid.getLocationAndOverhang();
		HashMap<Integer, HashSet<String>> locationAndEnzymeValid = new HashMap<>();
		HashMap<String, HashSet<Integer>> enzymeAndLocation = thisPlasmid.getEnzymeAndLocation();
		Set<String> enzymeSet = enzymeAndLocation.keySet();
		String[] enzymes = enzymeSet.toArray(new String[enzymeSet.size()]);
		// Vector validity test
		for(int j = 0; j < enzymes.length; j++) {
			HashSet<Integer> locationSet = enzymeAndLocation.get(enzymes[j]);
			int cursor = start;
			boolean valid = true;
			// Run through each location from that enzyme
			while(cursor != end) {
				if(cursor == thisPlasmid.getLength())
					cursor = 0;
				if(locationSet.contains(cursor)) {
					valid = false;
					break;
				}
				cursor++;
			}
			// If valid, enter into hashmap
			if(valid) {
				Integer[] locations = locationSet.toArray(new Integer[locationSet.size()]);
				for(int k = 0; k < locations.length; k++) {
					if(locationAndEnzymeValid.containsKey(locations[k])) {
						Set<String> enzymeSetValid = locationAndEnzymeValid.get(locations[k]);
						enzymeSetValid.add(enzymes[j]);
					}
					else {
						HashSet<String> enzymeSetValid = new HashSet<>();
						enzymeSetValid.add(enzymes[j]);
						locationAndEnzymeValid.put(locations[k], enzymeSetValid);
					}
				}
			}
		}
		return locationAndEnzymeValid;
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

	public static Solution[] getSolutions() {
		if(solutionList != null)
			return solutionList.toArray(new Solution[solutionList.size()]);
		return null;
	}
}
