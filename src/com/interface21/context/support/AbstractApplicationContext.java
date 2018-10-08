/**
 * Generic framework code included with
 * <a href="http://www.amazon.com/exec/obidos/tg/detail/-/1861007841/">Expert One-On-One J2EE Design and Development</a>
 * by Rod Johnson (Wrox, 2002).
 * This code is free to use and modify.
 * Please contact <a href="mailto:rod.johnson@interface21.com">rod.johnson@interface21.com</a>
 * for commercial support.
 */

package com.interface21.context.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.interface21.beans.BeansException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.NoSuchBeanDefinitionException;
import com.interface21.beans.factory.support.BeanFactoryUtils;
import com.interface21.beans.factory.support.ListableBeanFactoryImpl;
import com.interface21.context.ApplicationContext;
import com.interface21.context.ApplicationContextAware;
import com.interface21.context.ApplicationContextException;
import com.interface21.context.ApplicationEvent;
import com.interface21.context.ApplicationEventMulticaster;
import com.interface21.context.ApplicationListener;
import com.interface21.context.ContextOptions;
import com.interface21.context.MessageSource;
import com.interface21.context.MessageSourceResolvable;
import com.interface21.context.NestingMessageSource;
import com.interface21.context.NoSuchMessageException;
import com.interface21.util.StringUtils;


/**
 * Partial implementation of ApplicationContext. Doesn't mandate the
 * type of storage used for configuration, but implements common functionality.
 *
 * ApplicationContext的部分实现。不强制要求用于配置的类型的存储，而是实现通用功能。
 *
 * <p>This class uses the Template Method design pattern, requiring
 * concrete subclasses to implement protected abstract methods.
 *
 * 此类使用模板方法设计模式，需要具体子类来实现受保护的抽象方法。
 *
 * <p>The context options may be supplied as a bean in the default bean factory,
 *  * with the name "contextOptions".
 *
 *  上下文选项可以作为bean在默认bean工厂中提供，名称为“contextOptions”。
 *
 * <p>A message source may be supplied as a bean in the default bean factory,
 * with the name "messageSource". Else, message resolution is delegated to the
 * parent context.
 *
 * 消息源可以作为bean在默认bean工厂中提供，其名称为“messageSource”。否则，将消息解析委托给父上下文。
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since January 21, 2001
 * @version $Revision$
 * @see #refreshBeanFactory
 * @see #getBeanFactory
 * @see #OPTIONS_BEAN_NAME
 * @see #MESSAGE_SOURCE_BEAN_NAME
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

	/**
	 * Name of options bean in the factory.
	 * If none is supplied, a default ContextOptions instance will be used.
	 * @see ContextOptions
	 */
	public static final String OPTIONS_BEAN_NAME = "contextOptions";

	/**
	 * Name of the MessageSource bean in the factory.
	 * If none is supplied, message resolution is delegated to the parent.
	 * @see MessageSource
	 */
	public static final String MESSAGE_SOURCE_BEAN_NAME = "messageSource";


	//---------------------------------------------------------------------
	// Instance data
	//---------------------------------------------------------------------

	/** Log4j logger used by this class. Available to subclasses. */
	protected final Log logger = LogFactory.getLog(getClass());

	/** Parent context */
	private ApplicationContext parent;

	/** Display name */
	private String displayName = getClass().getName() + ";hashCode=" + hashCode();

	/** System time in milliseconds when this context started */
	private long startupTime;

	/** Special bean to handle configuration */
	private ContextOptions contextOptions;

	/** MessageSource helper we delegate our implementation of this interface to */
	private MessageSource messageSource;

	/**
	 * Helper class used in event publishing.
	 * TODO: This could be parameterized as a JavaBean (with a distinguished name
	 * specified), enabling a different thread usage policy for event publication.
	 */
	private ApplicationEventMulticaster eventMulticaster = new ApplicationEventMulticasterImpl();

	/**
	 * Set of ApplicationContextAware objects that have already received the context
	 * reference, to be able to avoid double initialization of managed objects.
	 */
	private Set managedSingletons = new HashSet();

	/** Map of shared objects, keyed by String */
	private Map sharedObjects = new HashMap();


	//---------------------------------------------------------------------
	// Constructors
	//---------------------------------------------------------------------

	/**
	 * Create a new AbstractApplicationContext with no parent.
	 */
	public AbstractApplicationContext() {
	}

	/**
	 * Create a new AbstractApplicationContext with the given parent context.
	 * @param parent parent context
	 */
	public AbstractApplicationContext(ApplicationContext parent) {
		this.parent = parent;
	}


	//---------------------------------------------------------------------
	// Implementation of ApplicationContext
	//---------------------------------------------------------------------

	/**
	 * Return the parent context, or null if there is no parent,
	 * and this is the root of the context hierarchy.
	 * @return the parent context, or null if there is no parent
	 */
	public ApplicationContext getParent() {
		return parent;
	}

	/**
	 * Subclasses may call this to set parent after constructor.
	 * Note that parent shouldn't be changed: it should only be
	 * set later if it isn't available when an object of this
	 * class is created.
	 *
	 * 子类可以在构造函数之后调用它来设置父级。
	 * 请注意，不应更改父级：如果在创建此类的对象时它不可用，则应仅稍后进行设置。
	 *
	 * @param ac parent context
	 */
	protected void setParent(ApplicationContext ac) {
		this.parent = ac;
	}

	/**
	 * Return a friendly name for context
	 * @return a display name for the context
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * To avoid endless constructor chaining, only concrete classes
	 * take this in their constructor, and then invoke this method
	 */
	protected void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Return the timestamp when this context was first loaded
	 * @return the timestamp (ms) when this context was first loaded
	 */
	public final long getStartupDate() {
		return startupTime;
	}

	/**
	 * Return context options. These control reloading etc.
	 * @return context options
	 */
	public final ContextOptions getOptions() {
		return this.contextOptions;
	}

	/**
	 * Load or reload configuration.
	 * @throws ApplicationContextException if the configuration was invalid or couldn't
	 * be found, or if configuration has already been loaded and reloading is forbidden
	 */
	public final void refresh() throws ApplicationContextException {
		if (this.contextOptions != null && !this.contextOptions.isReloadable())
			throw new ApplicationContextException("Forbidden to reload config");

		this.startupTime = System.currentTimeMillis();

		refreshBeanFactory();

		if (getBeanDefinitionCount() == 0)
			logger.warn("No beans defined in ApplicationContext [" + getDisplayName() + "]");
		else
			logger.info(getBeanDefinitionCount() + " beans defined in ApplicationContext [" + getDisplayName() + "]");

		// 设置BeanFactory的后置处理器
//		postProcessBeanFactory(beanFactory);
		// invoke configurers that can override values in the bean definitions
		// 调用可以覆盖bean定义中的值的配置器 BeanFactoryPostProcessor
		invokeContextConfigurers();

		// load options bean for this context
		//为此上下文加载选项bean
		loadOptions();

		// 注册Bean的后处理器，在Bean创建过程中调用
//		registerBeanPostProcessors(beanFactory);
		// initialize message source for this context
		// 初始化此上下文的消息源
		initMessageSource();

		// 初始化上下文中的事件机制
//		initApplicationEventMulticaster();

		// initialize other special beans in specific context subclasses
		// 初始化特定上下文子类中的其他特殊bean
		onRefresh();

		// check for listener beans and register them
		// 检查监听器bean并注册它们
		refreshListeners();

		// instantiate singletons this late to allow them to access the message source
		// 最近实例化单例以允许它们访问消息源
		preInstantiateSingletons();

		// last step: publish respective event
		// 发布事件
		publishEvent(new ContextRefreshedEvent(this));
	}

	/**
	 * Callback method which can be overridden to add context-specific refresh work.
	 * @throws ApplicationContextException in case of errors during refresh
	 */
	protected void onRefresh() throws ApplicationContextException {
		// For subclasses: do nothing by default.
	}

	/**
	 * Instantiate and invoke all registered BeanFactoryPostProcessor beans.
	 * Must be called before singleton instantiation.
	 *
	 * 实例化并调用所有已注册的BeanFactoryPostProcessor bean。 必须在单例实例化之前调用。
	 *
	 */
	private void invokeContextConfigurers() {
		String[] beanNames = getBeanDefinitionNames(BeanFactoryPostProcessor.class);
		for (int i = 0; i < beanNames.length; i++) {
			String beanName = beanNames[i];
			BeanFactoryPostProcessor configurer = (BeanFactoryPostProcessor) getBean(beanName);
			configurer.postProcessBeanFactory(getBeanFactory());
		}
	}

	/**
	 * Load the options bean.
	 * The BeanFactory must be loaded before this method is called.
	 */
	private void loadOptions() {
		try {
			this.contextOptions = (ContextOptions) getBean(OPTIONS_BEAN_NAME);
		} catch (NoSuchBeanDefinitionException ex) {
			logger.info("No options bean (\"" + OPTIONS_BEAN_NAME + "\") found: using default");
			this.contextOptions = new ContextOptions();
		}
	}

	/**
	 * Initialize the message source.
	 * Use parent's if none defined in this context.
	 */
	private void initMessageSource() {
		try {
			this.messageSource = (MessageSource) getBean(MESSAGE_SOURCE_BEAN_NAME);
			// set parent message source if applicable,
			// and if the message source is defined in this context, not in a parent
			if (this.parent != null && (this.messageSource instanceof NestingMessageSource) &&
			    Arrays.asList(getBeanDefinitionNames()).contains(MESSAGE_SOURCE_BEAN_NAME)) {
				((NestingMessageSource) this.messageSource).setParent(this.parent);
			}
		}
		catch (NoSuchBeanDefinitionException ex) {
			logger.info("No MessageSource found for [" + getDisplayName() + "]: using empty StaticMessageSource");
			// use empty message source to be able to accept getMessage calls
			this.messageSource = new StaticMessageSource();
		}
	}

	/**
	 * Invoke the setApplicationContext() callback on all objects
	 * in the context. This involves instantiating the objects.
	 * Only singletons will be instantiated eagerly.
	 *
	 * 在上下文中的所有对象上调用setApplicationContext（）回调。
	 * 这涉及实例化对象。 只会热切地实例化单身人士。
	 *
	 */
	private void preInstantiateSingletons() {
		logger.info("Configuring singleton beans in context");
		String[] beanNames = getBeanDefinitionNames();
		logger.debug("Found " + beanNames.length + " listeners in bean factory: names=[" +
		             StringUtils.arrayToDelimitedString(beanNames, ",") + "]");
		for (int i = 0; i < beanNames.length; i++) {
			String beanName = beanNames[i];
			if (isSingleton(beanName)) {
				getBean(beanName);
			}
		}
	}

	/**
	 * If the object is context-aware, give it a reference to this object.
	 * Does not reinitialize singletons that have already received the context.
	 * @param bean object to invoke the setApplicationContext() method on,
	 * if it implements the ApplicationContextAware interface
	 */
	private void configureManagedObject(String name, Object bean) {
		if (bean instanceof ApplicationContextAware &&
				(!isSingleton(name) || !this.managedSingletons.contains(bean))) {
			logger.debug("Setting application context on ApplicationContextAware object [" + bean + "]");
			ApplicationContextAware aca = (ApplicationContextAware) bean;
			aca.setApplicationContext(this);
			this.managedSingletons.add(bean);
		}
	}

	/**
	 * Add beans that implement ApplicationListener as listeners.
	 * Doesn't affect other listeners, which can be added without being beans.
	 */
	private void refreshListeners() {
		logger.info("Refreshing listeners");
		List listeners = BeanFactoryUtils.beansOfType(ApplicationListener.class, this);
		logger.debug("Found " + listeners.size() + " listeners in bean factory");
		for (int i = 0; i < listeners.size(); i++) {
			ApplicationListener listener = (ApplicationListener) listeners.get(i);
			addListener(listener);
			logger.info("Bean listener added: [" + listener + "]");
		}
	}

	/**
	 * Destroy the singletons in the bean factory of this application context.
	 */
	public void close() {
		logger.info("Closing application context [" + getDisplayName() + "]");

		// destroy all cached singletons in this context,
		// invoking DisposableBean.destroy and/or "destroy-method"
		getBeanFactory().destroySingletons();

		// publish respective event
		publishEvent(new ContextClosedEvent(this));
	}

	/**
	 * Publish the given event to all listeners.
	 * <p>Note: Listeners get initialized after the message source, to be able
	 * to access it within listener implementations. Thus, message source
	 * implementation cannot publish events.
	 * @param event event to publish. The event may be application-specific,
	 * or a standard framework event.
	 */
	public final void publishEvent(ApplicationEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("Publishing event in context [" + getDisplayName() + "]: " + event.toString());
		}
		this.eventMulticaster.onApplicationEvent(event);
		if (this.parent != null) {
			parent.publishEvent(event);
		}
	}

	/**
	 * Add a listener. Any beans that are listeners are automatically added.
	 */
	protected void addListener(ApplicationListener l) {
		this.eventMulticaster.addApplicationListener(l);
	}

	/**
	 * This implementation supports fully qualified URLs and appropriate
	 * (file) paths, via getResourceByPath.
	 * Throws a FileNotFoundException if getResourceByPath returns null.
	 * @see #getResourceByPath
	 */
	public final InputStream getResourceAsStream(String location) throws IOException {
		try {
			// try URL
			URL url = new URL(location);
			logger.debug("Opening as URL: " + location);
			return url.openStream();
		} catch (MalformedURLException ex) {
			// no URL -> try (file) path
			InputStream in = getResourceByPath(location);
			if (in == null) {
				throw new FileNotFoundException("Location '" + location + "' isn't a URL and cannot be interpreted as (file) path");
			}
			return in;
		}
	}

	/**
	 * Return input stream to the resource at the given (file) path.
	 * <p>Default implementation supports file paths, either absolute or
	 * relative to the application's working directory. This should be
	 * appropriate for standalone implementations but can be overridden,
	 * e.g. for implementations targetted at a container.
	 * @param path path to the resource
	 * @return InputStream for the specified resource, can be null if
	 * not found (instead of throwing an exception)
	 * @throws IOException exception when opening the specified resource
	 */
	protected InputStream getResourceByPath(String path) throws IOException {
		return new FileInputStream(path);
	}

	/**
	 * This implementation returns the working directory of the Java VM.
	 * This should be appropriate for standalone implementations but can
	 * be overridden for implementations targetted at a container.
	 */
	public String getResourceBasePath() {
		return (new File("")).getAbsolutePath() + File.separatorChar;
	}

	public synchronized Object sharedObject(String key) {
		return this.sharedObjects.get(key);
	}

	public synchronized void shareObject(String key, Object o) {
		logger.info("Set shared object '" + key + "'");
		this.sharedObjects.put(key, o);
	}

	public synchronized Object removeSharedObject(String key) {
		logger.info("Removing shared object '" + key + "'");
		Object o = this.sharedObjects.remove(key);
		if (o == null) {
			logger.warn("Shared object '" + key + "' not present; could not be removed");
		} else {
			logger.info("Removed shared object '" + key + "'");
		}
		return o;
	}


	//---------------------------------------------------------------------
	// Implementation of MessageSource
	//---------------------------------------------------------------------

	public String getMessage(String code, Object args[], String defaultMessage, Locale locale) {
		return this.messageSource.getMessage(code, args, defaultMessage, locale);
	}

	public String getMessage(String code, Object args[], Locale locale) throws NoSuchMessageException {
		return this.messageSource.getMessage(code, args, locale);
	}

	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		return this.messageSource.getMessage(resolvable, locale);
	}


	//---------------------------------------------------------------------
	// Implementation of BeanFactory
	//---------------------------------------------------------------------

	public Object getBean(String name) throws BeansException {
		Object bean = getBeanFactory().getBean(name);
		configureManagedObject(name, bean);
		return bean;
	}

	public Object getBean(String name, Class requiredType) throws BeansException {
		Object bean = getBeanFactory().getBean(name, requiredType);
		configureManagedObject(name, bean);
		return bean;
	}

	public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		return getBeanFactory().isSingleton(name);
	}

	public String[] getAliases(String name) throws NoSuchBeanDefinitionException {
		return getBeanFactory().getAliases(name);
	}


	//---------------------------------------------------------------------
	// Implementation of ListableBeanFactory
	//---------------------------------------------------------------------

	public int getBeanDefinitionCount() {
		return getBeanFactory().getBeanDefinitionCount();
	}

	public String[] getBeanDefinitionNames() {
		return getBeanFactory().getBeanDefinitionNames();
	}

	public String[] getBeanDefinitionNames(Class type) {
		return getBeanFactory().getBeanDefinitionNames(type);
	}

	public BeanFactory getParentBeanFactory() {
		return getParent();
	}

	/** Show information about this context */
	public String toString() {
		StringBuffer sb = new StringBuffer("ApplicationContext: displayName=[" + this.displayName + "]; ");
		sb.append("class=[" + getClass().getName() + "]; ");
		sb.append("BeanFactory={" + getBeanFactory() + "}; ");
		sb.append("} MessageSource={" + this.messageSource + "}; ");
		sb.append("ContextOptions={" + this.contextOptions + "}; ");
		sb.append("Startup date=" + new Date(this.startupTime) + "; ");
		if (this.parent == null)
			sb.append("Root of ApplicationContext hierarchy");
		else
			sb.append("Parent={" + this.parent + "}");
		return sb.toString();
	}


	//---------------------------------------------------------------------
	// Abstract methods that must be implemented by subclasses
	//---------------------------------------------------------------------

	/**
	 * Subclasses must implement this method to perform the actual configuration load.
	 * The method is invoked by refresh before any other initialization work.
	 *
	 * 子类必须实现此方法才能执行实际的配置加载。 在任何其他初始化工作之前，通过刷新调用该方法。
	 *
	 * @see #refresh
	 */
	protected abstract void refreshBeanFactory() throws ApplicationContextException;

	/**
	 * Subclasses must return their internal bean factory here.
	 * They should implement the lookup efficiently, so that it can be called
	 * repeatedly without a performance penalty.
	 * @return this application context's internal bean factory
	 */
	protected abstract ListableBeanFactoryImpl getBeanFactory();

}
