package com.sg;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
public class SgServer {
	public static void main(String[] args) throws Exception {

		Server server = new Server(7070);
		// resources
        final ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages("com.sg.rest.resources");
        final ServletContainer servletContainer = new ServletContainer(resourceConfig);
        final ServletHolder sh = new ServletHolder(servletContainer);
		ServletContextHandler context = new ServletContextHandler(server, "/");
		context.addServlet(sh, "/api/*");
		
		final ResourceHandler ui = new ResourceHandler();
        ContextHandler uiResource = new ContextHandler();
        uiResource.setAllowNullPathInfo(true);
        uiResource.setContextPath("/sg");
        uiResource.setResourceBase("src/main/webapp/resources");
        
        String contextPath = null;
        contextPath = uiResource.getContextPath();
        uiResource.setWelcomeFiles(new String[] { "index.html" });
        uiResource.setHandler(ui);
        
        final String finalContextPath = contextPath;
        final ServletHolder sh_default = new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                resp.sendRedirect(finalContextPath + "/");
            }
        });
        context.addServlet(sh_default, "");
        
        final ContextHandlerCollection handlers = new ContextHandlerCollection();
        handlers.addHandler(context);
        handlers.addHandler(uiResource);
        server.setHandler(handlers);
		server.start();

	}
}
