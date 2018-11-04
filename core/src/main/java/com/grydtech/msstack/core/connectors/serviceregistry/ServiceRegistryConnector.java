package com.grydtech.msstack.core.connectors.serviceregistry;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.annotation.InjectInstance;
import com.grydtech.msstack.core.connectors.IConnector;

@FrameworkComponent
public abstract class ServiceRegistryConnector implements IConnector {

    @InjectInstance
    @SuppressWarnings("unused")
    private static ServiceRegistryConnector instance;

    public static ServiceRegistryConnector getInstance() {
        return instance;
    }

    public abstract Member register();

    //ToDo: need to change implementation (member details already in application config)
    public abstract Member updateMember(Member member);

    //ToDo: need to change implementation (member details already in application config)
    public abstract void removeMember(String memberName);

    public abstract void listen(String name, MemberListener memberListener);
}
