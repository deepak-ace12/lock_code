package com.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Main {

	public static final HashMap<Integer, Integer> ALLOWED_GROUP_ELEMENT = new HashMap<Integer, Integer>() {
		{
			put(1, 1);
			put(2, 1);
			put(3, 2);
			put(4, 0);
			put(5, 1);
		}
	};

	static LinkedHashMap<Integer, Node> nodes = new LinkedHashMap<Integer, Node>();

	static int groupElements[][] = new int[5][3];

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the elements without any space or deleimeter e.g. 123");
		System.out.println("One number is correct and is at the right place: ");
		createNode(sc.next(), 1);

		System.out.println("One number is correct and is at the wrong place: ");
		createNode(sc.next(), 2);

		System.out.println("Two numbers are correct and are at the wrong places: ");
		createNode(sc.next(), 3);

		System.out.println("No number is correct: ");
		createNode(sc.next(), 4);

		System.out.println("One number is correct and is at the wrong place: ");
		createNode(sc.next(), 5);

		ArrayList<Integer> filteredGroups = getFilteredGroups();

		HashMap<Integer, Integer> lockCodes = new HashMap<Integer, Integer>();

		for (int groupNo : filteredGroups) {
			addConfirmedNumbers(groupNo, lockCodes);
		}

		for (Node node : nodes.values()) {
			addOtherNumbers(node, lockCodes);
		}
		System.out.println();
		System.out.print("The Lock code is:");
		System.out.println(" " + lockCodes.get(1) + lockCodes.get(2) + lockCodes.get(3));
	}

	private static void addConfirmedNumbers(int groupNo, HashMap<Integer, Integer> code) {
		for (int i = 0; i < groupElements[groupNo - 1].length; i++) {
			int currentElement = groupElements[groupNo - 1][i];
			if (groupNo == 1 && !nodes.get(currentElement).isElemenated
					&& !isGroupElementInserted(code, currentElement, i + 1)) {
				return;
			} else if (!nodes.get(currentElement).isElemenated) {
				ArrayList<Integer> positions = getAllowedPositions(nodes.get(currentElement).notAllowedPositions);
				for (int position : positions) {
					if (!code.containsKey(position) && !isGroupElementInserted(code, currentElement, position)) {
						return;
					}
				}
			}
		}
	}

	private static void addOtherNumbers(Node node, HashMap<Integer, Integer> code) {
		if (!node.isElemenated) {
			if (node.allowedPosition > 0 && !isGroupElementInserted(code, node.number, node.allowedPosition)) {
				return;
			} else {
				for (int group : nodes.get(node.number).groups) {
					if (ALLOWED_GROUP_ELEMENT.get(group) <= 0) {
						return;
					}
				}
				ArrayList<Integer> positions = getAllowedPositions(nodes.get(node.number).notAllowedPositions);
				for (int position : positions) {
					if (!code.containsKey(position) && !isGroupElementInserted(code, node.number, position)) {
						return;
					}
				}
			}
		}
	}

	private static ArrayList<Integer> getAllowedPositions(HashSet<Integer> groups) {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for (int i = 1; i <= 3; i++) {
			if (!groups.contains(i))
				positions.add(i);
		}
		return positions;
	}

	private static ArrayList<Integer> getFilteredGroups() {
		ArrayList<Integer> confirmed = new ArrayList<>();
		for (int groupNo = 0; groupNo < 5; groupNo++) {
			int nElemenatedElements = 0;
			for (int element : groupElements[groupNo]) {
				if (!nodes.get(element).isElemenated) {
					nElemenatedElements += 1;
				}
			}
			if (nElemenatedElements == ALLOWED_GROUP_ELEMENT.get(groupNo + 1) && groupNo != 3) {
				confirmed.add(groupNo + 1);
			}
		}

		return confirmed;

	}

	public static void createNode(String input, int groupNumber) {
		String[] numbers = input.split("");
		for (int i = 0; i < 3; i++) {
			int number = Integer.parseInt(numbers[i]);
			if (nodes.containsKey(number)) {
				nodes.get(number).insertValues(i + 1, groupNumber);
			} else {
				Node node = new Node(number);
				node.insertValues(i + 1, groupNumber);
				nodes.put(number, node);
			}

			groupElements[groupNumber - 1][i] = number;
		}
	}

	public static boolean isGroupElementInserted(HashMap<Integer, Integer> code, int element, int position) {
		for (int group : nodes.get(element).groups) {
			if (ALLOWED_GROUP_ELEMENT.get(group) <= 0)
				return false;
		}
		code.put(position, element);
		for (int group : nodes.get(element).groups) {
			ALLOWED_GROUP_ELEMENT.put(group, ALLOWED_GROUP_ELEMENT.get(group) - 1);
		}

		return true;
	}

}
