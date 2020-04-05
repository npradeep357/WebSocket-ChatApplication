package com.test.chat.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.chat.ws.exception.ServerException;
import com.test.chat.ws.server.JettyServerImpl;

/**
 * @author npradeep357
 */
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(
            String[] args) {

        // using auto closeable feature to stop server.
        try (var server = new JettyServerImpl()) {

            server.setPort(8080);

            // Blocked call. Waits until server shutdown called either by using ctrl+c or shutdown URL request
            server.start();

        } catch (ServerException e) {
            // for now just log it.
            LOG.error(e.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        System.exit(0);
    }
}
