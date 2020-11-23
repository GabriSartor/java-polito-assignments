package it.polito.tdp.ufo.db;

import java.time.Year;

public class YearCount {
	private Year year;
	private Integer count;
	@Override
	public String toString() {
		return year + " (" + count + ")";
	}
	public YearCount(Year year, Integer count) {
		super();
		this.year = year;
		this.count = count;
	}
	public Year getYear() {
		return year;
	}
	public void setYear(Year year) {
		this.year = year;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

}
