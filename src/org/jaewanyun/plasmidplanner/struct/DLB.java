//package org.jaewanyun.plasmidplanner.struct;
//
//public class DLB extends Tree<Nu> {
//
//	public void removeSequence(String sequence) {
//		throw new UnsupportedOperationException("Operation not implemented.");
//	}
//
//	public void addSequence(String sequence) {
//
//
//
//	}
//
//	public boolean contains(String sequence) {
//		// Root should not contain a char to be checked
//		Node<Nu> currentNode = getRoot();
//
//		// For each possible combinations
//		for(Node<Nu> node : currentNode.getChildren()) {
//
//			// For each character in the sequence
//			for(int j = 0; j < sequence.length(); j++) {
//
//				// When current character-matches eventually iterate to its valid child node
//				if(sequence.charAt(j) == currentNode.getVal().getVal()) {
//
//
//
//				} else {
//					// Terminate search
//					return false;
//				}
//			}
//		}
//	}
//}
