package com.strate.constants;

import com.strate.Init;
import com.strate.remote.Http;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * <p>Represents a version of this bot and
 * can update itself.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Version {

    private static final String baseUrl = "https://api.harddestiny.de/v1/bot/version/";

    private boolean valid = false;

    private String version;
    private long build;
    private String download;

    /**
     * <p>Represents a version of this application,
     * contains version, build number and download link.</p>
     * @param version   version
     * @param build     build number
     * @param download  download link
     * @since 3.0.0
     */
    public Version (String version, int build, String download) {
        this.version = version;
        this.build = build;
        this.download = download;
        this.valid = true;
    }

    /**
     * <p>Search for a new version using remote
     * api of 'api.harddestiny.de'.</p>
     * @since 3.0.0
     */
    public Version () {
        System.out.println("[tlu] Searching for new version.");
        try {
            Http http = new Http(baseUrl + URLEncoder.encode("latest", "UTF-8"));

            if (http.isSuccessful()) {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(http.getResponse());

                version = (String) jsonObject.get("version");
                build = (long) jsonObject.get("build");
                download = (String) jsonObject.get("download");

                valid = true;
            } else {
                System.out.println("[tlu] Can not connect to our servers. You can try again later.");
                valid = false;
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println("[tlu] Code 500: Internal update error, please contact dev@harddestiny.de.");
        } catch (ParseException e) {
            System.out.println("[tlu] Code 402: Wrong update content, please contact dev@harddestiny.de.");
        }
    }

    /**
     * <p>Check and update from another version.</p>
     * @param old   {@link Version}
     * @since 3.0.0
     */
    public void update (Version old) {
        try {
            if (this.isValid()) {
                if (this.isNewerThan(old)) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                    boolean update = false;
                    boolean touched = false;
                    do {
                        System.out.print("[tlu] New version available. Do you want to update (recommended)? (Y/n) ");
                        String temp = br.readLine();
                        if (temp.toLowerCase().equals("y")) {
                            touched = true;
                            update = true;
                        } else if (temp.toLowerCase().equals("n")) {
                            touched = true;
                            update = false;
                        }
                    } while (!touched);

                    if (update) {
                        System.out.println("[tlu] Starting update from " + old.getVersion() + " to " + this.getVersion() + ".");
                        download();
                    }
                } else {
                    System.out.println("[tlu] No new update available.");
                }
            }
        } catch (IOException e) {
            System.out.println("[tlu] Error while updating. Try again later.");
        }
    }

    /**
     * <p>Download the version and exit the application.</p>
     * @since 3.0.0
     */
    private void download () {
        try {
            System.out.println("[tlu] Reading download path.");
            URL download = new URL(this.download);
            ReadableByteChannel readableByteChannel = Channels.newChannel(download.openStream());

            File path = new File(Init.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String decodePath = path.getName();

            System.out.println("[tlu] Starting download.");
            FileOutputStream fileOutputStream = new FileOutputStream(decodePath + ".jar");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            System.out.println("[tlu] Update finished. Please restart.");
            System.exit(0);
        } catch (MalformedURLException e) {
            System.out.println("[tlu] Server error. Wrong download link.");
        } catch (IOException e) {
            System.out.println("[tlu] Client error. Can not download update.");
        }
    }

    /**
     * <p>Returns {@code true} when the other version
     * is older than this one.</p>
     * @param other {@link Version}
     * @return      {@code true} when version is older
     * @since 3.0.0
     */
    public boolean isNewerThan (Version other) {
        return this.getBuild() > other.getBuild();
    }

    /**
     * <p>Returns the base url.</p>
     * @return  base url
     * @since 3.0.0
     */
    public String getBaseUrl () {
        return baseUrl;
    }

    /**
     * <p>Returns {@code true} when this version is
     * valid.</p>
     * @return  valid
     * @since 3.0.0
     */
    public boolean isValid () {
        return valid;
    }

    /**
     * <p>Returns the version.</p>
     * @return  version
     * @since 3.0.0
     */
    public String getVersion () {
        return version;
    }

    /**
     * <p>Returns the build number.</p>
     * @return  build number
     * @since 3.0.0
     */
    public long getBuild () {
        return build;
    }

    /**
     * <p>Returns the download link.</p>
     * @return  download link
     * @since 3.0.0
     */
    public String getDownload () {
        return download;
    }

    /**
     * <p>Sets the version.</p>
     * @param version   version
     * @since 3.0.0
     */
    public void setVersion (String version) {
        this.version = version;
    }

    /**
     * <p>Sets the build number.</p>
     * @param build     build number
     * @since 3.0.0
     */
    public void setBuild (long build) {
        this.build = build;
    }

    /**
     * <p>Sets the download link.</p>
     * @param download  download link
     * @since 3.0.0
     */
    public void setDownload (String download) {
        this.download = download;
    }
}