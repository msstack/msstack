package com.grydtech.msstack.servicediscovery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import junit.framework.TestCase;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MembershipProtocolCuratorImplTest extends TestCase {

    MembershipProtocolCuratorImpl instance;

    int port;

    JsonElement jsonElement;

    JsonObject jsonObject;

    Gson gson;

    TestingServer zkTestServer;

    private CuratorFramework cli;

    public MembershipProtocolCuratorImplTest(String testName) {
        super(testName);
    }

    public void startZookeeper() throws Exception {
        port = 2181;
        zkTestServer = new TestingServer(port);
        cli = CuratorFrameworkFactory.newClient(zkTestServer.getConnectString(), new RetryOneTime(2000));
    }

    public void stopZookeeper() throws IOException {
        cli.close();
        zkTestServer.stop();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        startZookeeper();
        instance = new MembershipProtocolCuratorImpl("localhost:" + port);
        gson = new GsonBuilder().create();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        stopZookeeper();
    }

    /**
     * Test of registerMember method, of class MembershipProtocolCuratorImpl.
     */
    public void testRegisterMember() {
        System.out.println("registerMember");
        String memberName = "sampleName";
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("address.ip", "localhost");
        attributes.put("address.port", 7182);
        String expResult = "sampleName";
        Member result = instance.registerMember(memberName, attributes);
        System.out.println(result.getName());
        assertEquals(expResult, result.getName());
    }

    //    /**
    //     * Test of updateMember method, of class MembershipProtocolCuratorImpl.
    //     */
    //    public void testUpdateMember() {
    //        System.out.println("updateMember");
    //        Map<String, Object> attributes = new HashMap<>();
    //        attributes.put("address.ip", "localhost");
    //        attributes.put("address.port",7777);
    //        jsonElement = gson.toJsonTree(attributes);
    //        jsonObject = (JsonObject) jsonElement;
    //        Member member = new Member("/services","sampleName", jsonObject);
    //
    //        int expResult = 7777;
    //        Member result = instance.updateMember(member);
    //        assertEquals(expResult, result.getPort());
    //    }
    //
    //    /**
    //     * Test of removeMember method, of class MembershipProtocolCuratorImpl.
    //     */
    //    public void testRemoveMember() {
    //        System.out.println("removeMember");
    //        String memberName = "";
    //        MembershipProtocolCuratorImpl instance = null;
    //        instance.removeMember(memberName);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of listen method, of class MembershipProtocolCuratorImpl.
    //     */
    //    public void testListen() {
    //        System.out.println("listen");
    //        String group = "";
    //        MemberListener listener = null;
    //        MembershipProtocolCuratorImpl instance = null;
    //        instance.listen(group, listener);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of closeConnection method, of class MembershipProtocolCuratorImpl.
    //     */
    //    public void testCloseConnection() {
    //        System.out.println("closeConnection");
    //        MembershipProtocolCuratorImpl instance = null;
    //        instance.closeConnection();
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
}
