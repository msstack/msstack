package com.grydtech.msstack.servicediscovery;

import com.grydtech.msstack.core.connectors.serviceregistry.Member;
import com.grydtech.msstack.core.connectors.serviceregistry.MemberListener;
import com.grydtech.msstack.core.connectors.serviceregistry.ServiceRegistryConnector;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CuratorServiceRegistryConnector extends ServiceRegistryConnector {

    private static final Logger LOGGER = Logger.getLogger(CuratorServiceRegistryConnector.class.toGenericString());
    private static final String BASE_PATH = "/services/";
    private CuratorFramework curatorFrameworkClient;
    private ServiceDiscovery<Member> serviceDiscovery;
    private UriSpec uriSpec;
    private ServiceInstance<Member> serviceInstance;

    @Override
    public Member register() {
        try {
            //ToDo: added id attribute host and port is redundant because serviceInstance contain host and port
            Member member = new Member()
                    .setId(applicationConfiguration.getServiceRegistryConfiguration().getId())
                    .setName(applicationConfiguration.getServiceRegistryConfiguration().getName())
                    .setHost(applicationConfiguration.getServiceRegistryConfiguration().getHost())
                    .setPort(applicationConfiguration.getServiceRegistryConfiguration().getPort());
            JsonInstanceSerializer<Member> serializer = new JsonInstanceSerializer<>(Member.class);

            uriSpec = new UriSpec("{scheme}://{address}:{port}");

            serviceInstance = ServiceInstance
                    .<Member>builder()
                    .uriSpec(uriSpec)
                    .name(member.getName())
                    .address(member.getHost())
                    .port(member.getPort())
                    .payload(member)
                    .build();

            serviceDiscovery = ServiceDiscoveryBuilder.builder(Member.class)
                    .client(curatorFrameworkClient)
                    .basePath(BASE_PATH)
                    .serializer(serializer)
                    .thisInstance(serviceInstance)
                    .build();

            serviceDiscovery.start();
            LOGGER.info("service discovery started");
            return member;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    public void findMember(String memberName) {
        System.out.println("finding");
        try {
            Collection<ServiceInstance<Member>> instances = serviceDiscovery.queryForInstances(memberName);
            System.out.println(Arrays.toString(new Collection[]{instances}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Member> getRegisteredServices(String serviceName) {
        try {
            String path = BASE_PATH + serviceName;
            List<String> memberPaths = curatorFrameworkClient.getChildren().forPath(path);
            ArrayList<Member> members = new ArrayList<>();
            for (String memPath : memberPaths) {
                String[] attributes = memPath.split(":");
                Member mem = new Member()
                        .setName(memPath)
                        .setHost(attributes[0])
                        .setPort(Integer.valueOf(attributes[1]));
                members.add(mem);
            }
            return members;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Member updateMember(Member member) {
        try {
            uriSpec = new UriSpec(String.format("%s:{%d}", member.getHost(), member.getPort()));
            serviceInstance = ServiceInstance.<Member>builder().address(member.getHost()).name(member.getName())
                    .port(member.getPort()).uriSpec(uriSpec).payload(member).build();

            serviceDiscovery.updateService(serviceInstance);
            return member;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void removeMember(String memberName) {
        try {
            Collection<ServiceInstance<Member>> collection = serviceDiscovery.queryForInstances(memberName);
            if (!collection.isEmpty()) {
                serviceDiscovery.unregisterService(collection.iterator().next());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public void listen(String group, MemberListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void connect() throws IOException {
        curatorFrameworkClient = CuratorFrameworkFactory.newClient(
                applicationConfiguration.getServiceRegistryConfiguration().getBootstrap(),
                new RetryNTimes(5, 1000)
        );
        curatorFrameworkClient.start();
    }

    @Override
    public void disconnect() throws IOException {
        curatorFrameworkClient.close();
    }
}
