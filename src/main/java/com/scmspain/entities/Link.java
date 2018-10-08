package com.scmspain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.scmspain.utils.TextElement;

@Entity
public class Link implements TextElement {

	public static final String REGEX_PATTERN = "\\b(http|https)://\\S+\\b";
	
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(nullable = false)
	private String value;
    
    @Column(nullable = false)
	private int index;

    public Link() {
	}
    
	public Link(String text, int index) {
		super();
		this.value = text;
		this.index = index;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public String getValue() {
		return value;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
	@Override
	public String toString() {
		return "Link [linkText=" + value + ", position=" + index + "]";
	}

}