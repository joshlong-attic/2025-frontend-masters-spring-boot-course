package com.example.beans.postprocessors;

import com.example.beans.Loggable;
import com.example.beans.LoggableProxyMaker;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

class LoggingBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof Loggable) {
			return LoggableProxyMaker.proxy(bean);
		}
		return bean;
	}

}
