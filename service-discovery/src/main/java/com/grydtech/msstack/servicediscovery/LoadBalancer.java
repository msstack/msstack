package com.grydtech.msstack.servicediscovery;

import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceProvider;

import java.util.Map;

/**
 * Created by dileka on 4/6/18.
 */
public class LoadBalancer extends MembershipProtocolCuratorImpl {
    
    ServiceProvider<Member> serviceProvider;
    
    public LoadBalancer(String connectionString) {
        super(connectionString);
    }
    
    public void registerManager(String workerName, String basePath, Map<String, Object> attributes) throws Exception {
        
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(Member.class).basePath(basePath).client(this.client).build();
        this.serviceDiscovery.start();
        
        serviceProvider = serviceDiscovery.serviceProviderBuilder().serviceName(workerName).build();
        serviceProvider.start();
    }
    
    public ServiceProvider<Member> getServiceProvider() {
        return this.serviceProvider;
    }
    
}
