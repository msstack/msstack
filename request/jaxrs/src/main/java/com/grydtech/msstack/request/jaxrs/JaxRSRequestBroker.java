package com.grydtech.msstack.request.jaxrs;

import com.grydtech.msstack.annotation.FrameworkComponent;

@FrameworkComponent
public abstract class JaxRSRequestBroker {

// COMMENTED OUT BECAUSE INTERNAL COMMUNICATION NOW HAPPENS VIA MESSAGE BUS
//    @Override
//    public void start() throws Exception {
//        getClasses().add(JacksonFeature.class);
//    }

}
