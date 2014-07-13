package org.omega.trade.entity;

public class CandleStick {

	private long date;
	private double open;
	private double high;
	private double low;
	private double close;
	private double watchedVolume;
	private double exchangeVolume;
	
	public CandleStick() {
		
	}
	
	public CandleStick(long date, double open, double high, double low, double close, double watchedVolume, double exchangeVolume) {
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.watchedVolume = watchedVolume;
		this.exchangeVolume = exchangeVolume;
	}

	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}

	public double getWatchedVolume() {
		return watchedVolume;
	}

	public void setWatchedVolume(double watchedVolume) {
		this.watchedVolume = watchedVolume;
	}

	public double getExchangeVolume() {
		return exchangeVolume;
	}

	public void setExchangeVolume(double exchangeVolume) {
		this.exchangeVolume = exchangeVolume;
	}
	
}
