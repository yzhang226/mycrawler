package org.omega.crawler.common;

import java.util.concurrent.atomic.AtomicInteger;

public class DocIder {
	
	private static Object lock = new Object();
	private static final AtomicInteger id = new AtomicInteger(1000);

	private DocIder() { }

	public static synchronized int getNext() {
		System.out.println("id.get() is " + id.get());
		return id.getAndIncrement();
	}

}
