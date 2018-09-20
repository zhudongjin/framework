package com.hstypay.framework.service.hessian.support;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianProxy;
import com.caucho.hessian.client.HessianProxyFactory;
import com.hstypay.sandbox.support.LogHelper;
import org.slf4j.MDC;

import java.net.URL;
import java.util.Map;

/**
 * 拓展HessianProxy，在客户端发送请求之前，将HessianHeaderContext中的请求头添加到请求中。
 *
 * @author Vincent
 * @version 1.0 2017-06-27 17:14
 */
public class SpringHessianProxy extends HessianProxy {

    protected SpringHessianProxy(URL url, HessianProxyFactory factory) {
        super(url, factory);
    }

    protected SpringHessianProxy(URL url, HessianProxyFactory factory, Class<?> type) {
        super(url, factory, type);
    }

    @Override
    protected void addRequestHeaders(HessianConnection conn) {
        super.addRequestHeaders(conn);

        // add Hessian Header
        Map<String, String> headerMap = HessianHeaderContext.getContext().getHeaders();
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            conn.addHeader(entry.getKey(), entry.getValue());
        }

        // add request id
        if (MDC.get(LogHelper.KEY_REQUEST_ID) != null) {
            conn.addHeader(LogHelper.KEY_REQUEST_ID, MDC.get(LogHelper.KEY_REQUEST_ID));
        }
    }
}
