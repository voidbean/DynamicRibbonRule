package com.voidbean.ribbon.service.impl;


import com.alibaba.fastjson.JSON;
import com.voidbean.ribbon.RibbonDefaultConfiguration;
import com.voidbean.model.MyRibbon;
import com.voidbean.service.LoadBalancerService;
import com.netflix.client.config.IClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.cloud.context.scope.GenericScope;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Ribbon负载均衡实现方式
 *
 * @author voidbean
 * @create 2019/3/2
 * @since 1.0.0
 */
@Service
@Slf4j
public class RibbonServiceImpl implements LoadBalancerService {
    
    @Value("${ribbon.NFLoadBalancerRuleClassName:com.netflix.loadbalancer.RoundRobinRule}")
    private String defaultClassName;

    @Qualifier("springClientFactory")
    @Autowired
    NamedContextFactory namedContextFactory;

    @Autowired
    DBFactory db;


    @Override
    public Object getLoadBalancerByName(String name) {
        return Optional.ofNullable(db.getRibbon()).orElse(getDefaultRibbonDefinition().setServiceName(name));
    }

    @Override
    public Boolean setLoadBalancer(MyRibbon myRibbon) {
         try {
             namedContextFactory.destroy();
            syncService.refreshClusterBean();
        } catch (Exception e) {
             log.error(e);
        }
        db.setRibbon(myRibbon)l
        return true;
    }
}
