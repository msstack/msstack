package com.grydtech.msstack.servicediscovery;

import junit.framework.TestCase;
import org.apache.curator.test.TestingServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CuratorServiceRegistryConnectorTest extends TestCase {

    private CuratorServiceRegistryConnector instance;
    private int port;
    private String host;
    private TestingServer zkTestServer;

    public CuratorServiceRegistryConnectorTest(String testName) {
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
//        super.setUp();
//        startZookeeper();
//        instance = new CuratorServiceRegistryConnector();
//        instance.setConnectionString(host + ":" + databasePort);
//        instance.start();
    }

    @Override
    protected void tearDown() throws Exception {
//        super.tearDown();
//        instance.stop();
//        stopZookeeper();
    }

    private Map<String, String> createMemberAttributes(String host, int port) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("host", host);
        attributes.put("databasePort", String.valueOf(port));
        return attributes;
    }

    /**
     * Test of register method, of class MembershipProtocolCuratorImpl.
     */
    public void testRegisterMember() {
        //ToDo: Refine test case (need to change member find method)
//        System.out.println("Test Register Member");
//        // Create Member
//        String serviceId = "order-service-1";
//        String serviceName = "order-service";
//        String memberName = "127.0.0.1:8888";
//        int memberPort = 8888;
////        Map<String, String> attributes = createMemberAttributes(host, memberPort);
//
//        // Insert member and assert properties
//        String expResult = "127.0.0.1:8888";
//        Member result = instance.register(serviceId, serviceName, host,memberPort);
//
//
//        //get the member named "127.0.0.1:8888"
//        instance.findMember(memberName);
//
//        //get all the members under /services/order
//        List<Member> list =  instance.getRegisteredServices("order");
//        for(Member s : list){
//            System.out.println(s.getHost()+" : "+s.getDatabasePort());
//        }
//
//        assertEquals(expResult, result.getDatabaseName());
//        assertEquals(memberPort, result.getDatabasePort());
//        System.out.println("Register Member Test Completed");


//        // Create Member
//        String serviceName = "order";
//        String memberName = "127.0.0.1:8888";
//        int memberPort = 8888;
////        Map<String, String> attributes = createMemberAttributes(host, memberPort);
//
//        // Insert member and assert properties
//        String expResult = "127.0.0.1:8888";
//        Member result = instance.register(serviceName, host,memberPort);
//
////        instance.findMember("order");
//
//        assertEquals(expResult, result.getDatabaseName());
//        assertEquals(memberPort, result.getDatabasePort());
//        System.out.println("Register Member Test Completed");
    }

}
