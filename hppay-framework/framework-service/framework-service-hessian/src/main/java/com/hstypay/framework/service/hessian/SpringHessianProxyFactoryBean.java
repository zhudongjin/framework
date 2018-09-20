package com.hstypay.framework.service.hessian;

import com.caucho.hessian.client.HessianProxyFactory;
import com.hstypay.framework.core.configuration.AppConfiguration;
import com.hstypay.framework.service.hessian.support.SpringHessianProxyFactory;
import com.hstypay.sandbox.error.IError;
import com.hstypay.sandbox.exception.BusinessException;
import com.hstypay.sandbox.exception.CommonErrorCodes;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

/**
 * 重写hessian用户在请求头写入日志编码
 *
 * @author Vincent
 * @version 1.0 2017-06-27 16:47
 */
public class SpringHessianProxyFactoryBean extends HessianProxyFactoryBean {

    private HessianProxyFactory proxyFactory = new SpringHessianProxyFactory();

    private static final String HESSIAN_READ_TIMEOUT = "hessian.read.timeout";
    private static final String HESSIAN_CONNECT_TIMEOUT = "hessian.connect.timeout";

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return super.invoke(invocation);
        } catch (RemoteConnectFailureException e) {
            throw new BusinessException(CommonErrorCodes.THIRD_SERVER_EXCEPTION.toErrorCode(), e);
        } catch (Exception e) {
            if(e instanceof IError){
                throw e;
            }
            throw new BusinessException(CommonErrorCodes.THIRD_SERVER_EXCEPTION.toErrorCode(), e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        AppConfiguration configuration = AppConfiguration.getInstance();
        Object readTimeout = configuration.get(HESSIAN_READ_TIMEOUT);
        if(readTimeout != null && StringUtils.isNotBlank(readTimeout.toString())){
            logger.debug("init hessian readTimeout:" + readTimeout.toString());
            proxyFactory.setReadTimeout(NumberUtils.toInt(readTimeout.toString(), 10000));
        }
        Object connectTimeout = configuration.get(HESSIAN_CONNECT_TIMEOUT);
        if(connectTimeout != null && StringUtils.isNotBlank(connectTimeout.toString())){
            logger.debug("init hessian connectTimeout:" + connectTimeout.toString());
            proxyFactory.setConnectTimeout(NumberUtils.toInt(connectTimeout.toString(), 10000));
        }
        setProxyFactory(proxyFactory);
        super.afterPropertiesSet();
    }
}
