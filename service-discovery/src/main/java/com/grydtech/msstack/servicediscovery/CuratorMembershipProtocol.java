package com.grydtech.msstack.servicediscovery;

import com.grydtech.msstack.core.serviceregistry.Member;
import com.grydtech.msstack.core.serviceregistry.MemberListener;
import com.grydtech.msstack.core.serviceregistry.MembershipProtocol;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CuratorMembershipProtocol extends MembershipProtocol {

    private static final Logger LOGGER = Logger.getLogger(CuratorMembershipProtocol.class.toGenericString());
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
                    .setId(applicationConfiguration.getServer().getId())
                    .setName(applicationConfiguration.getServer().getName())
                    .setHost(InetAddress.getLocalHost().getHostAddress())
                    .setPort(applicationConfiguration.getServer().getPort());
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

    public List<Member> getRegisteredServices(String serviceName){
        try {
            String path = BASE_PATH+serviceName;
            List<String> memberPaths =  curatorFrameworkClient.getChildren().forPath(path);
            ArrayList<Member> members = new ArrayList<>();
            for(String memPath : memberPaths ){
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
    public void start() {
        curatorFrameworkClient = CuratorFrameworkFactory.newClient(
                applicationConfiguration.getServiceRegistry().getBootstrap(),
                new RetryNTimes(5, 1000)
        );
        curatorFrameworkClient.start();
    }

    @Override
    public void stop() {
        curatorFrameworkClient.close();
    }
}
