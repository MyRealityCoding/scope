package nl.fontys.scope.net.server;

import java.io.Serializable;

import nl.fontys.scope.core.Player;
import nl.fontys.scope.object.GameObject;

public class Responses {

    // list of all responses
    public static class GameCreated extends Response {

        private String clientId;

        public GameCreated() {
            // noOp
        }

        public GameCreated(String gameId, String clientId) {
            super(gameId);
            this.clientId = clientId;
        }

        public String getClientId() {
            return clientId;
        }
    }

    public static class GameReady extends Response {

        public GameReady() {
            // noOp
        }

        public GameReady(String gameId) {
            super(gameId);
        }
    }

    public static class GameOver extends Response {

        private String winnerId;

        public GameOver() {
            // noOp
        }

        public GameOver(String gameId, String winnerId) {
            super(gameId);
            this.winnerId = winnerId;
        }

        public String getWinnerId() {
            return winnerId;
        }
    }

    public static class ClientJoined extends Response {

        private String clientId;

        private String gameObjectId;

        public ClientJoined() {
            // noOp
        }

        public ClientJoined(String gameId, String clientId, String gameObjectId) {
            super(gameId);
            this.clientId = clientId;
            this.gameObjectId = gameObjectId;
        }

        public String getClientId() {
            return clientId;
        }

        public String getGameObjectId() {
            return gameObjectId;
        }
    }

    public static class ClientLeft extends Response {

        private String clientId;

        public ClientLeft() {
            // noOp
        }

        public ClientLeft(String gameId, String clientId) {
            super(gameId);
            this.clientId = clientId;
        }

        public String getClientId() {
            return clientId;
        }
    }

    public static class GameObjectAdded extends Response {

        private GameObject object;

        private String clientId;

        public GameObjectAdded() {
            // noOp
        }

        public GameObjectAdded(String gameId, String clientId, GameObject object) {
            super(gameId);
            this.clientId = clientId;
            this.object = object;
        }

        public String getClientId() {
            return clientId;
        }

        public GameObject getGameObject() {
            return object;
        }
    }

    public static class GameObjectRemoved extends Response {

        private String objectId;

        private String clientId;

        public GameObjectRemoved() {
            // noOp
        }

        public GameObjectRemoved(String gameId, String clientId, String objectId) {
            super(gameId);
            this.clientId = clientId;
            this.objectId = objectId;
        }

        public String getGameObjectId() {
            return objectId;
        }

        public String getClientId() {
            return clientId;
        }
    }

    public static class GameObjectUpdated extends Response {

        private GameObject object;

        private String clientId;

        public GameObjectUpdated() {
            // noOp
        }

        public GameObjectUpdated(String gameId, String clientId, GameObject object) {
            super(gameId);
            this.clientId = clientId;
            this.object = object;
        }

        public GameObject getGameObject() {
            return object;
        }

        public String getClientId() {
            return clientId;
        }
    }

    public static class ClientUpdated extends Response {

        private String clientId;

        private int points;

        public ClientUpdated() {
            // noOp
        }

        public ClientUpdated(String gameId, String clientId, int points) {
            super(gameId);
            this.clientId = clientId;
            this.points = points;
        }

        public String getClientId() {
            return clientId;
        }

        public int getPoints() {
            return points;
        }
    }

    // private classes
    private static class Response implements Serializable {

        private String gameId;

        public Response() {
            // noOp
        }

        public Response(String gameId) {
            this.gameId = gameId;
        }

        public String getGameId() {
            return gameId;
        }
    }
}
