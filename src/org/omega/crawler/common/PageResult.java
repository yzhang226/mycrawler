package org.omega.crawler.common;

import java.io.Serializable;
import java.util.List;

public class PageResult implements Serializable{

	 // asc | desc
	 private String dir;
	 private int results;
	 private String sort;
	 
	 public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getRecordsReturned() {
		return recordsReturned;
	}

	public void setRecordsReturned(int recordsReturned) {
		this.recordsReturned = recordsReturned;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	 private int totalRecords;
	 private int recordsReturned;
	 
	 private int pageSize;		
	 private int startIndex;
	 private List<?> records;
	 
	 public List<?> getRecords() {
		return records;
	}

	public void setRecords(List<?> records) {
		this.records = records;
	}

	public String getDir() {
		return dir;
	}

	public int getResults() {
		return results;
	}

	public void setResults(int results) {
		this.results = results;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

}
