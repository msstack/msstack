package com.grydtech.msstack.core.serviceregistry;

public class MembershipEvent {

    private final MembershipEventType eventType;

    private final Member member;

    public MembershipEvent(MembershipEventType eventType, Member member) {
        this.eventType = eventType;
        this.member = member;
    }

    public MembershipEventType getEventType() {
        return eventType;
    }

    public Member getMember() {
        return member;
    }

}
