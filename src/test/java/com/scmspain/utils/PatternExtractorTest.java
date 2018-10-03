package com.scmspain.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PatternExtractorTest {

    @Test
    public void shouldExtractPatternAtEnd() throws Exception {
    	String pattern = "\\b(http|https)://\\S+\\b";
    	String text = "Hey http://foogle.co";
    	String expectedText = "Hey ";
    	PatternExtractor patternExtractor = new PatternExtractor(pattern, text);
    	patternExtractor.extract();
    	int extractedPatterns = patternExtractor.getExtractedPatternMap().entrySet().size();
    	assertEquals(1, extractedPatterns);
    	assertEquals(expectedText, patternExtractor.getText());
    }
    
    @Test
    public void shouldExtractPatternAtBegin() throws Exception {
    	String pattern = "\\b(http|https)://\\S+\\b";
    	String text = "http://foogle.co Hey How are u?";
    	String expectedText = " Hey How are u?";
    	PatternExtractor patternExtractor = new PatternExtractor(pattern, text);
    	patternExtractor.extract();
    	int extractedPatterns = patternExtractor.getExtractedPatternMap().entrySet().size();
    	assertEquals(1, extractedPatterns);
    	assertEquals(expectedText, patternExtractor.getText());
    }
    
    @Test
    public void shouldExtractPatternInMiddle() throws Exception {
    	String pattern = "\\b(http|https)://\\S+\\b";
    	String text = "Hey http://foogle.co bla https://foogle.co bla bla ";
    	String expectedText = "Hey  bla  bla bla ";
    	PatternExtractor patternExtractor = new PatternExtractor(pattern, text);
    	patternExtractor.extract();
    	int extractedPatterns = patternExtractor.getExtractedPatternMap().entrySet().size();
    	assertEquals(2, extractedPatterns);
    	assertEquals(expectedText, patternExtractor.getText());
    }
    
}
