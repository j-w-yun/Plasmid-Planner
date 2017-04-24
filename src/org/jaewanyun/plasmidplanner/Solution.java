package org.jaewanyun.plasmidplanner;

public class Solution {

	private String insertLeftEnzyme;
	private String vectorLeftEnzyme;
	private String insertRightEnzyme;
	private String vectorRightEnzyme;
	private int insertLeftLocation;
	private int vectorLeftLocation;
	private int insertRightLocation;
	private int vectorRightLocation;
	private Overhang insertLeftOverhang;
	private Overhang vectorLeftOverhang;
	private Overhang insertRightOverhang;
	private Overhang vectorRightOverhang;
	private int finalSize;

	Solution(String ile, String vle, String ire, String vre, int ill, int vll, int irl, int vrl, Overhang ilo, Overhang vlo, Overhang iro, Overhang vro) {
		this.insertLeftEnzyme = ile;
		this.vectorLeftEnzyme = vle;
		this.insertRightEnzyme = ire;
		this.vectorRightEnzyme = vre;
		this.insertLeftLocation = ill;
		this.vectorLeftLocation = vll;
		this.insertRightLocation = irl;
		this.vectorRightLocation = vrl;
		this.insertLeftOverhang = ilo;
		this.vectorLeftOverhang = vlo;
		this.insertRightOverhang = iro;
		this.vectorRightOverhang = vro;

		int insertFragmentSize;
		if(ill < irl) {
			insertFragmentSize = irl - ill + 1;
		} else {
			insertFragmentSize = Planner.getInsert().getLength() - (ill - irl);
		}

		int vectorFragmentSize;
		if(vll > vrl) {
			vectorFragmentSize = vll - vrl + 1;
		} else {
			vectorFragmentSize = Planner.getVector().getLength() - (vrl - vll);
		}

		finalSize = insertFragmentSize + vectorFragmentSize;
	}

	public boolean conflicts(Solution other) {
		if(this.insertLeftEnzyme.equals(other.insertLeftEnzyme) && this.insertLeftLocation != other.insertLeftLocation)
			return true;
		if(this.vectorLeftEnzyme.equals(other.vectorLeftEnzyme) && this.vectorLeftLocation != other.vectorLeftLocation)
			return true;
		if(this.insertRightEnzyme.equals(other.insertRightEnzyme) && this.insertRightLocation != other.insertRightLocation)
			return true;
		if(this.vectorRightEnzyme.equals(other.vectorRightEnzyme) && this.vectorRightLocation != other.vectorRightLocation)
			return true;
		return false;
	}

	public String getInsertLeftEnzyme() {
		return insertLeftEnzyme;
	}

	public String getVectorLeftEnzyme() {
		return vectorLeftEnzyme;
	}

	public String getInsertRightEnzyme() {
		return insertRightEnzyme;
	}

	public String getVectorRightEnzyme() {
		return vectorRightEnzyme;
	}

	public int getInsertLeftLocation() {
		return insertLeftLocation;
	}

	public int getVectorLeftLocation() {
		return vectorLeftLocation;
	}

	public int getInsertRightLocation() {
		return insertRightLocation;
	}

	public int getVectorRightLocation() {
		return vectorRightLocation;
	}

	public Overhang getInsertLeftOverhang() {
		return insertLeftOverhang;
	}

	public Overhang getVectorLeftOverhang() {
		return vectorLeftOverhang;
	}

	public Overhang getInsertRightOverhang() {
		return insertRightOverhang;
	}

	public Overhang getVectorRightOverhang() {
		return vectorRightOverhang;
	}

	public int getFinalSize() {
		return finalSize;
	}
}
