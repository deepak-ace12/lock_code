package com.java;

import java.util.HashSet;

public class Node {
	
	public Integer number;
	public int allowedPosition;
	public HashSet<Integer> notAllowedPositions;
	public HashSet<Integer> groups;
	public boolean isElemenated;
	
	public Node(int n) {
		number = n;
		notAllowedPositions = new HashSet<Integer>();
		groups = new HashSet<Integer>();
		isElemenated = false;
	}
	

	public void insertValues(int position, int groupNumber) {
		if(groupNumber == 1) {
			allowedPosition = position;
		} else {
			notAllowedPositions.add(position);
			if (allowedPosition == position || groupNumber == 4) {
				isElemenated = true;
			}
		}
		groups.add(groupNumber);
	}

}
