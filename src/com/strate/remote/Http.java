package com.strate.remote;

import com.strate.constants.Ansi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <p>Used to perform a http/https request to a remote server. Just
 * create a new {@link Http} object with url as parameter and use
 * {@see getResponse} to get the response string. To check if request
 * was successful you can use {@see isSuccessful} or {@see isFailed}.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Http {

    /**
     * <p>Defines a http/https status code to identify if
     * a request was successful and especially see what went wrong. It is
     * a part of {@link Http}</p>
     * @author Stephan Strate
     * @since 3.0.0
     */
    public enum STATUS_CODE {

        SUCCESS(200),
        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        FORBIDDEN(403), // eg. no valid api key
        DATA_NOT_FOUND(404),
        METHOD_NOT_ALLOWED(405),
        UNSUPPORTED_MEDIA_TYPE(415),
        RATE_LIMIT_EXCEEDED(429),
        INTERNAL_SERVER_ERROR(500),
        BAD_GATEWAY(502),
        SERVICE_UNAVAILABLE(503),
        GATEWAY_TIMEOUT(504);

        private final int code;

        /**
         * <p>Default constructor.</p>
         * @param code  http/https status code
         * @since 3.0.0
         */
        STATUS_CODE (int code) {
            this.code = code;
        }

        /**
         * <p>Get a status code with a given integer. Returns null,
         * when passed integer could not be found.</p>
         * @param code  status code
         * @return  status code object
         * @since 3.0.0
         */
        static STATUS_CODE getStatusCodeByInt (int code) {
            for (STATUS_CODE status_code : STATUS_CODE.values()) {
                if (status_code.getCode() == code) {
                    return status_code;
                }
            }

            return null;
        }

        /**
         * <p>Get the requested http/https status code. Public access
         * is given, so you can check the status code in other classes.</p>
         * @return  http/https status code
         * @since 3.0.0
         */
        public int getCode () {
            return code;
        }
    }

    /**
     * <p>Represents the current request state of a
     * {@link Http} object.</p>
     * @author Stephan Strate
     * @since 3.0.0
     */
    enum State {
        Default, // default state
        Pending,
        Success,
        Failed
    }

    private static final String USER_AGENT = "Mozilla/5.0";

    // default state
    private volatile State state = State.Default;

    // pending status code
    private STATUS_CODE status_code = null;

    private URL url;
    private HttpURLConnection con;
    private String response;

    /**
     * <p>Perform a synchronized http/https request to a remote
     * url. To get the request use {@see getResponse}.</p>
     * @param url   request url
     * @since 3.0.0
     */
    public Http (String url) {
        init(url);
    }

    /**
     * <p>Opens a connection to the request url. Method is called
     * by default constructor.</p>
     * @param url   request url
     * @since 3.0.0
     */
    private void init (String url) {
        try {
            // create executable url
            this.url = new URL(url);

            start();
        } catch (MalformedURLException e) {
            setState(State.Failed);
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Can not generate request of " + url + ".");
        }
    }

    /**
     * <p>Opens a synchronized connection to request url and fetches the result. To get the
     * result you can use {@see getResponse}.</p>
     * @since 3.0.0
     */
    private synchronized void start () {
        setState(State.Pending);
        try {
            // open connection
            con = (HttpURLConnection) this.url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            if (STATUS_CODE.getStatusCodeByInt(con.getResponseCode()) == STATUS_CODE.SUCCESS) {
                setStatusCode(STATUS_CODE.SUCCESS);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader((con.getInputStream())));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                this.response = response.toString();
                setState(State.Success);
            } else {
                setState(State.Failed);
                setStatusCode(STATUS_CODE.getStatusCodeByInt(con.getResponseCode()));
            }
        } catch (IOException e) {
            setState(State.Failed);
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Can not open connection to " + url.toString() + ".");
        }
    }

    /**
     * <p>Get the response string (can be parsed to json for example) or
     * {@code null} when request failed/is in progress.</p>
     * @return  response string
     * @since 3.0.0
     */
    public String getResponse () {
        if (getState() == State.Success) {
            return response;
        } else {
            return null;
        }
    }

    /**
     * <p>Returns {@code true} when request failed.</p>
     * @return  did request fail
     * @since 3.0.0
     */
    public boolean isFailed () {
        return state == State.Failed;
    }

    /**
     * <p>Returns {@code true} when request was successful.</p>
     * @return  did request was successful
     * @since 3.0.0
     */
    public boolean isSuccessful () {
        return state == State.Success;
    }

    /**
     * <p>Returns the {@link State} of request.</p>
     * @return  status of request
     * @since 3.0.0
     */
    public State getState () {
        return state;
    }

    /**
     * <p>Returns the {@link STATUS_CODE} of request.</p>
     * @return  status code of request
     * @since 3.0.0
     */
    public STATUS_CODE getStatusCode () {
        return status_code;
    }

    /**
     * <p>Returns the {@link URL} of request.</p>
     * @return  URL object of request
     * @since 3.0.0
     */
    public URL getUrl () {
        return url;
    }

    /**
     * <p>Returns the {@link HttpURLConnection} of request.</p>
     * @return  Connection of request
     * @since 3.0.0
     */
    public HttpURLConnection getCon () {
        return con;
    }

    /**
     * <p>Set the {@link State} of the current request.</p>
     * @param state current {@link State}
     * @since 3.0.0
     */
    private void setState (State state) {
        this.state = state;
    }

    /**
     * <p>Set the {@link STATUS_CODE} of the current request.</p>
     * @param statusCode    current {@link STATUS_CODE}
     * @since 3.0.0
     */
    private void setStatusCode (STATUS_CODE statusCode) {
        this.status_code = statusCode;
    }
}