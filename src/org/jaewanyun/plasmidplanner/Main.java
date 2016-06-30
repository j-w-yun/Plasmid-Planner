package org.jaewanyun.plasmidplanner;

import javax.swing.SwingUtilities;

import org.jaewanyun.plasmidplanner.gui.MainFrame;

public class Main {

	public static MainFrame mainFrame;

	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					mainFrame = MainFrame.createGUI();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

//public class Main {
//
//	private static Enzyme[] enzymes;
//	private static Plasmid plasmid;
//	private static Planner planner;
//
//	public static void main(String[] args) {
//
//		MainFrame mainFrame = new MainFrame("Plasmid Planner");
//
//		// Enzymes import test
//		//		enzymes = importEnzymesTest();
//		//		displayEnzymesTest();
//
//
//		// Plasmid import test
//		//		plasmid = importPlasmidTest();
//		//		displayPlasmidTest();
//
//
//		// Overhang complement test
//		//		overhangComplementTest();
//
//
//		// Digest test
//		//		int progress = 0;
//		//		for(int j = 0; j < enzymes.length; j++) {
//		//			progress++;
//		//			int percentProgress = (progress * 100) / enzymes.length;
//		//			System.out.println(percentProgress + "% complete");
//		//
//		//			enzymes[j].digest(plasmid);
//		//		}
//		//		locationAndEnzymeTest();
//		//		enzymeAndLocationTest();
//		//		locationAndOverhangTest();
//		//		overhangAndEnzymeTest();
//
//
//		//		plannerTest();
//
//
//		// Memory usage check
//		//		Utility.checkMem();
//	}
//
//	private static Enzyme[] importEnzymesTest() {
//		String fileLocation = "src/org/jaewanyun/plasmidplanner/enzymes.txt";
//		return Enzyme.importFromFile(fileLocation);
//	}
//
//	private static void displayEnzymesTest() {
//		for(int j = 0; j < enzymes.length; j++) {
//			System.out.println(enzymes[j]);
//		}
//	}
//
//	private static Plasmid importPlasmidTest() {
//		String fileLocation = "src/org/jaewanyun/plasmidplanner/plasmid.txt";
//		return Plasmid.importFromFile(fileLocation);
//	}
//
//	private static void displayPlasmidTest() {
//		String sequence = plasmid.getSequence();
//		for(int j = 0; j < sequence.length(); j++) {
//			System.out.print(sequence.charAt(j));
//			if(j % 50 == 49)
//				System.out.println();
//		}
//		System.out.println("\n" + sequence.length());
//	}
//
//	private static void overhangComplementTest() {
//		Overhang overhang = new Overhang("cccaaa", false);
//		Overhang complement = overhang.getComplement();
//		String prime = "Not set";
//		if(overhang.isFivePrime())
//			prime = "5";
//		else if(overhang.isThreePrime())
//			prime = "3";
//		else if(overhang.isBlunt())
//			prime = "0";
//		System.out.println("original :\t(" + prime + "') " + overhang.getOverhang());
//		prime = "Not set";
//		if(complement.isFivePrime())
//			prime = "5";
//		else if(overhang.isThreePrime())
//			prime = "3";
//		else if(overhang.isBlunt())
//			prime = "0";
//		System.out.println("generated :\t(" + prime + "') " + complement.getOverhang());
//
//		System.out.println("original.isComplement(generated) returns : " + overhang.isComplement(complement));
//		System.out.println("generated.isComplement(original) returns : " + complement.isComplement(overhang));
//		System.out.println("original.isComplement(original) returns : " + overhang.isComplement(overhang));
//		System.out.println("generated.isComplement(generated) returns : " + complement.isComplement(complement));
//	}
//
//	private static void enzymeAndLocationTest() {
//		HashMap<String, HashSet<Integer>> enzymeAndLocation = plasmid.getEnzymeAndLocation(); // Test 1
//		for(int j = 0; j < enzymes.length; j++) {
//			if(enzymeAndLocation.containsKey(enzymes[j].getEnzymeName())) {
//				HashSet<Integer> locationSet = enzymeAndLocation.get(enzymes[j].getEnzymeName());
//				Integer[] locations = locationSet.toArray(new Integer[locationSet.size()]);
//				Arrays.sort(locations);
//				System.out.print(enzymes[j].getEnzymeName() + " : CUTS " + locations.length + " : ");
//				for(int k = 0; k < locations.length; k++) {
//					System.out.print("[" + locations[k] + "], ");
//				}
//				System.out.println();
//			}
//			else {
//				System.out.println(enzymes[j].getEnzymeName() + " : NO CUTS AVAILABLE");
//			}
//		}
//	}
//
//	private static void locationAndOverhangTest() {
//		HashMap<Integer, HashSet<Overhang>> locationAndOverhang = plasmid.getLocationAndOverhang(); // Test 2
//		Set<Integer> keySet = locationAndOverhang.keySet();
//		Integer[] locations = keySet.toArray(new Integer[keySet.size()]);
//		Arrays.sort(locations);
//		for(int j = 0; j < locations.length; j++) {
//			System.out.print(locations[j] + " : ");
//			Set<Overhang> overhangSet = locationAndOverhang.get(locations[j]);
//			Overhang[] overhangs = overhangSet.toArray(new Overhang[overhangSet.size()]);
//			for(int k = 0; k < overhangs.length; k++) {
//				if(overhangs[k].isFivePrime())
//					System.out.print("[(5') " + overhangs[k].getOverhang() + "], ");
//				else if(overhangs[k].isThreePrime())
//					System.out.print("[(3') " + overhangs[k].getOverhang() + "], ");
//				else if(overhangs[k].isBlunt())
//					System.out.print("[(BLUNT)], ");
//			}
//			System.out.println();
//		}
//	}
//
//	private static void locationAndEnzymeTest() {
//		HashMap<Integer, HashSet<String>> locationAndEnzyme = plasmid.getLocationAndEnzyme(); // Test 3
//		Set<Integer> locationSet = locationAndEnzyme.keySet();
//		Integer[] locations = locationSet.toArray(new Integer[locationSet.size()]);
//		Arrays.sort(locations);
//		for(int j = 0; j < locations.length; j++) {
//			System.out.print(locations[j] + " : ");
//			Set<String> enzymeSet = locationAndEnzyme.get(locations[j]);
//			String[] enzymes = enzymeSet.toArray(new String[enzymeSet.size()]);
//			for(int k = 0; k < enzymes.length; k++) {
//				System.out.print("[" + enzymes[k] + "], ");
//			}
//			System.out.println();
//		}
//	}
//
//	private static void overhangAndEnzymeTest() {
//		HashMap<Overhang, HashSet<String>> overhangAndEnzyme = plasmid.getOverhangAndEnzyme(); // Test 4
//		Set<Overhang> overhangSet = overhangAndEnzyme.keySet();
//		Overhang[] overhangs = overhangSet.toArray(new Overhang[overhangSet.size()]);
//		for(int j = 0; j < overhangs.length; j++) {
//			if(overhangs[j].isFivePrime())
//				System.out.print("(5') " + overhangs[j].getOverhang() + " : ");
//			else if(overhangs[j].isThreePrime())
//				System.out.print("(3') " + overhangs[j].getOverhang() + " : ");
//			else if(overhangs[j].isBlunt())
//				System.out.print("(BLUNT) : ");
//			Set<String> enzymeSet = overhangAndEnzyme.get(overhangs[j]);
//			String[] enzymes = enzymeSet.toArray(new String[enzymeSet.size()]);
//			for(int k = 0; k < enzymes.length; k++) {
//				System.out.print("[" + enzymes[k] + "], ");
//			}
//			System.out.println();
//		}
//	}
//
//	private static void plannerTest() {
//		planner = new Planner("src/org/jaewanyun/plasmidplanner/enzymes.txt");
//		planner.loadInsert("src/org/jaewanyun/plasmidplanner/insert.txt");
//		planner.loadVector("src/org/jaewanyun/plasmidplanner/vector.txt");
//		System.out.println("Insert Length : " + planner.getInsert().getLength());
//		System.out.println("Vector Length : " + planner.getVector().getLength());
//		// insertStart, insertEnd, vectorStart, vectorEnd
//		planner.plan(100, 7160, 100, 600);
//	}
//}

