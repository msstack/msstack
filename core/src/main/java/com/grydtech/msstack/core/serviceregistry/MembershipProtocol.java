package com.grydtech.msstack.core.serviceregistry;

import com.grydtech.msstack.core.annotation.AutoInjected;
import com.grydtech.msstack.core.annotation.FrameworkComponent;

import java.util.Map;

@FrameworkComponent
public abstract class MembershipProtocol {

    @AutoInjected
    @SuppressWarnings("unused")
    private static MembershipProtocol instance;

    public static MembershipProtocol getInstance() {
        return instance;
    }

    /**
     * Register a member with attributes in service registry
     *
     * @param serviceName Base Service of the member
     * @param host host of member
     * @param port port of member
     * @return Member if found, else exception
     */
    public abstract Member registerMember(String serviceName, String host, int port);

    public abstract Member updateMember(Member member);

    public abstract void removeMember(String memberName);

    public abstract void listen(String name, MemberListener memberListener);

    public abstract void setConnectionString(String connectionString);

    public abstract void start();

    public abstract void stop();

}
