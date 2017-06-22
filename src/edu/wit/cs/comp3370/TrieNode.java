package edu.wit.cs.comp3370;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TrieNode {
	Character value = null;
	boolean isCompleteWord;
	TrieNode parent = null;
	Map<Character, TrieNode> children = new HashMap<>();
	
	public TrieNode(Character value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		String result = " [";
		boolean insert = false;
		for(Entry<Character, TrieNode> child : children.entrySet()) {
			if(insert) {
				result += ", ";
			} else {
				insert = true;
			}
			result += child.toString();
		}
		result += "] ";
		return result;
	}
}
