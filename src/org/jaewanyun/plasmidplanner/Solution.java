package org.jaewanyun.plasmidplanner;

class Solution {

	private Enzyme insertEnzyme1; // Enzyme which cuts the left side of the insert
	private int insertEnzyme1Location;
	private Enzyme insertEnzyme2; // Enzyme which cuts the right side of the insert
	private int insertEnzyme2Location;
	private Enzyme vectorEnzyme1; // Enzyme which is compatible with insertEnzyme1
	private int vectorEnzyme1Location;
	private Enzyme vectorEnzyme2; // Enzyme which is comparible with insertEnzyme2
	private int vectorEnzyme2Location;
	private int preference; // Higher the more preferable solution

	Solution(Enzyme insertEnzyme1, int insertEnzyme1Location,
			Enzyme insertEnzyme2, int insertEnzyme2Location,
			Enzyme vectorEnzyme1, int vectorEnzyme1Location,
			Enzyme vectorEnzyme2, int vectorEnzyme2Location) {
		this.insertEnzyme1 = insertEnzyme1;
		this.insertEnzyme1Location = insertEnzyme1Location;
		this.insertEnzyme2 = insertEnzyme2;
		this.insertEnzyme2Location = insertEnzyme2Location;
		this.vectorEnzyme1 = vectorEnzyme1;
		this.vectorEnzyme1Location = vectorEnzyme1Location;
		this.vectorEnzyme2 = vectorEnzyme2;
		this.vectorEnzyme2Location = vectorEnzyme2Location;
	}

	/*
	 * With an element that will be used for sorting
	 */
	Solution(Enzyme insertEnzyme1, int insertEnzyme1Location,
			Enzyme insertEnzyme2, int insertEnzyme2Location,
			Enzyme vectorEnzyme1, int vectorEnzyme1Location,
			Enzyme vectorEnzyme2, int vectorEnzyme2Location,
			int preference) {
		this(insertEnzyme1, insertEnzyme1Location,
				insertEnzyme2, insertEnzyme2Location,
				vectorEnzyme1, vectorEnzyme1Location,
				vectorEnzyme2, vectorEnzyme2Location);
		this.preference = preference;
	}

	Solution() {
		this(null, 0, null, 0, null, 0, null, 0);
	}

	boolean isFullSolution() {
		return (insertEnzyme1 != null && insertEnzyme2 != null && vectorEnzyme1 != null && vectorEnzyme2 != null);
	}

	void clear() {
		this.insertEnzyme1 = null;
		this.insertEnzyme1Location = 0;
		this.insertEnzyme2 = null;
		this.insertEnzyme2Location = 0;
		this.vectorEnzyme1 = null;
		this.vectorEnzyme1Location = 0;
		this.vectorEnzyme2 = null;
		this.vectorEnzyme2Location = 0;
		this.preference = 0;
	}

	Enzyme getInsertEnzyme1() {
		return insertEnzyme1;
	}
	void setInsertEnzyme1(Enzyme e) {
		this.insertEnzyme1 = e;
	}

	int getInsertEnzyme1Location() {
		return insertEnzyme1Location;
	}
	void setInsertEnzyme1Location(int loc) {
		this.insertEnzyme1Location = loc;
	}

	Enzyme getInsertEnzyme2() {
		return insertEnzyme2;
	}
	void setInsertEnzyme2(Enzyme e) {
		this.insertEnzyme2 = e;
	}

	int getInsertEnzyme2Location() {
		return insertEnzyme2Location;
	}
	void setInsertEnzyme2Location(int loc) {
		this.insertEnzyme2Location = loc;
	}

	Enzyme getVectorEnzyme1() {
		return vectorEnzyme1;
	}
	void setVectorEnzyme1(Enzyme e) {
		this.vectorEnzyme1 = e;
	}

	int getVectorEnzyme1Location() {
		return vectorEnzyme1Location;
	}
	void setVectorEnzyme1Location(int loc) {
		this.vectorEnzyme1Location = loc;
	}

	Enzyme getVectorEnzyme2() {
		return vectorEnzyme2;
	}
	void setVectorEnzyme2(Enzyme e) {
		this.vectorEnzyme2 = e;
	}

	int getVectorEnzyme2Location() {
		return vectorEnzyme2Location;
	}
	void setVectorEnzyme2Location(int loc) {
		this.vectorEnzyme2Location = loc;
	}

	int getPreference() {
		return preference;
	}
	void setPreference(int preference) {
		this.preference = preference;
	}
}
