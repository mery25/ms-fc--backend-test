package com.scmspain.utils;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PatternExtractor {
	
	private final String pattern;
	private final Map<Integer, String> extractedPatternMap = new TreeMap<>();
	private String text;
	
	public PatternExtractor(final String pattern, final String text) {
		if (pattern == null || text == null)
			throw new IllegalArgumentException();
		
		this.pattern = pattern;
		this.text = text;
	}
	
	public void extract() {
		extractedPatternMap.clear();
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(text);
		StringBuffer sb = new StringBuffer(text.length());
		while (m.find()) {
			String text = m.group(0);
			int position = m.start(0);
			extractedPatternMap.put(position, text);
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		this.text = sb.toString();
	}

	public Map<Integer, String> getExtractedPatternMap() {
		return Collections.unmodifiableMap(extractedPatternMap);
	}

	public String getText() {
		return text;
	}

}
