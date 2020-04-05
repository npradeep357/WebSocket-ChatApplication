package com.test.chat_ws.server;

import java.io.Closeable;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ShutdownHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.chat_ws.exception.ServerException;

/**
 * @author npradeep357
 */
public class JettyServerImpl implements Closeable {

    /*
     * @WebServlet(name = "MyEcho WebSocket Servlet", urlPatterns = { "/chat" }) private final class
     * WebSocketServletImpl extends WebSocketServlet { private static final long serialVersionUID = 1L;
     * @Override public void configure( WebSocketServletFactory factory) { factory.getPolicy().setIdleTimeout(0);
     * factory.register(WebSocketServer.class); } }
     */

    private static final Logger LOG = LoggerFactory.getLogger(JettyServerImpl.class);

    private int port;
    private Server server;

    public JettyServerImpl() {
        this.port = 0;
        this.server = new Server();
    }

    public void start() throws ServerException {

        addConnector("WS_CHAT");

        try {
            ShutdownHandler sh = new ShutdownHandler("", false, true);

            ServletContextHandler webSocketCtxHandler = createServletContextHandler("/", "/*", new DefaultServlet());

            addWebSocketContext(0, WebSocketServer.class, webSocketCtxHandler);

            Handler handler = new HandlerList(sh, webSocketCtxHandler);
            server.setHandler(handler);

            server.start();

            LOG.info("server started on port {}", port);

            server.join();
        } catch (Exception e) {
            throw new ServerException(e.getMessage(), e.getCause());
        }
    }

    public void stop() throws ServerException {
        if (!server.isStopped()) {
            try {
                server.stop();
            } catch (Exception e) {
                throw new ServerException(e.getMessage(), e.getCause());
            }
        }
    }

    @Override
    public void close() throws IOException {
        try {
            stop();
        } catch (ServerException e) {
            LOG.error(e.getMessage());
        }
    }

    private Connector createConnector(
            String name) {
        // creating a connector to listen for HTTP calls on provided port
        try (ServerConnector connector = new ServerConnector(server)) {
            connector.setPort(port);
            connector.setName(name);
            return connector;
        }
    }

    protected void addConnector(
            String name) {
        Connector connector = createConnector(name);
        server.setConnectors(new Connector[] { connector });
    }

    protected ServletContextHandler createServletContextHandler(
            String contextPath,
            String pathSpec,
            HttpServlet servletContainer) {
        ServletHolder servlet = new ServletHolder(servletContainer);
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath(contextPath);
        context.addServlet(servlet, pathSpec);
        context.setServer(server);
        return context;
    }

    protected void addWebSocketContext(
            long maxSessionIdleTimeout,
            Class<?> endpointClazz,
            ServletContextHandler contextHandler) {
        Configurator configurator = (
                ServletContext servletContext,
                org.eclipse.jetty.websocket.jsr356.server.ServerContainer serverContainer) -> {
            serverContainer.setDefaultMaxSessionIdleTimeout(maxSessionIdleTimeout);
            serverContainer.addEndpoint(endpointClazz);
        };

        WebSocketServerContainerInitializer.configure(contextHandler, configurator);
    }

    public void setPort(
            int port) {
        this.port = port;
    }

}
