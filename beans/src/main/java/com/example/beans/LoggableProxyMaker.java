package com.example.beans;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;

public abstract class LoggableProxyMaker {

	static final Logger logger = LoggerFactory.getLogger(LoggableProxyMaker.class);

	@SuppressWarnings("unchecked")
	public static <T> T proxy(T target) {
		var pfb = new ProxyFactoryBean();
		pfb.setTarget(target);
		pfb.setProxyTargetClass(true);
		pfb.addAdvice((MethodInterceptor) invocation -> {
			var start = System.currentTimeMillis();
			var result = (Object) null;
			try {
				result = invocation.proceed();
			}
			finally {
				var end = System.currentTimeMillis();
				logger.info("method {} took {} ms", invocation.getMethod().getName(), end - start);
			}
			return result;
		});
		return (T) pfb.getObject();
	}

}
