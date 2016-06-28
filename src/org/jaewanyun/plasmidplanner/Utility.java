package org.jaewanyun.plasmidplanner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Utility {

	private static long _maxMemory;
	private static long _allocatedMemory;
	private static long _freeMemory;
	private static long _totalFreeMemory;

	static void checkMem() {
		Runtime runtime = Runtime.getRuntime();
		NumberFormat format = NumberFormat.getInstance();
		StringBuilder sb = new StringBuilder();
		long maxMemory = runtime.maxMemory() / 1024;
		long allocatedMemory = runtime.totalMemory() / 1024;
		long freeMemory = runtime.freeMemory() / 1024;
		long totalFreeMemory = (freeMemory + (maxMemory - allocatedMemory));
		sb.append("free:\t" + format.format(freeMemory) + "\t");
		sb.append("alloc:\t" + format.format(allocatedMemory) + "\t");
		sb.append("max:\t" + format.format(maxMemory) + "\t");
		sb.append("tot fr:\t" + format.format(totalFreeMemory));
		System.out.println(sb);

		sb = new StringBuilder();
		sb.append("delta:\t" + format.format(freeMemory - _freeMemory) + "\t");
		sb.append("delta:\t" + format.format(allocatedMemory - _allocatedMemory) + "\t");
		sb.append("delta:\t" + format.format(maxMemory - _maxMemory) + "\t");
		sb.append("delta:\t" + format.format(totalFreeMemory - _totalFreeMemory));
		System.out.println(sb);

		_maxMemory = maxMemory;
		_allocatedMemory = allocatedMemory;
		_freeMemory = freeMemory;
		_totalFreeMemory = totalFreeMemory;
	}

	/*
	 * Returns an array of String from file
	 * Each line is its own String
	 */
	static String[] readFile(InputStream location) {
		ArrayList<String> readLines = new ArrayList<>();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(location))) {
			String line = br.readLine();
			while(line != null) {
				readLines.add(line);
				line = br.readLine();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return readLines.toArray(new String[readLines.size()]);
	}

	/*
	 * Enumerates all possible combinations of the sequence based on nucleotide keys
	 */
	static String enumerate(String sequence) {
		//		sequence = sequence.toLowerCase();
		String regexSequence = "";

		for(int j = 0; j < sequence.length(); j++) {
			char nucleotide = sequence.charAt(j);
			if(nucleotide == 'a' || nucleotide == 't' || nucleotide == 'g' || nucleotide == 'c' || nucleotide == 'u')
				regexSequence += nucleotide;
			else if(nucleotide == 'w')
				regexSequence += "[at]{1}";
			else if(nucleotide == 's')
				regexSequence += "[cg]{1}";
			else if(nucleotide == 'm')
				regexSequence += "[ac]{1}";
			else if(nucleotide == 'k')
				regexSequence += "[tg]{1}";
			else if(nucleotide == 'r')
				regexSequence += "[ag]{1}";
			else if(nucleotide == 'y')
				regexSequence += "[ct]{1}";
			else if(nucleotide == 'b')
				regexSequence += "[ctg]{1}";
			else if(nucleotide == 'd')
				regexSequence += "[atg]{1}";
			else if(nucleotide == 'h')
				regexSequence += "[atc]{1}";
			else if(nucleotide == 'v')
				regexSequence += "[acg]{1}";
			else if(nucleotide == 'n')
				regexSequence += "[actg]{1}";
			else
			{
				System.out.println("Unknown nucleotide");
				System.exit(1);
			}
		}

		return regexSequence;
	}

	/*
	 * Returns the complementary sequence of the argument string
	 */
	public static String complementSequence(String sequence, boolean orderFromFivePrime) {
		sequence = sequence.toLowerCase();
		StringBuilder complementarySequence = new StringBuilder();

		for(int j = 0; j < sequence.length(); j++) {
			// ATU
			if(sequence.charAt(j) == 'a')
				complementarySequence.append("t");
			else if(sequence.charAt(j) == 't' || sequence.charAt(j)== 'u')
				complementarySequence.append("a");
			// GC
			else if(sequence.charAt(j) == 'g')
				complementarySequence.append("c");
			else if(sequence.charAt(j) == 'c')
				complementarySequence.append("g");
			// RY
			else if(sequence.charAt(j) == 'r')
				complementarySequence.append("y");
			else if(sequence.charAt(j) == 'y')
				complementarySequence.append("r");
			// BV
			else if(sequence.charAt(j) == 'b')
				complementarySequence.append("v");
			else if(sequence.charAt(j) == 'v')
				complementarySequence.append("b");
			// DH
			else if(sequence.charAt(j) == 'd')
				complementarySequence.append("h");
			else if(sequence.charAt(j) == 'h')
				complementarySequence.append("d");
			// MK
			else if(sequence.charAt(j) == 'm')
				complementarySequence.append("k");
			else if(sequence.charAt(j) == 'k')
				complementarySequence.append("m");
			// WSN
			else if(sequence.charAt(j) == 'w')
				complementarySequence.append("w");
			else if(sequence.charAt(j) == 's')
				complementarySequence.append("s");
			else if(sequence.charAt(j) == 'n')
				complementarySequence.append("n");
			else
				throw new IllegalArgumentException();
		}

		if(orderFromFivePrime)
			return complementarySequence.reverse().toString();
		else
			return complementarySequence.toString();
	}
}
