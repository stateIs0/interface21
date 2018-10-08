/*
 * The Spring Framework is published under the terms
 * of the Apache Software License.
 */
 
package com.interface21.beans.factory;

/**
 * Interface to be implemented by beans that wish to be aware of their owning
 * BeanFactory. Beans can e.g. look up collaborating beans via the factory.
 *
 * 由希望了解其拥有的 BeanFactory的bean实现的接口。bean 类可以例如通过工厂查找合作 bean。
 *
 * <p>Note that most beans will choose to receive references to collaborating
 * beans via respective bean properties.
 *
 * 请注意，大多数bean将选择通过相应的bean属性接收对协作bean的引用。
 *
 * @author Rod Johnson
 * @since 11-Mar-2003
 * @version $Revision$
 */
public interface BeanFactoryAware {
	
	/**
	 * Callback that supplies the owning factory to a bean instance.
	 *
	 * 将拥有工厂提供给bean实例的回调。
	 *
	 * <p>If the bean also implements InitializingBean, this method will
	 * be invoked after InitializingBean's <code>afterPropertiesSet</code>.
	 *
	 * 如果bean也实现了InitializingBean，则在InitializingBean之后将调用此方法
	 *
	 * @param beanFactory owning BeanFactory (may not be null).
	 * The bean can immediately call methods on the factory.拥有BeanFactory（可能不为null）。 bean可以立即调用工厂的方法。
	 * @throws Exception this method can throw any exception. Normally we want
	 * methods to declare more precise exceptions, but in this case the owning
	 * BeanFactory will catch and handle the exception (treating it as fatal),
	 * and we want to make it easy to implement BeanFactoryAware beans by
	 * freeing developers from the need to catch and wrap fatal exceptions.
	 * Exceptions thrown here are considered fatal.
	 *
	 * 这个方法可以抛出任何异常。通常我们希望*方法声明更精确的异常，
	 * 但在这种情况下，拥有 BeanFactory将捕获并处理异常（将其视为致命），
	 * 并且我们希望通过*从开发人员中解放来实现BeanFactoryAware bean。
	 * 需要抓住并包装致命的例外。 此处抛出的异常被认为是致命的。
	 */
	void setBeanFactory(BeanFactory beanFactory) throws Exception;

}
