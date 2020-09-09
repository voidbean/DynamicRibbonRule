package com.voidbean.service;

import com.voidbean.model.MyRibbon

public interface LoadBalancerService {

	//Get a ribbon rule
    MyRibbon getLoadBalancerByName(String name);

    Boolean setLoadBalancer(MyRibbon myRibbon);


}
