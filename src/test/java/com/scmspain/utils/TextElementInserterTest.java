package com.scmspain.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.scmspain.entities.Link;

public class TextElementInserterTest {

    @Test
    public void shouldInsertElementsAtEnd() throws Exception {
    	String text = "Hey ";
    	List<Link> links = new ArrayList<>();
    	links.add(new Link("http://foogle.co", 4));
    	String textResult = new TextElementInserter().apply(text, links);
    	String expectedText = "Hey http://foogle.co";
    	assertEquals(expectedText, textResult);
    }
    
    @Test
    public void shouldInsertElementsAtBeginning() throws Exception {
    	String text = " Hey ";
    	List<Link> links = new ArrayList<>();
    	links.add(new Link("http://foogle.co", 0));
    	String textResult = new TextElementInserter().apply(text, links);
    	String expectedText = "http://foogle.co Hey ";
    	assertEquals(expectedText, textResult);
    }
    
    @Test
    public void shouldInsertElementsAtBeginningAndEnd() throws Exception {
    	String text = " Hey ";
    	List<Link> links = new ArrayList<>();
    	links.add(new Link("http://foogle.co", 0));
    	links.add(new Link("http://google.com", 21));
    	String textResult = new TextElementInserter().apply(text, links);
    	String expectedText = "http://foogle.co Hey http://google.com";
    	assertEquals(expectedText, textResult);
    }
    
    @Test
    public void shouldInsertElementsInTheMiddle() throws Exception {
    	String text = "Hey  bla  end";
    	List<Link> links = new ArrayList<>();
    	links.add(new Link("http://foogle.co", 4));
    	links.add(new Link("http://google.com", 25));
    	String textResult = new TextElementInserter().apply(text, links);
    	String expectedText = "Hey http://foogle.co bla http://google.com end";
    	assertEquals(expectedText, textResult);
    }
    
}
