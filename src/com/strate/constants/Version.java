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
 * <p>[description]</p>
 * @author Stephan Strate
 * @since 2.0.0
 */
public class Version {

    private static final String baseUrl = "https://api.harddestiny.de/v1/bot/version/";

    private boolean valid = false;

    private String version;
    private long build;
    private String download;

    /**
     * <p>[description]</p>
     * @param version
     * @param build
     * @param download
     */
    public Version (String version, int build, String download) {
        this.version = version;
        this.build = build;
        this.download = download;
        this.valid = true;
    }

    /**
     * <p>[description]</p>
     */
    public Version () {
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Searching for new version.");
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
                System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RED + "Can not connect to our servers. " + Ansi.RESET + "You can try again later.");
                valid = false;
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RED + "Code 500: Internal update error, please contact dev@harddestiny.de." + Ansi.RESET);
        } catch (ParseException e) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RED + "Code 402: Wrong update content, please contact dev@harddestiny.de." + Ansi.RESET);
        }
    }

    /**
     * <p>[description]</p>
     * @param old
     * @throws IOException
     */
    public void update (Version old) throws IOException {
        if (this.isValid()) {
            if (this.isNewerThan(old)) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                boolean update = false;
                boolean touched = false;
                do {
                    System.out.print(Ansi.BLUE + "[tlu] " + Ansi.RESET + "New version available. Do you want to update (" + Ansi.RED + "recommended" + Ansi.RESET + ")? (Y/n) ");
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
                    System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Starting update from " + old.getVersion() + " to " + this.getVersion() + ".");
                    download();
                }
            } else {
                System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "No new update available.");
            }
        }
    }

    /**
     * <p>[description]</p>
     */
    private void download () {
        try {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Reading download path.");
            URL download = new URL(this.download);
            ReadableByteChannel readableByteChannel = Channels.newChannel(download.openStream());

            File path = new File(Init.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String decodePath = path.getName();

            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Starting download.");
            FileOutputStream fileOutputStream = new FileOutputStream(decodePath + ".jar");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Update finished. Please restart.");
            System.exit(0);
        } catch (MalformedURLException e) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RED + "Server error. " + Ansi.RESET + "Wrong download link.");
        } catch (IOException e) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RED + "Client error. " + Ansi.RESET + "Can not download update.");
        }
    }

    /**
     * <p>[description]</p>
     * @param other
     * @return
     */
    public boolean isNewerThan (Version other) {
        return this.getBuild() > other.getBuild();
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public String getBaseUrl () {
        return baseUrl;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public boolean isValid () {
        return valid;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public String getVersion () {
        return version;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public long getBuild () {
        return build;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public String getDownload () {
        return download;
    }

    /**
     * <p>[description]</p>
     * @param version
     */
    public void setVersion (String version) {
        this.version = version;
    }

    /**
     * <p>[description]</p>
     * @param build
     */
    public void setBuild (long build) {
        this.build = build;
    }

    /**
     * <p>[description]</p>
     * @param download
     */
    public void setDownload (String download) {
        this.download = download;
    }
}