package nl.fontys.scope.networking;

import com.esotericsoftware.kryo.Kryo;

import nl.fontys.scope.networking.broadCasts.BroadcastRequest;
import nl.fontys.scope.networking.broadCasts.CollisionBroadCast;
import nl.fontys.scope.networking.broadCasts.GameObjectBroadCast;
import nl.fontys.scope.networking.broadCasts.MovementBroadCast;
import nl.fontys.scope.networking.broadCasts.ObjectBroadCast;
import nl.fontys.scope.networking.broadCasts.PlayerBroadCast;
import nl.fontys.scope.networking.requests.GameCreateRequest;
import nl.fontys.scope.networking.requests.GameStartedCheckRequest;
import nl.fontys.scope.networking.requests.GetGamesRequest;
import nl.fontys.scope.networking.requests.JoinRequest;
import nl.fontys.scope.networking.responses.GameStartedCheckResponse;
import nl.fontys.scope.networking.responses.GameStartedResponse;
import nl.fontys.scope.networking.responses.GetGamesResponse;
import nl.fontys.scope.networking.responses.JoinedResponse;
import nl.fontys.scope.networking.serverobjects.ServerGame;

public abstract class ScopeNetworkingHelper {

    public static final int TCP_PORT = 54555;
    public static final int UDP_PORT =  54777;

    public static void registerClasses(Kryo kryo){

        // Generalstuff
        kryo.register(java.util.HashMap.class);
        kryo.register(java.util.HashSet.class);
        kryo.register(ServerGame.class);
        kryo.register(com.esotericsoftware.kryonet.Connection.class);
        kryo.register(com.esotericsoftware.kryonet.Server.class);
        kryo.register(com.esotericsoftware.kryonet.Connection[].class);

        // Broadcasts
        kryo.register(BroadcastRequest.class);
        kryo.register(CollisionBroadCast.class);
        kryo.register(GameObjectBroadCast.class);
        kryo.register(MovementBroadCast.class);
        kryo.register(ObjectBroadCast.class);
        kryo.register(PlayerBroadCast.class);

        // Request
        kryo.register(GameCreateRequest.class);
        kryo.register(GetGamesRequest.class);
        kryo.register(JoinRequest.class);
        kryo.register(GameStartedCheckRequest.class);

        // Responses
        kryo.register(GetGamesResponse.class);
        kryo.register(JoinedResponse.class);
        kryo.register(GameStartedResponse.class);
        kryo.register(GameStartedCheckResponse.class);
    }
}
