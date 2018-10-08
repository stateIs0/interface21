/**
 * Generic framework code included with
 * <a href="http://www.amazon.com/exec/obidos/tg/detail/-/1861007841/">Expert One-On-One J2EE Design and Development</a>
 * by Rod Johnson (Wrox, 2002).
 * This code is free to use and modify.
 * Please contact <a href="mailto:rod.johnson@interface21.com">rod.johnson@interface21.com</a>
 * for commercial support.
 */

package com.interface21.beans.factory;

import com.interface21.beans.BeansException;

/**
 * Interface to be implemented by objects that hold a number of bean definitions,
 * each uniquely identified by a String name. An independent instance of any of
 * these objects can be obtained (the Prototype design pattern), or a single
 * shared instance can be obtained (a superior alternative to the Singleton
 * design pattern). Which type of instance will be returned depends on the bean
 * factory configuration - the API is the same. The Singleton approach is much
 * more useful and more common in practice.
 *
 * 接口由包含许多bean定义的对象实现，每个bean定义由String名称唯一标识。可以获得任何这些对象的独立实例（Prototype设计模式），
 * 或者可以获得单个共享实例（Singleton 设计模式的优越替代）。
 * 将返回哪种类型的实例取决于bean factory配置 -  API是相同的。 Singleton方法在实践中更有用，更常见。
 *
 * <p>The point of this approach is that the BeanFactory is a central registry
 * of application components, and centralizes the configuring of application
 * components (no more do individual objects need to read properties files,
 * for example). See chapters 4 and 11 of "Expert One-on-One J2EE Design and
 * Development" for a discussion of the benefits of this approach.
 *
 * 这种方法的重点是 BeanFactory 是应用程序组件的中央注册表，并集中了应用程序组件的配置（例如，单个对象不再需要读取属性文件）。
 * 有关此方法的优点的讨论，请参见“Expert One-on-One J2EE Design and Development”的第4章和第11章。
 *
 * <p>Normally a BeanFactory will load bean definitions stored in a configuration
 * source (such as an XML document), and use the com.interface21.beans package
 * to configure the beans. However, an implementation could simply return Java
 * objects it creates as necessary directly in Java code. There are no constraints
 * on how the definitions could be stored: LDAP, RDBMS, XML, properties file etc.
 * Implementations are encouraged to support references amongst beans, to either
 * Singletons or Prototypes.
 *
 * 通常，BeanFactory将加载存储在配置源（例如XML文档）中的bean定义，
 * 并使用com.interface21.beans包来配置bean。但是，实现可以直接在Java代码中直接返回它创建的 Java 对象。
 * 关于如何存储定义没有约束：LDAP，RDBMS，XML，属性文件等。鼓励实现支持bean之间的引用，单例或原型。
 *
 * @author Rod Johnson
 * @since 13 April 2001
 * @version $Revision$
 */
public interface BeanFactory {

	/**
	 * Return an instance (possibly shared or independent) of the given bean name.
	 * This method allows a bean factory to be used as a replacement for
	 * the Singleton or Prototype design pattern.
	 * <p>Note that callers should retain references to returned objects. There is
	 * no guarantee that this method will be implemented to be efficient. For example,
	 * it may be synchronized, or may need to run an RDBMS query.
	 *
	 * 返回给定bean名称的实例（可能是共享的或独立的）。
	 * 此方法允许将bean工厂用作 Singleton或 Prototype 设计模式的替代品。 *
	 * <p>请注意，调用者应保留对返回对象的引用。 无法保证此方法的实施效率。例如，它可能是同步的，或者可能需要运行RDBMS查询。
	 * @param name name of the bean to return
	 * @return the instance of the bean
	 * @throws NoSuchBeanDefinitionException if there's no such bean definition
	 */
	Object getBean(String name) throws BeansException;

	/**
	 * Return an instance (possibly shared or independent) of the given bean name.
	 * Provides a measure of type safety by throwing an exception if the bean is not
	 * of the required type.
	 * This method allows a bean factory to be used as a replacement for
	 * the Singleton or Prototype design pattern.
	 * <p>Note that callers should retain references to returned objects. There is
	 * no guarantee that this method will be implemented to be efficient. For example,
	 * it may be synchronized, or may need to run an RDBMS query.
	 *
	 * 返回给定bean名称的实例（可能是共享的或独立的）。 如果bean不是所需类型的，则通过抛出异常来提供类型安全性的度量。
	 * 此方法允许将bean工厂用作* Singleton或Prototype设计模式的替代品。请注意，调用者应保留对返回对象的引用。 无法保证此方法的实施效率。
	 * 例如，它可能是同步的，或者可能需要运行RDBMS查询。
	 *
	 * @param name name of the bean to return
	 * @param requiredType type the bean may match. Can be an interface or superclass
	 * of the actual class. For example, if the value is Object.class, this method will
	 * succeed whatever the class of the returned instance.
	 * @return the instance of the bean
	 * @throws BeanNotOfRequiredTypeException if the bean is not of the required type
	 * @throws NoSuchBeanDefinitionException if there's no such bean definition
	 */
	Object getBean(String name, Class requiredType) throws BeansException;

	/**
	 * Is this bean a singleton? That is, will getBean() always return the same object?
	 * 这bean是单例吗？也就是说，getBean（）总是会返回相同的对象吗？
	 * @param name name of the bean to query
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @return is this bean a singleton
	 */
	boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Return the aliases for the given bean name, if defined.
	 *
	 * 如果已定义，则返回给定bean名称的别名。
	 *
	 * @param name the bean name to check for aliases
	 * @return the aliases, or an empty array if none
	 * @throws NoSuchBeanDefinitionException if there's no such bean definition
	 */
	String[] getAliases(String name) throws NoSuchBeanDefinitionException;

}
