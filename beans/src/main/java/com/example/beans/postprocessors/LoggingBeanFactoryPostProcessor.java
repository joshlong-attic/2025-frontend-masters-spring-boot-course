package com.example.beans.postprocessors;

import com.example.beans.Loggable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

class LoggingBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		for (var beanName : beanFactory.getBeanNamesForType(Loggable.class)) {
			var beanDefinition = beanFactory.getBeanDefinition(beanName);
			var type = beanFactory.getType(beanName);
			if (Loggable.class.isAssignableFrom(type)) {
				System.out.println("bean " + beanName + " is a " + type.getSimpleName() + "and also a "
						+ Loggable.class.getSimpleName() + ". the scope is [" + beanDefinition.getScope() + ']');
			}
		}

	}

}
