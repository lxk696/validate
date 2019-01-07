package lxk.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * com.middlepay.common.service.mfb
 * middlepay
 *
 * @author alnico
 * @Description: <br/>
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2018-12-19      alnico          1.0             1.0
 */
@Service("MFB")
public class MfbChannel implements OnlineChannelBusiness  {
    private static final Log logger = LogFactory.getLog(MfbChannel.class);

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
