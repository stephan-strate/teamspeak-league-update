package com.strate.remote.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;

import java.util.Date;
import java.util.logging.Level;

/**
 * <p>Represents a ts3 configuration, that's
 * responsible for the whole connection. You can overwrite
 * and customize everything.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public abstract class Connection {

    /**
     * <p>Config object with ip address/hostname,
     * reconnect strategies, ..</p>
     * @since 3.0.0
     */
    private TS3Config ts3Config;

    /**
     * <p>Query equipped with {@link TS3Config} object,
     * to connect/disconnect from sever.</p>
     * @since 3.0.0
     */
    private TS3Query ts3Query;

    /**
     * <p>Api fetched from {@link TS3Query} to
     * access teamspeak functions.</p>
     * @since 3.0.0
     */
    private TS3Api ts3Api;

    /**
     * <p>Default bot nickname.</p>
     * @since 3.0.0
     */
    private String nickname = "Nocturne";

    /**
     * <p>Init a ts3 configuration and add
     * default listeners/handlers to it. Everything
     * can be customized later.</p>
     * @param ip        ip address
     * @param port      port
     * @param username  query username
     * @param password  query password
     * @since 3.0.0
     */
    public Connection (String ip, int port, String username, String password, int channel) {
        ts3Config = new TS3Config();

        // configure connection
        ts3Config.setHost(ip);
        ts3Config.setDebugLevel(Level.OFF);
        ts3Config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
        ts3Config.setConnectionHandler(new com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler() {
            @Override
            public void onConnect(TS3Query ts3Query) {
                ts3Api = ts3Query.getApi();
                handleConnect(ts3Query, port, username, password, channel);
                init();
            }

            @Override
            public void onDisconnect(TS3Query ts3Query) {
                ts3Api = ts3Query.getApi();
                handleDisconnect(ts3Query);
            }
        });

        // init query and api to connect
        ts3Query = new TS3Query(ts3Config);
        ts3Api = ts3Query.getApi();
    }

    /**
     * <p>Connect to the server.</p>
     * @since 3.0.0
     */
    public void connect () {
        ts3Query.connect();
    }

    /**
     * <p>Disconnect from the server.</p>
     * @since 3.0.0
     */
    public void disconnect () {
        ts3Query.exit();
    }

    /**
     * <p>Add functions to the ts3Api. You can listen to events like
     * onClientJoin or onTextMessage and react on them.</p>
     * @param type      Event you want to register
     * @param adapter   Function that will be called
     * @since 3.0.0
     */
    public void addEventListener (TS3EventType type, TS3EventAdapter adapter) {
        ts3Api.registerEvent(type);
        ts3Api.addTS3Listeners(adapter);
    }

    /**
     * <p>Callback function that is called, when api
     * is ready to add event listeners. You are supposed to
     * overwrite it.</p>
     * @since 3.0.0
     */
    protected void init () {

    }

    /**
     * <p>Default handling of connection. It can be overwritten
     * if needed.</p>
     * @param ts3Query  query
     * @param port      port
     * @param username  query username
     * @param password  query password
     * @since 3.0.0
     */
    private void handleConnect (TS3Query ts3Query, int port, String username, String password, int channel) {
        ts3Api.selectVirtualServerByPort(port);
        ts3Api.login(username, password);
        ts3Api.setNickname(nickname);
        ts3Api.sendChannelMessage(channel, "Online");
        System.out.println("[" + new Date().toString() + "][tlu] Bot connected to the server.");
    }

    /**
     * <p>Default handling of disconnect. It can be overwritten
     * if needed.</p>
     * @param ts3Query  query
     * @since 3.0.0
     */
    private void handleDisconnect (TS3Query ts3Query) {
        System.out.println("[" + new Date().toString() + "][tlu] Bot disconnected from the server.");
    }

    /**
     * <p>Get ts3Config.</p>
     * @return  {@code ts3Config}
     * @since 3.0.0
     */
    public TS3Config getTs3Config () {
        return ts3Config;
    }

    /**
     * <p>Get ts3Query.</p>
     * @return  {@code ts3Query}
     * @since 3.0.0
     */
    public TS3Query getTs3Query () {
        return ts3Query;
    }

    /**
     * <p>Get ts3Api.</p>
     * @return  {@code ts3Api}
     * @since 3.0.0
     */
    public TS3Api getTs3Api () {
        return ts3Api;
    }

    /**
     * <p>Get nickname.</p>
     * @return  {@code nickname}
     * @since 3.0.0
     */
    public String getNickname () {
        return nickname;
    }

    /**
     * <p>Sets the nickname.</p>
     * @param nickname  nickname on server
     * @since 3.0.0
     */
    public void setNickname (String nickname) {
        this.nickname = nickname;
    }
}