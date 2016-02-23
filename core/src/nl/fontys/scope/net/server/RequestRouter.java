package nl.fontys.scope.net.server;

import com.esotericsoftware.kryonet.Connection;

import java.util.HashMap;
import java.util.Map;

import nl.fontys.scope.net.handlers.RequestHandler;

/**
 * Created by miguel on 23.02.16.
 */
public class RequestRouter {

    private Map<Class<?>, RequestHandler> handlers;

    public RequestRouter() {
        handlers = new HashMap<Class<?>, RequestHandler>();
    }

    public void registerHandler(RequestHandler handler) {
        handlers.put(handler.getType(), handler);
    }

    public void route(Connection connection, Object object, GameInstanceManager gameInstanceManager) {
        Class<?> key = object.getClass();
        if (handlers.containsKey(key)) {
            handlers.get(key).handle(connection, object, gameInstanceManager);
        }
    }
}
