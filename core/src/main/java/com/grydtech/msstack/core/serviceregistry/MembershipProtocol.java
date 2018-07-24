package com.grydtech.msstack.core.serviceregistry;

import com.grydtech.msstack.core.annotation.AutoInjected;
import com.grydtech.msstack.core.annotation.FrameworkComponent;
import com.grydtech.msstack.core.annotation.Value;
import com.grydtech.msstack.core.configuration.ApplicationConfiguration;

@FrameworkComponent
public abstract class MembershipProtocol {

    @Value
    protected static ApplicationConfiguration applicationConfiguration;

    @AutoInjected
    @SuppressWarnings("unused")
    private static MembershipProtocol instance;

    public static MembershipProtocol getInstance() {
        return instance;
    }

    public abstract Member register();

    //ToDo: need to change implementation (member details already in application config)
    public abstract Member updateMember(Member member);

    //ToDo: need to change implementation (member details already in application config)
    public abstract void removeMember(String memberName);

    public abstract void listen(String name, MemberListener memberListener);

    public abstract void start();

    public abstract void stop();

}
