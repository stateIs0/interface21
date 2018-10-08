/**
 * Generic framework code included with 
 * <a href="http://www.amazon.com/exec/obidos/tg/detail/-/1861007841/">Expert One-On-One J2EE Design and Development</a>
 * by Rod Johnson (Wrox, 2002). 
 * This code is free to use and modify. 
 * Please contact <a href="mailto:rod.johnson@interface21.com">rod.johnson@interface21.com</a>
 * for commercial support.
 */

package com.interface21.context;

import java.io.InputStream;
import java.io.IOException;

import com.interface21.beans.factory.HierarchicalBeanFactory;
import com.interface21.beans.factory.ListableBeanFactory;

/** 
 * Interface to provide configuration for an application.
 * This is read-only while the application is running,
 * but may be reloaded if the implementation supports this.
 *
 * 用于为应用程序提供配置的接口。 这在应用程序运行时是只读的，但如果实现支持，则可以重新加载。 *
 *
 * <p>The configuration provides:
 * <ul>
 * <li>The ability to resolve messages, supporting internationalization.解决消息，支持国际化的能力。
 * <li>The ability to publish events. Implementations must provide a means
 * of registering event listeners. 发布事件的能力。实现必须提供注册事件侦听器的方法。
 * <li>The ability to share objects by publishing them to the context.通过将对象发布到上下文来共享对象的能力。
 * <li>Bean factory methods, inherited from ListableBeanFactory. This
 * avoids the need for applications to use singletons. Bean工厂方法，继承自ListableBeanFactory。这避免了应用程序使用单例的需要。
 * <li>Notification of beans initialized by the context of the context,
 * enabling communication with the rest of the application, for
 * example by publishing events. The BeanFactory superinterface
 * provides no similar mechanism.
 * 通过上下文的上下文初始化的bean的通知，允许与应用程序的其余部分进行通信，例如通过发布事件。
 * BeanFactory superinterface 没有提供类似的机制。
 * <li>Inheritance from a parent context. Definitions in a descendant context
 * will always take priority. This means, for example, that a single parent
 * context can be used by an entire web application, while each servlet has its
 * own child context that is independent of that of any other servlet.
 *
 * 从父上下文继承。后代上下文中的定义将始终优先。
 * 这意味着，例如，整个Web应用程序可以使用单个父上下文，而每个servlet都有自己的子上下文，该上下文独立于任何其他servlet。
 * </ul>
 *
 * @author Rod Johnson
 * @version $Revision$
 */
public interface ApplicationContext extends MessageSource, ListableBeanFactory, HierarchicalBeanFactory {
	
	/**
	 * Return the parent context, or null if there is no parent,
	 * and this is the root of the context hierarchy.
	 * @return the parent context, or null if there is no parent
	 */
	ApplicationContext getParent();
	
	/**
	 * Return a friendly name for this context.
	 * @return a display name for this context
	*/
	String getDisplayName();

	/**
	 * Return the timestamp when this context was first loaded
	 * @return the timestamp (ms) when this context was first loaded
	 */
	long getStartupDate();

	/**
	 * Return context options. These control reloading etc.
	 * <p>ApplicationContext implementations may subclass ContextOptions to
	 * add additional properties. It must always be a bean.
	 * @return context options (must not be null).
	 */
	ContextOptions getOptions();

	/**
	 * Load or refresh the persistent representation of the configuration,
	 * which might for example be an XML file, properties file or
	 * relational database schema.
	 *
	 * 加载或刷新配置的持久表示，例如可以是XML文件，属性文件或关系数据库模式。
	 *
	 * @throws ApplicationContextException if the config cannot be loaded
	 */
	void refresh() throws ApplicationContextException;

	/**
	 * Close this application context, releasing all resources and locks
	 * that the implementation might hold. This includes disposing all
	 * cached singleton beans.
	 * <p>Note: Does <i>not</i> invoke close on a parent context.
	 * @throws ApplicationContextException if there were fatal errors
	 */
	void close() throws ApplicationContextException;

	/**
	 * Notify all listeners registered with this application of an application
	 * event. Events may be framework events (such as RequestHandledEvent)
	 * or application-specific events.
	 * @param event event to publish
	 */
	void publishEvent(ApplicationEvent event);

	/**
	 * Open an InputStream to the specified resource:
	 * <ul>
	 * <li>Must support fully qualified URLs, e.g. "file:C:/test.dat".
	 * <li>Should support relative file paths, e.g. "WEB-INF/test.dat".
	 * <li>May allow for absolute file paths, e.g. "C:/test.dat".
	 * </ul>
	 * Note that the safest way to access an absolute file path is via
	 * a "file:" URL, as this must be supported by all implementations.
	 * <p>Note: Callers are responsible for closing the input stream.
	 * @param location location to the resource
	 * @return InputStream for the specified resource
	 * @throws IOException exception when opening the specified resource
	 */
	InputStream getResourceAsStream(String location) throws IOException;

	/**
	 * Return the base path for relatively addressed resources for this
	 * application context. Normally, this path will be the same as the one
	 * that getResourceAsStream uses for evaluating relative paths.
	 * <p>Note that this method returns null if this application context
	 * does not have a dedicated base path. Accordingly, getResourceAsStream
	 * may not support relative paths at all, or use more than one base path
	 * for evaluating relative paths.
	 * @return the resource base path (ending with a separator), or null
	 */
	String getResourceBasePath();

	/**
	 * Put an object available for sharing. Note that this
	 * method is not synchronized. As with Java 2 collections,
	 * it's up to calling code to ensure thread safety.
	 * Also, this doesn't work in a cluster. It's
	 * analogous to putting something in a ServletContext.
	 * @param key object key
	 * @param o object to put
	 */
	void shareObject(String key, Object o);
	
	/**
	 * Retrieve a shared object added with a call to shareObject().
	 * @return the object, or null if no object is known under
	 * this name (this is not an error).
	 */
	Object sharedObject(String key);
	
	/** 
	 * Remove a shared object added with a call to shareObject().
	 * Does nothing if the object was null.
	 * @param key the object was added with
	 * @return the object if it was found, or null.
	 */
	Object removeSharedObject(String key);
		
}
