package org.omega.crawler.test;

import org.omega.crawler.common.DocIder;

public class DocIderThread extends Thread {

	private int i;
	
	public DocIderThread(int i) {
		this.i = i;
	}
	
	@Override
	public void run() {
		setName("T"+i);
		for (int j=0; j<10; j++) {
			System.out.println(getName() + " - " + j + " : " + DocIder.getNext());
		}
		
	}
	
	public static void main(String[] args) {
		DocIderThread t1 = new DocIderThread(1);
		DocIderThread t2 = new DocIderThread(2);
		t1.start();
		t2.start();
		
	}
	
}
