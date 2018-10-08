package com.scmspain.utils;

import java.util.List;
import java.util.function.BiFunction;

public class TextElementInserter implements BiFunction<String, List<? extends TextElement>, String> {

	@Override
	public String apply(String text, List<? extends TextElement> elements) {
		if (elements.size() == 0)
			return text;
		
		char[] letters = text.toCharArray();
		StringBuffer sb = new StringBuffer();
		int lCounter = 0;
		int idxLinks = 0;
		int i = 0;
		TextElement element = (TextElement) elements.get(idxLinks);
		String value = element.getValue();
		
		while (i < letters.length + 1 && idxLinks < elements.size()) {
			element = (TextElement) elements.get(idxLinks);
			if (lCounter == element.getIndex()) {
				value = element.getValue();
				sb.append(value);
				lCounter += value.length();
				++idxLinks;
			}
			else {
				sb.append(letters[i++]);
				++lCounter;
			}
		}
		
		while (i < letters.length) 
			sb.append(letters[i++]);
		
		return sb.toString();

	}

}
