package com.grydtech.msstack.servicediscovery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MembershipProtocolCuratorImpl implements MembershipProtocol {

    private static final Logger LOGGER = Logger.getLogger(MembershipProtocolCuratorImpl.class.toGenericString());

    private static final String BASE_PATH = "/services";

    protected final CuratorFramework client;

    protected ServiceDiscovery<Member> serviceDiscovery;

    private Gson gson;

    private UriSpec uriSpec;

    private ServiceInstance<Member> serviceInstance;

    MembershipProtocolCuratorImpl(String connectionString) {
        client = CuratorFrameworkFactory.newClient(connectionString, new RetryNTimes(5, 1000));
        gson = new GsonBuilder().create();
        client.start();
    }

    public Member createMember(String memberName, Map<String, Object> attributes) {
        JsonElement jsonElement = gson.toJsonTree(attributes);
        JsonObject jsonObject = (JsonObject) jsonElement;
        return new Member(memberName, jsonObject);
    }

    @Override
    public Member registerMember(String memberName, String basePath, Map<String, Object> attributes) {
        try {
            Member member = createMember(memberName, attributes);
            JsonInstanceSerializer<Member> serializer = new JsonInstanceSerializer<>(Member.class);

            uriSpec = new UriSpec(member.getIp() + ":{" + member.getPort() + "}");

            serviceInstance = ServiceInstance.<Member>builder().address(member.getIp()).name(memberName)
                    .port(member.getPort()).uriSpec(uriSpec).payload(member).build();

            serviceDiscovery = ServiceDiscoveryBuilder.builder(Member.class).client(client).basePath(basePath)
                    .serializer(serializer).thisInstance(serviceInstance).build();

            serviceDiscovery.start();
            System.out.println("service discovery started");
            LOGGER.info("service discovery started");
            return member;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Member updateMember(Member member) {
        try {
            uriSpec = new UriSpec(String.format("%s:{%d}", member.getIp(), member.getPort()));
            serviceInstance = ServiceInstance.<Member>builder().address(member.getIp()).name(member.getName())
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
    public void closeConnection() {
        client.close();
    }
}
