package edu.wit.cs.comp3370;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

/* Implements a trie data structure 
 * 
 * Wentworth Institute of Technology
 * COMP 3370
 * Lab Assignment 6 solution
 * 
 */

public class Trie extends Speller {
	private final TrieNode root = new TrieNode(null);
	private final int MAX_COUNT = 2;

	@Override
	public void insertWord(String s) {
		if(s == null || s.isEmpty()) {
			return;
		}
		
		final TrieNode lastChar = insertWordIntoNode(root, s.toCharArray(), 0);
		lastChar.isCompleteWord = true;
	}
	
	private TrieNode insertWordIntoNode(final TrieNode node, final char[] characters, final int offset) {
		if(isArrayEmpty(characters, offset)) {
			return null;
		}
		
		final char first = characters[offset];
		TrieNode childNode = node.children.get(first);
		if(childNode == null) {
			childNode = new TrieNode(first);
			node.children.put(first, childNode);
			childNode.parent = node;
		}
		TrieNode lastNode = insertWordIntoNode(childNode, characters, offset + 1);
		if(lastNode == null) {
			lastNode = childNode;
		}
		return lastNode;
	}

	@Override
	public boolean contains(String s) {
		if(s== null || s.isEmpty()) {
			return false;
		}
		return nodeContains(root, s.toCharArray(), 0);
	}
	
	private boolean nodeContains(final TrieNode node, final char[] characters, final int offset) {
		final char first = characters[offset];
		TrieNode childNode = node.children.get(first);
		
		if(childNode == null) {
			return false;
		}
		
		if(isArrayEmpty(characters, offset + 1)) {
			return childNode.isCompleteWord;
		}
		
		return nodeContains(childNode, characters, offset + 1);
	}

	@Override
	public String[] getSugg(String s) {
		char[] characters = s.toCharArray();
		List<String> suggestionsList = getSuggestions(root, "", characters, 0, 0);
		suggestionsList.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		String[] suggestions = new String[suggestionsList.size()];
		suggestions = suggestionsList.toArray(suggestions); // TODO: Double check
		return suggestions;
	}
	
	private List<String> getSuggestions(final TrieNode node, final String builtWord, final char[] searchWord, final int offset, final int count) {
		List<String> suggestions = new ArrayList<>();
		if(count > MAX_COUNT || isArrayEmpty(searchWord, offset)) {
			return suggestions;
		}
		
		final char letter = searchWord[offset];
		String newWord = builtWord;
		if(node.value != null) {
			newWord += node.value;
		}
		for(Entry<Character, TrieNode> childEntry : node.children.entrySet()) {
			int currentCount = count;
			if(childEntry.getValue().value != letter) {
				currentCount++;
			}
			if(currentCount > MAX_COUNT) {
				continue;
			}
			
			if (childEntry.getValue().isCompleteWord && isArrayEmpty(searchWord, offset + 1)){
				suggestions.add(newWord + childEntry.getValue().value);
			} else {
				List<String> childSuggestions = getSuggestions(childEntry.getValue(), newWord, searchWord, offset + 1, currentCount);
				suggestions.addAll(childSuggestions);
			}
		}
		
		return suggestions;
	}
	
	private boolean isArrayEmpty(final char[] characters, final int offset) {
		return characters.length - offset == 0;
	}
}
