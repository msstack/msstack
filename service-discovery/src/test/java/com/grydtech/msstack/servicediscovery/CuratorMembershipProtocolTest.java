package com.grydtech.msstack.servicediscovery;

import com.grydtech.msstack.core.serviceregistry.Member;
import junit.framework.TestCase;
import org.apache.curator.test.TestingServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuratorMembershipProtocolTest extends TestCase {

    private CuratorMembershipProtocol instance;
    private int port;
    private String host;
    private TestingServer zkTestServer;

    public CuratorMembershipProtocolTest(String testName) {
        super(testName);
    }

    private void startZookeeper() throws Exception {
        host = "127.0.0.1";
        port = 2181;
        zkTestServer = new TestingServer(port);
    }

    private void stopZookeeper() throws IOException {
        zkTestServer.stop();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        startZookeeper();
        instance = new CuratorMembershipProtocol();
        instance.setConnectionString(host + ":" + port);
        instance.start();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        instance.stop();
        stopZookeeper();
    }

    private Map<String, String> createMemberAttributes(String host, int port) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("host", host);
        attributes.put("port", String.valueOf(port));
        return attributes;
    }

    /**
     * Test of registerMember method, of class MembershipProtocolCuratorImpl.
     */
    public void testRegisterMember() {
        System.out.println("Test Register Member");
        // Create Member
        String serviceId = "order-service-1";
        String serviceName = "order-service";
        String memberName = "127.0.0.1:8888";
        int memberPort = 8888;
//        Map<String, String> attributes = createMemberAttributes(host, memberPort);

        // Insert member and assert properties
        String expResult = "127.0.0.1:8888";
        Member result = instance.registerMember(serviceId, serviceName, host, memberPort);


        //get the member named "127.0.0.1:8888"
        instance.findMember(memberName);

        //get all the members under /services/order
        List<Member> list = instance.getRegisteredServices("order");
        for (Member s : list) {
            System.out.println(s.getHost() + " : " + s.getPort());
        }

        assertEquals(expResult, result.getName());
        assertEquals(memberPort, result.getPort());
        System.out.println("Register Member Test Completed");


//        // Create Member
//        String serviceName = "order";
//        String memberName = "127.0.0.1:8888";
//        int memberPort = 8888;
////        Map<String, String> attributes = createMemberAttributes(host, memberPort);
//
//        // Insert member and assert properties
//        String expResult = "127.0.0.1:8888";
//        Member result = instance.registerMember(serviceName, host,memberPort);
//
////        instance.findMember("order");
//
//        assertEquals(expResult, result.getName());
//        assertEquals(memberPort, result.getPort());
//        System.out.println("Register Member Test Completed");
    }

}
