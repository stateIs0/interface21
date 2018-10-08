
package com.interface21.beans.factory;

/**
 * Interface to be implemented by beans that need to react
 * once all their properties have been set by a BeanFactory:
 * for example, to perform initialization, or merely to check
 * that all mandatory properties have been set.
 *
 * 接口由bea​​n实现，一旦所有属性都由BeanFactory设置，就需要做出反应：例如，执行初始化，或仅检查已设置所有必需属性。
 *
 * @author Rod Johnson
 */
public interface InitializingBean {
	
	/**
	 * Invoked by a BeanFactory after it has set all bean properties supplied.
	 *
	 * 在BeanFactory设置了所有提供的bean属性之后调用它。
	 *
	 * <p>This method allows the bean instance to perform initialization only
	 * possible when all bean properties have been set and to throw an
	 * exception in the event of misconfiguration.
	 *
	 * 此方法允许bean实例仅在设置了所有bean属性时执行初始化*，并在配置错误时抛出*异常。
	 *
	 * @throws Exception in the event of misconfiguration (such
	 * as failure to set an essential property) or if initialization fails.
	 */
	void afterPropertiesSet() throws Exception;

}
