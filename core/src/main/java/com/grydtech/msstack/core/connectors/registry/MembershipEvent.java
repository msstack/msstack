package com.grydtech.msstack.core.connectors.registry;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MembershipEvent {

    private final MembershipEventType eventType;
    private final Member member;
}
