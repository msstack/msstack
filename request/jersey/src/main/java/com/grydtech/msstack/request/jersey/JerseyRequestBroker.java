package com.grydtech.msstack.request.jersey;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.request.jaxrs.JaxRSRequestBroker;

@FrameworkComponent
public class JerseyRequestBroker extends JaxRSRequestBroker {

// COMMENTED OUT BECAUSE INTERNAL COMMUNICATION NOW HAPPENS VIA MESSAGE BUS
//    @Override
//    public final void start() throws Exception {
//        super.start();
//        ServletContainer jerseyServlet = new ServletContainer(
//                ResourceConfig.forApplicationClass(getClass()));
//        ServletHolder holder = new ServletHolder(jerseyServlet);
//        holder.setInitParameter(CommonProperties.METAINF_SERVICES_LOOKUP_DISABLE, "true");
//
//        Server server = new Server(applicationConfiguration.getServer().getPort());
//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
//        context.addServlet(holder, "/*");
//        server.setHandler(context);
//        try {
//            server.start();
//            server.join();
//        } finally {
//            server.destroy();
//        }
//    }
}
