/**
 *
 */
package com.hstypay.framework.session.support;

import com.hstypay.framework.session.SessionKeyGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 默认的session key生成器，用uuid方式生成
 *
 * @author Exception
 */
@Component
public class SimpleGenerator implements SessionKeyGenerator {

    @Override
    public String generateKey(String authId) {
        //uuid生成的串需要去除-
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        SimpleGenerator gen = new SimpleGenerator();
        System.out.println(gen.generateKey("aaaa"));
    }


}
