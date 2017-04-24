package org.jaewanyun.plasmidplanner.struct;

public class Tree<T extends Comparable<T>> {

	private Node<T> rootNode;
	private int numNodes;

	/*
	 * Const
	 */
	public Tree() {
		this(null);
	}
	public Tree(Node<T> rootNode) {
		this.rootNode = rootNode;
		this.numNodes = 0;
	}

	/*
	 * G/S
	 */
	public Node<T> getRoot() {
		return rootNode;
	}
	public void setRoot(Node<T> rootNode) {
		this.rootNode = rootNode;
	}

	/*
	 * Generic
	 */
	public int size() {
		return numNodes;
	}
}
