package com.scmspain.utils;

import org.junit.Test;

import com.scmspain.utils.PatternExtractor.PatternExtractorResult;

import static org.junit.Assert.assertEquals;

public class PatternExtractorTest {

    @Test
    public void shouldExtractPatternAtEnd() throws Exception {
    	String pattern = "\\b(http|https)://\\S+\\b";
    	String text = "Hey http://foogle.co";
    	String expectedText = "Hey ";
    	PatternExtractorResult patternExtractorResult = new PatternExtractor().apply(pattern, text);
    	int extractedPatterns = patternExtractorResult.getExtractedPatternMap().entrySet().size();
    	assertEquals(1, extractedPatterns);
    	assertEquals(expectedText, patternExtractorResult.getText());
    }
    
    @Test
    public void shouldExtractPatternAtBegin() throws Exception {
    	String pattern = "\\b(http|https)://\\S+\\b";
    	String text = "http://foogle.co Hey How are u?";
    	String expectedText = " Hey How are u?";
    	PatternExtractorResult patternExtractorResult = new PatternExtractor().apply(pattern, text);
    	int extractedPatterns = patternExtractorResult.getExtractedPatternMap().entrySet().size();
    	assertEquals(1, extractedPatterns);
    	assertEquals(expectedText, patternExtractorResult.getText());
    }
    
    @Test
    public void shouldExtractPatternInMiddle() throws Exception {
    	String pattern = "\\b(http|https)://\\S+\\b";
    	String text = "Hey http://foogle.co bla https://foogle.co bla bla ";
    	String expectedText = "Hey  bla  bla bla ";
    	PatternExtractorResult patternExtractorResult = new PatternExtractor().apply(pattern, text);
    	int extractedPatterns = patternExtractorResult.getExtractedPatternMap().entrySet().size();
    	assertEquals(2, extractedPatterns);
    	assertEquals(expectedText, patternExtractorResult.getText());
    }
    
}
