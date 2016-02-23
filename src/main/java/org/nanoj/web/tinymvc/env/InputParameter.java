package org.nanoj.web.tinymvc.env ;

public class InputParameter<T> {

	private String name ;
	private T      value ;

	public InputParameter(String name, T value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public T getValue() {
		return value;
	}
	
}
