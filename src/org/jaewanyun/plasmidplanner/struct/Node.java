package org.jaewanyun.plasmidplanner.struct;

import java.util.ArrayList;

public class Node<T extends Comparable<T>> {

	private T val;
	private ArrayList<Node<T>> children;

	public Node(T val) {
		this.val = val;
	}

	public Node<T> relaventChild(T val) {
		for(Node<T> node : children) {

			// If they equal, return currentNode
			if(node.getVal() == val) {
				return node;
			}
		}

		// If not found, return null
		return null;
	}

	public void addChild(Node<T> child) {
		this.children.add(child);
	}

	public void removeChild(Node<T> child) {
		this.children.remove(child);
	}

	public ArrayList<Node<T>> getChildren() {
		return children;
	}

	public T getVal() {
		return val;
	}

	public void setVal(T val) {
		this.val = val;
	}
}
