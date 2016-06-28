package org.jaewanyun.plasmidplanner;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;

public class Plasmid {

	private HashMap<String, Overhang> overhangBank; // To reduce memory usage
	private final String sequence; // DNA sequence of the plasmid
	private int length; // Length of plasmidS
	private HashMap<Integer, HashSet<String>> locationAndEnzyme; // Maps location of cut to enzyme name
	private HashMap<Integer, HashSet<Overhang>> locationAndOverhang; // Maps location of cut to Overhang
	private HashMap<String, HashSet<Integer>> enzymeAndLocation; // Maps enzyme name to number of cuts
	private HashMap<Overhang, HashSet<String>> overhangAndEnzyme; // Maps overhang to enzyme name
	private HashMap<String, HashSet<Overhang>> enzymeAndOverhang; // Maps enzyme name to overhang
	/*
	 * Maps enzyme and location appended to overhang
	 * e.g. BamHI3092 to Overhang
	 */
	private HashMap<String, Overhang> enzymeAndLocationAndOverhang;

	public Plasmid(String sequence) {
		this.sequence = sequence;
		this.length = sequence.length();
		this.locationAndEnzyme = new HashMap<>();
		this.locationAndOverhang = new HashMap<>();
		this.enzymeAndLocation = new HashMap<>();
		this.overhangAndEnzyme = new HashMap<>();
		this.enzymeAndOverhang = new HashMap<>();
		this.enzymeAndLocationAndOverhang = new HashMap<>();
		this.overhangBank = new HashMap<>();
	}

	/*
	 * Returns a stored overhang to reduce memory usage
	 */
	Overhang getOverhang(String sequence, boolean overhangOnOriginal) {
		if(overhangBank.containsKey(sequence))
			return overhangBank.get(sequence);
		else {
			Overhang overhang = new Overhang(sequence, overhangOnOriginal);
			overhangBank.put(sequence, overhang);
			return overhang;
		}
	}

	/*
	 * Returns the DNA sequence of the plasmid
	 */
	public String getSequence() {
		return sequence;
	}

	/*
	 * Returns the length of the plasmid
	 */
	public int getLength() {
		return length;
	}

	/*
	 * Returns mapped location of cut to enzyme name
	 */
	public HashMap<Integer, HashSet<String>> getLocationAndEnzyme() {
		return locationAndEnzyme;
	}

	/*
	 * Returns mapped location of cut to Overhang
	 */
	public HashMap<Integer, HashSet<Overhang>> getLocationAndOverhang() {
		return locationAndOverhang;
	}

	/*
	 * Returns mapped name to number of cuts
	 */
	public HashMap<String, HashSet<Integer>> getEnzymeAndLocation() {
		return enzymeAndLocation;
	}

	/*
	 * Returns mapped Overhang to enzyme name
	 */
	public HashMap<Overhang, HashSet<String>> getOverhangAndEnzyme() {
		return overhangAndEnzyme;
	}

	/*
	 * Returns mapped enzyme name to Overhang
	 */
	public HashMap<String, HashSet<Overhang>> getEnzymeAndOverhang() {
		return enzymeAndOverhang;
	}

	/*
	 * Returns mapped enzyme name and location appended to Overhang
	 */
	public HashMap<String, Overhang> getEnzymeAndLocationAndOverhang() {
		return enzymeAndLocationAndOverhang;
	}

	/*
	 * Parses into a plasmid sequence using the contents of a file
	 */
	static Plasmid importFromFile(InputStream fileLocation) {
		String[] unparsedStrings = Utility.readFile(fileLocation);
		final String validChars = "atgcu";
		String builder = "";
		for(int j = 0; j < unparsedStrings.length; j++) {
			unparsedStrings[j] = unparsedStrings[j].toLowerCase();
			char[] nucleotides = unparsedStrings[j].toCharArray(); // Split into chars
			for(int k = 0; k < nucleotides.length; k++) {
				if(validChars.indexOf(nucleotides[k]) >= 0)
					builder += nucleotides[k];
			}
		}
		return new Plasmid(builder);
	}
}
