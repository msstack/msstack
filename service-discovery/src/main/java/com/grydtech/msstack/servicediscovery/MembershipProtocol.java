package com.grydtech.msstack.servicediscovery;

import java.util.Map;

public interface MembershipProtocol {

	Member registerMember(String memberName, Map<String, Object> attributes);

	Member updateMember(Member member);

	void removeMember(String memberName);

	void listen(String name, MemberListener memberListener);

	void closeConnection();

}
