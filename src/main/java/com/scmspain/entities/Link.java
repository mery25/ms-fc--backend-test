package com.scmspain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Link {

	public static final String REGEX_PATTERN = "\\b(http|https)://\\S+\\b";
	
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(nullable = false)
	private String text;
    
    @Column(nullable = false)
	private int position;

    public Link() {
	}
    
	public Link(String text, int position) {
		super();
		this.text = text;
		this.position = position;
	}

	public String getLinkText() {
		return text;
	}

	public int getPosition() {
		return position;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
	@Override
	public String toString() {
		return "Link [linkText=" + text + ", position=" + position + "]";
	}

}