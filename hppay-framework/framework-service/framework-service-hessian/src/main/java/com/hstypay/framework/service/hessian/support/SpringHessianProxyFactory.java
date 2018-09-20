package com.hstypay.framework.service.hessian.support;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.HessianRemoteObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;

/**
 * @author Vincent
 * @version 1.0 2017-06-27 17:20
 */
public class SpringHessianProxyFactory extends HessianProxyFactory {

    @Override
    public Object create(Class<?> api, URL url, ClassLoader loader) {
        if (api == null) {
            throw new NullPointerException("api must not be null for HessianProxyFactory.create()");
        }

        InvocationHandler handler = new SpringHessianProxy(url, this, api);

        return Proxy.newProxyInstance(loader, new Class[]{api, HessianRemoteObject.class}, handler);
    }
}
