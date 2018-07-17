package com.grydtech.msstack.request.jersey;

import com.grydtech.msstack.core.annotation.FrameworkComponent;
import com.grydtech.msstack.request.jaxrs.JaxRSRequestBroker;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

@FrameworkComponent
public class JerseyRequestBroker extends JaxRSRequestBroker {

    @Override
    public final void start() throws Exception {
        super.start();
        ServletContainer jerseyServlet = new ServletContainer(
                ResourceConfig.forApplicationClass(getClass()));
        ServletHolder holder = new ServletHolder(jerseyServlet);
        holder.setInitParameter(CommonProperties.METAINF_SERVICES_LOOKUP_DISABLE, "true");

        Server server = new Server(this.getPort());
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(holder, "/*");
        server.setHandler(context);
        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }
}
