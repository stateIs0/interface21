
package com.interface21.beans;

/**
 * Interface that can be implemented by application code that needs
 * to validate PropertyValues. If an implementation of this
 * interface is supplied, it will be invoked by a BeanWrapper implementation's
 * setPropertyValues() method.
 *
 * 可以通过需要来验证 PropertyValues 的应用程序代码实现的接口。
 * 如果提供了此接口的实现，它将由 BeanWrapper 实现的 setPropertyValues（）方法调用。
 *
 * @author  Rod Johnson
 * @version $Revision$ 
 */
public interface PropertyValuesValidator {
	
	/** 
	 * Are these PropertyValues invalid?
	 * For example,
	 * are required properties missing? Does the presence of a particular
	 * property require others to be present? Implementations can
	 * rely on the contains() method of the PropertyValues interface, but cannot
	 * assume anything about the type of properties: type conversion is done only later,
	 * when PropertyValues are applied to a bean using a BeanWrapper object.
	 * @param pvs PropertyValues to validate
	 * @throws InvalidPropertyValuesException if the PropertyValues object is invalid
	 */
	void validatePropertyValues(PropertyValues pvs) throws InvalidPropertyValuesException;

}

