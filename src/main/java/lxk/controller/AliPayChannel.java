package lxk.controller;

import org.springframework.stereotype.Service;

/**
 * com.middlepay.common.service.channel.alipay
 * middlepay
 *
 * @author alnico
 * @EMAIL:SHENGMIAO@HKRT.CN
 * @Description: <br/>
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2018-12-04      SHENGMIAO          1.0             1.0
 */
@Service("ALIPAY")
public class AliPayChannel implements OnlineChannelBusiness  {


    /**
     * 是否需要重定向
     */
    @Override
    public boolean needRedirect() {
        return false;
    }

    /**
     * 渠道是够大商户模式
     *
     * @return
     */
    @Override
    public boolean isBigMerchant() {
        return false;
    }

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such
     *                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
