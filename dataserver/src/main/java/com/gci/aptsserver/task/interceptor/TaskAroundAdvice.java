package com.gci.aptsserver.task.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TaskAroundAdvice implements MethodInterceptor{

	@Override
	public Object invoke(MethodInvocation arg0) throws Throwable {
		// TODO Auto-generated method stub
        //System.out.println("clientName:"+arg0.getMethod().getDeclaringClass().getSimpleName());
        String className = arg0.getMethod().getDeclaringClass().getSimpleName();
        long startTime = System.currentTimeMillis();   //获取开始时间
        
        Object result= arg0.proceed();
        
        long endTime = System.currentTimeMillis(); //获取结束时间  
		System.out.println(className+"程序运行时间： "+(endTime-startTime)+"ms");
        
        return result;
	}

}
