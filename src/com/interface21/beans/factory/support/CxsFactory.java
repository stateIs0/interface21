package com.interface21.beans.factory.support;

import com.interface21.beans.factory.NoSuchBeanDefinitionException;

/**
 *
 *
 * @author 玄灭
 * @date 2018/10/7-下午7:32
 */
public class CxsFactory extends AbstractBeanFactory {

    protected AbstractBeanDefinition getBeanDefinition(String beanName)
        throws NoSuchBeanDefinitionException {
        AbstractBeanDefinition bd = null;
//            (AbstractBeanDefinition) beanDefinitionMap.get(beanName);
        if (bd == null)
            throw new NoSuchBeanDefinitionException(beanName, toString());
        return bd;
    }
}
