package org.jaewanyun.plasmidplanner.struct;

public class Nu implements Comparable<Nu> {

	private static char[] valid = {'a', 'g', 't', 'c'};
	private char val;

	public Nu() {
		this('!');
	}

	public Nu(char val) {
		setVal(val);
	}

	public char getVal() {
		return val;
	}

	public void setVal(char val) {
		if(validVal(val))
			this.val = val;
		else
			System.err.println("Not a valid input for Nu.");
	}

	private static boolean validVal(char val) {
		boolean flag = false;
		for(int j = 0; j < valid.length; j++) {
			if(val == valid[j]) {
				flag = true;
			}
		}
		return flag;
	}

	/*
	 * Returns 0 if other's value equals this value
	 */
	@Override
	public int compareTo(Nu other) {
		if(this.val == other.val) {
			return 0;
		}
		return 1;
	}
}
