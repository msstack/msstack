package com.grydtech.msstack.microservices.jersey;

import com.grydtech.msstack.microservices.jaxrs.JaxRSApplication;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class JerseyApplication extends JaxRSApplication {

	@Override
	public void start() throws Exception {
		ServletContainer jerseyServlet = new ServletContainer(ResourceConfig.forApplicationClass(getClass()));
		ServletHolder holder = new ServletHolder(jerseyServlet);
		holder.setInitParameter(CommonProperties.METAINF_SERVICES_LOOKUP_DISABLE, "true");

		Server server = new Server(getPort());
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.addServlet(holder, "/*");
		server.setHandler(context);

		try {
			server.start();
			server.join();
		}
		finally {
			server.destroy();
		}
	}

}
