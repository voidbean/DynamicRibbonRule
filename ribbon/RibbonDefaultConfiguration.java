package com.voidbean.ribbon;

import com.voidbean.model.MyRibbon
import com.voidbean.service.LoadBalancerService
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.IRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Ribbon Default Configuration
 * @author voidbean
 * @since 1.0.0
 */
@Slf4j
@Configuration
@RibbonClients(defaultConfiguration = RibbonDefaultConfiguration.class)
public class RibbonDefaultConfiguration {

    @Value("${ribbon.client.name:#{null}}")
    private String name;

    @Value("${ribbon.NFLoadBalancerRuleClassName:com.netflix.loadbalancer.RoundRobinRule}")
    private String className;

    @Autowired(required = false)
    private IClientConfig config;
    @Autowired
    private PropertiesFactory propertiesFactory;
    @Autowired
    private LoadBalancerService loadBalancerService;

    /**
     * Choose Rule 
     *
     * @param
     * @return:com.netflix.loadbalancer.IRule
     * @since: 1.0.0
     * @Author:voidbean
     */
    @Bean
    public IRule ribbonRule() {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        //RibbonDefinition
        MyRibbon ribbonDefinition = loadBalancerService.getLoadBalancerByName(name);
        if(ribbonDefinition!=null){
            AbstractLoadBalancerRule rule = null;
            try {
                rule = (AbstractLoadBalancerRule)Class.forName(ribbonDefinition.getRibbonClass()).newInstance();
                rule.initWithNiwsConfig(config);
                return rule;
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                log.error("Invalid ribbon rule:{}", e);
            }
        }
        if (this.propertiesFactory.isSet(IRule.class, name)) {
            return this.propertiesFactory.get(IRule.class, config, name);
        }
        try {
            AbstractLoadBalancerRule rule = (AbstractLoadBalancerRule)Class.forName(className).newInstance();
            rule.initWithNiwsConfig(config);
            return rule;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            log.error("Invalid ribbon rule:{}", e);
        }
        return null;
    }

}

