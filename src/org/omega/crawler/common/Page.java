package org.omega.crawler.common;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Page<T> {
	public static final String ASC = "asc"; 
	public static final String DESC = "desc"; 

	private static final int PAGE_LIMIT = 10;
	private static final int BEGIN_GAP = 5;// PAGE_LIMIT/2 
	private static final int END_GAP = 4;// PAGE_LIMIT % 2 == 0 ? PAGE_LIMIT/2 - 1 : PAGE_LIMIT/2
	
	private int pageNo = 1;
	private int pageSize = 15;
	
	private String orderBy = null; 
	private String order = ASC; 
	
	private boolean autoCount = false; 
	
	private String[] ids;
	private String method;
	private List<T> result = null;
	private int totalCount = -1;

	public int getBeginPageNo() {
		int begin = 1;
		int totalPages = getTotalPages();
		
		if (getTotalPages() > PAGE_LIMIT) {
			if (pageNo - BEGIN_GAP >= 1) {// need jump page no
				if (pageNo + END_GAP > totalPages) {
					begin = pageNo - ((PAGE_LIMIT-1) - (totalPages-pageNo));
				} else {
					begin = pageNo - BEGIN_GAP;
				}
			}
		}
		
		return begin;
	}
	
	public int getEndPageNo() {
		int totalPages = getTotalPages();
		int end = totalPages;
		
		if (getTotalPages() > PAGE_LIMIT) {
			if (pageNo - BEGIN_GAP >= 1) {// need jump page no
				if (pageNo + END_GAP <= totalPages) {
					end = pageNo + END_GAP;
				}
			} else {
				end = PAGE_LIMIT;
			}
		}
		
		return end;
	}

	public Page() {
	}

	public Page(final int pageSize) {
		this.pageSize = pageSize;
	}

	public Page(final int pageSize, final boolean autoCount) {
		this.pageSize = pageSize;
		this.autoCount = autoCount;
	}

	public int getPageNo() {
		return pageNo;
	}


	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;
	}


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}


	public boolean isPageSizeSetted() {
		return pageSize > 0;
	}


	public int getFirst() {
		if (pageNo < 1 || pageSize < 1)
			return -1;
		else
			return ((pageNo - 1) * pageSize);
	}


	public boolean isFirstSetted() {
		return (pageNo > 0 && pageSize > 0);
	}


	public String getOrderBy() {
		return orderBy;
	}


	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}


	public boolean isOrderBySetted() {
		return StringUtils.isNotBlank(orderBy);
	}


	public String getOrder() {
		return order;
	}


	public void setOrder(final String order) {
		if (ASC.equalsIgnoreCase(order) || DESC.equalsIgnoreCase(order)) {
			this.order = order.toLowerCase();
		} else
			throw new IllegalArgumentException(
					"Order should be 'desc' or 'asc'");
	}


	public String getPageParam() {
		return getPageNo() + "|" + StringUtils.defaultString(getOrderBy())
				+ "|" + getOrder();
	}


	public void setPageParam(String pageParam) {

		if (StringUtils.isBlank(pageParam))
			return;

		String[] params = StringUtils.splitPreserveAllTokens(pageParam, '|');

		if (StringUtils.isNumeric(params[0])) {
			setPageNo(Integer.valueOf(params[0]));
		}

		if (StringUtils.isNotBlank(params[1])) {
			setOrderBy(params[1]);
		}

		if (StringUtils.isNotBlank(params[2])) {
			setOrder(params[2]);
		}
	}


	public boolean isAutoCount() {
		return autoCount;
	}

	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}


	public List<T> getResult() {
		return result;
	}

	public void setResult(final List<T> result) {
		this.result = result;
	}


	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(final int totalCount) {
		this.totalCount = totalCount;
	}


	public int getTotalPages() {
		if (totalCount == -1)
			return -1;

		int count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}


	public String getInverseOrder() {
		if (order.endsWith(DESC))
			return ASC;
		else
			return DESC;
	}


	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}


	public int getNextPage() {
		if (isHasNext())
			return pageNo + 1;
		else
			return pageNo;
	}


	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}


	public int getPrePage() {
		if (isHasPre())
			return pageNo - 1;
		else
			return pageNo;
	}


	public String[] getIds() {
		return ids;
	}


	public void setIds(String[] ids) {
		this.ids = ids;
	}


	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
