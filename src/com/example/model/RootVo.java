package com.example.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class RootVo {
	@JsonProperty("RootA")
	public String rootA;
	@JsonProperty("ChildNode")
	public ChildVo childVo;
}
