package org.jaewanyun.plasmidplanner;

final class SiteRange {

	private volatile int hashCode = 0;
	int start;
	int end;
	boolean valid;

	/*
	 * The only invalid case
	 */
	SiteRange() {
		this.valid = false;
	}

	SiteRange(int start, int end) {
		set(start, end);
	}

	/*
	 * Stores overhang sequence start and end points on the plasmid and its sequence
	 */
	void set(int start, int end) {
		this.start = start;
		this.end = end;
		this.valid = true;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		if(!(obj instanceof SiteRange))
			return false;
		SiteRange site = (SiteRange)obj;

		return (this.start == site.start) && (this.end == site.end) && (this.valid == site.valid);
	}

	@Override
	public int hashCode() {
		if(hashCode == 0) { // Lazy initialization
			int result = 17; // Arbitrary non-zero number
			result = 37 * result + start; // Multiply by prime
			result = 37 * result + end; // Start and end addition order matters, therefore good hash
			hashCode = result;
		}
		return hashCode;
	}
}
