package com.digiturtle.blocktimerlive;

@FunctionalInterface
public interface ActorFactory<I, O> {

	public O create(I input);
	
}
