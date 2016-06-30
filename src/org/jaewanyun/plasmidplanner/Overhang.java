package org.jaewanyun.plasmidplanner;


public class Overhang implements Comparable<Overhang> {

	private int hashCode = 0;
	private String overhang; // DNA sequence of overhang left from an enzyme

	/*
	 * True if the overhang is a 3 prime overhang
	 * False if the overhang is a 5 prime overhang
	 *
	 * Example:
	 * CCCAAA	after digest by a hypothetical enzyme	5' CCCAA		A 3'
	 * GGGTTT											3' G		GGTTT 5'
	 * This would result in true because the overhang is 3 prime and on original
	 */
	private final boolean overhangOnOriginal;
	private boolean blunt;

	Overhang(String overhang, boolean overhangOnOriginial) {
		this.overhang = overhang;
		this.overhangOnOriginal = overhangOnOriginial;
		if(overhang.length() == 0)
			this.blunt = true;
	}

	String getOverhang() {
		return overhang;
	}

	/*
	 * Returns true if overhang is 5'
	 */
	public boolean isFivePrime() {
		if(blunt)
			return false;
		return !overhangOnOriginal;
	}

	/*
	 * Returns true if overhang is 3'
	 */
	public boolean isThreePrime() {
		if(blunt)
			return false;
		return overhangOnOriginal;
	}

	/*
	 * Returns true if blunt
	 */
	public boolean isBlunt() {
		return blunt;
	}

	/*
	 * Returns true if complementary
	 * Complementary sequences are compared from 5' and 3',respectively; and never in the same direction such as both 5'
	 */
	public boolean isComplement(Overhang other) {
		if(this.blunt && other.blunt)
			return true;
		if(getComplement().overhang.equals(other.overhang) && (this.overhangOnOriginal != other.overhangOnOriginal))
			return true;
		return false;
	}

	/*
	 * Returns a complementary Overhang
	 * Does not utilize the overhang bank
	 */
	public Overhang getComplement() {
		//		String complementarySequence = Utility.complementSequence(overhang, false);
		//		Overhang complement = new Overhang(complementarySequence, !overhangOnOriginal);
		//		return complement;
		return this;
	}

	@Override
	public String toString() {
		String toReturn = null;
		if(isFivePrime())
			toReturn = "(5') " + overhang;
		else if(isThreePrime())
			toReturn = "(3') " + overhang;
		else if(isBlunt())
			toReturn = "Blunt";
		return toReturn;
	}

	@Override
	public int compareTo(Overhang o) {
		if(this.overhangOnOriginal && o.overhangOnOriginal || !this.overhangOnOriginal && !o.overhangOnOriginal)
			return this.overhang.compareTo(o.overhang);
		else if(this.overhangOnOriginal)
			return -1;
		else
			return 1;

	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		if(!(obj instanceof Overhang))
			return false;
		Overhang o = (Overhang)obj;

		if(this.blunt && o.blunt)
			return true;
		return this.overhang.equals(o.overhang) && this.overhangOnOriginal == o.overhangOnOriginal;
	}

	@Override
	public int hashCode() {
		if(hashCode == 0) { // Lazy initialization
			int result = 17; // Arbitrary non-zero number
			result = 37 * result + (overhangOnOriginal ? 0 : 1); // Multiply by prime
			result = 37 * result + overhang.hashCode(); // Start and end addition order matters, therefore good hash
			hashCode = result;
		}
		return hashCode;
	}
}
