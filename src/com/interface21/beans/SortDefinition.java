package com.interface21.beans;

/**
 * Definition for sorting bean instances by a property.
 *
 * 用于按属性排序bean实例的定义。
 *
 * @author Juergen Hoeller
 * @since 26.05.2003
 */
public interface SortDefinition {

	/**
	 * Return the name of the property to sort by.
	 *
	 * 返回要排序的属性的名称。
	 *
	 */
	String getProperty();

	/**
	 * Return whether upper and lower case in String values should be ignored.
	 */
	boolean isIgnoreCase();

	/**
	 * Return if ascending or descending.
	 *
	 * 如果上升或下降则返回。
	 *
	 */
	boolean isAscending();

}
