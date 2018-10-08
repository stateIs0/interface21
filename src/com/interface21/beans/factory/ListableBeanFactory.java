/*
 * The Spring Framework is published under the terms
 * of the Apache Software License.
 */

package com.interface21.beans.factory;

/**
 * Extension of BeanFactory to be implemented by bean factories that can
 * enumerate all their bean instances, rather than attempting bean lookup
 * by name one by one as requested by clients.
 *
 * BeanFactory的扩展由bean工厂实现，它可以枚举所有的bean实例，而不是按客户端的请求逐个尝试bean查找.
 *
 * <p>If this is a HierarchicalBeanFactory, the return values will not take any
 * BeanFactory hierarchy into account, but will relate only to the beans defined
 * in the current factory. Use the BeanFactoryUtils helper class to get all.
 *
 * 如果这是一个HierarchicalBeanFactory，则返回值不会考虑任何 BeanFactory层次结构，
 * 而只涉及当前工厂中定义的bean 。使用BeanFactoryUtils帮助程序类来获取所有。
 * 
 * <p>With the exception of getBeanDefinitionCount(), the methods in this interface
 * are not designed for frequent invocation. Implementations may be slow.
 *
 * 除了getBeanDefinitionCount（）之外，此接口中的方法不是为频繁调用而设计的。实施可能很慢。
 *
 * <p>BeanFactory implementations that preload all their beans (for example,
 * DOM-based XML factories) may implement this interface.
 *
 * 预加载所有bean的BeanFactory实现（例如，基于DOM的XML工厂）可以实现此接口。
 *
 * <p>This interface is discussed in "Expert One-on-One J2EE", by Rod Johnson.
 *
 * 这个界面在Rod Johnson的“Expert One-on-One J2EE”中讨论。
 *
 * @author Rod Johnson
 * @since 16 April 2001
 * @version $Id$
 */
public interface ListableBeanFactory extends BeanFactory {

	/**
	 * Return the number of beans defined in the factory.
	 * Does not consider any hierarchy this factory may participate in.
	 * @return the number of beans defined in the factory
	 */
	int getBeanDefinitionCount();

	/**
	 * Return the names of all beans defined in this factory
	 * Does not consider any hierarchy this factory may participate in.
	 *
	 * 返回此工厂中定义的所有bean的名称*不考虑此工厂可能参与的任何层次结构。
	 *
	 * @return the names of all beans defined in this factory,
	 * or an empty array if none defined
	 */
	String[] getBeanDefinitionNames();
	
	/**
	 * Return the names of beans matching the given object type 返回与给定对象类型匹配的bean的名称
	 * (including subclasses). 
	 * Does not consider any hierarchy this factory may participate in.
	 * @param type class or interface to match
	 * @return the names of beans matching the given object type 
	 * (including subclasses), or an empty array if none
	 */
	String[] getBeanDefinitionNames(Class type);
    
}
