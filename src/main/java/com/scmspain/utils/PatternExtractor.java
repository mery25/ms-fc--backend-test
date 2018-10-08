package com.scmspain.utils;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.scmspain.utils.PatternExtractor.PatternExtractorResult;

public class PatternExtractor implements BiFunction<String, String, PatternExtractorResult> {

	@Override
	public PatternExtractorResult apply(String pattern, String text) {
		Map<Integer, String> extractedPatternMap = new TreeMap<>();
		extractedPatternMap.clear();
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(text);
		StringBuffer sb = new StringBuffer(text.length());
		while (m.find()) {
			text = m.group(0);
			int position = m.start(0);
			extractedPatternMap.put(position, text);
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return new PatternExtractorResult(extractedPatternMap, sb.toString());
	}
	
	public final class PatternExtractorResult {
		
		private final Map<Integer, String> extractedPatternMap;
		private final String text;
		
		public PatternExtractorResult(final Map<Integer, String> extractedPatternMap, final String text) {
			this.extractedPatternMap = extractedPatternMap;
			this.text = text;
		}

		public Map<Integer, String> getExtractedPatternMap() {
			return Collections.unmodifiableMap(extractedPatternMap);
		}

		public String getText() {
			return text;
		}
		
	}

}
