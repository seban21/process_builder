package com.example.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class ChildVo {
	@JsonProperty("ChildA")
	public String childA;
	@JsonProperty("ChildB")
	public int childB;
}
