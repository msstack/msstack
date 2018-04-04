package com.grydtech.msstack.servicediscovery;

import java.util.Collection;
import java.util.Map;

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
import org.apache.log4j.Logger;

public class MembershipProtocolCuratorImpl implements MembershipProtocol {

	final static Logger LOGGER = Logger.getLogger(Member.class);

	private final CuratorFramework client;

	private final String BASE_PATH = "/services";

	Gson gson;

	JsonElement jsonElement;

	JsonObject jsonObject;

	UriSpec uriSpec;

	ServiceDiscovery<Member> serviceDiscovery;

	ServiceInstance<Member> serviceInstance;

	MembershipProtocolCuratorImpl(String connectionString) {
		client = CuratorFrameworkFactory.newClient(connectionString, new RetryNTimes(5, 1000));
		gson = new GsonBuilder().create();
		client.start();
	}

	private Member createMember(String memberName, Map<String, Object> attributes) {
		jsonElement = gson.toJsonTree(attributes);
		jsonObject = (JsonObject) jsonElement;
		return new Member(BASE_PATH, memberName, jsonObject);
	}

	@Override
	public Member registerMember(String memberName, Map<String, Object> attributes) {
		try {
			Member member = createMember(memberName, attributes);
			JsonInstanceSerializer<Member> serializer = new JsonInstanceSerializer<>(Member.class);

			uriSpec = new UriSpec(member.getIp() + ":{" + member.getPort() + "}");

			serviceInstance = ServiceInstance.<Member>builder()
					.address(member.getIp()).name(memberName).port(member.getPort()).uriSpec(uriSpec).payload(member)
					.build();

			serviceDiscovery = ServiceDiscoveryBuilder.builder(Member.class)
					.client(client)
					.basePath(BASE_PATH)
					.serializer(serializer)
					.thisInstance(serviceInstance)
					.build();

			serviceDiscovery.start();
			System.out.println("service discovery started");
			LOGGER.info("service discovery started");
			return member;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error("This is an exception : " + ex.getMessage());
			return null;
		}
	}

	@Override
	public Member updateMember(Member member) {
		try {
			uriSpec = new UriSpec(member.getIp() + ":{" + String.valueOf(member.getPort()) + "}");
			serviceInstance = ServiceInstance.<Member>builder()
					.address(member.getIp())
					.name(member.getName())
					.port(member.getPort())
					.uriSpec(uriSpec)
					.payload(member).build();

			serviceDiscovery.updateService(serviceInstance);
			return member;
		}
		catch (Exception ex) {
			LOGGER.error("This is an exception : " + ex.getMessage());
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
		}
		catch (Exception ex) {
			LOGGER.error("This is an exception : " + ex.getMessage());
		}
	}

	@Override
	public void listen(String group, MemberListener listener) {
		throw new UnsupportedOperationException(
				"Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void closeConnection() {
		client.close();
	}
}
