/*
 * This class has the objective of using HttpURLConnection
 * to make GET requests in an api / site that provides this
 * resource, expect a text response. Also the implementation
 * of POST methods for sending FILE or JSON files.
 */

package requirements;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Student information systems Brazil, MG
 *
 * @author Eduardo Almeida <edualmeida.a7@gmail.com>
 * @version 1.0
 * @since April 07, 2020
 */
public class Requisition {
    /*
    it depends on the type of response expected, variables and
    methods must be adapted, in this case I expect a json or text.
     */
    private URL fishIn;
    private URL sendTo;
    private File file;
    private String responseGet;
    private String responsePost;
    private int codResponseGet;
    private int codResponseSend;

    /**
     * Empty Constructor
     */
    public Requisition() {
    }

    /**
     * Constructor with GET link
     *
     * @param fishIn String with valid link for GET request
     * @throws MalformedURLException Invalid link/api
     */
    public Requisition(String fishIn) throws MalformedURLException {
        this.fishIn = null;
        this.setFishIn(fishIn);
        if (this.fishIn == null) throw new MalformedURLException("Bad URL");
    }

    /**
     * Constructor with POST link and File to send
     *
     * @param sendTo String with valid link for POST request
     * @param file   File Type with uploaded content
     * @throws MalformedURLException Invalid link/api
     */
    public Requisition(String sendTo, File file) throws MalformedURLException {
        this.sendTo = null;
        this.setSendTo(sendTo);
        if (this.sendTo == null) throw new MalformedURLException("Bad URL");
        this.file = file;
    }

    /**
     * Constructor with GET link, POST link and File to send
     *
     * @param fishIn String with valid link for GET request
     * @param sendTo String with valid link for POST request
     * @param file   File Type with uploaded content
     * @throws MalformedURLException
     */
    public Requisition(String fishIn, String sendTo, File file) throws MalformedURLException {
        this.sendTo = null;
        this.fishIn = null;
        this.setFishIn(fishIn);
        this.setSendTo(sendTo);
        if (this.sendTo == null) throw new MalformedURLException("Bad URL POST");
        if (this.fishIn == null) throw new MalformedURLException("Bad URL GET");
        this.file = file;
    }

    /**
     * Make the GET request on top of the informed link/api,
     * saving the answer in the variable response of the class
     *
     * @return true if you can fetch, false if connection fails
     */
    public boolean fish_In() {
        /*
        depending you will have to modify the type of file received and the information for it
         */
        try {
            HttpURLConnection connexion = (HttpURLConnection) this.fishIn.openConnection();
            connexion.setRequestMethod("GET");
            connexion.setRequestProperty("User-Agent", "Mozilla/5.0");
            System.out.println("Sending 'GET' request to: \n" + this.fishIn);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = bfr.readLine()) != null) {
                response.append(inputLine);
            }
            this.responseGet = response.toString();
            this.codResponseGet = connexion.getResponseCode();
            bfr.close();
            connexion.disconnect();
            return true;
        } catch (IOException ioe) {
            ioe.getMessage();
        }
        return false;
    }

    /**
     * Make the POST request over the informed LINK/API and FILE,
     * saving the response code in the variable codResponse of the class
     *
     * @return true if you can send the multipart post false if you can't
     */
    public boolean send_To_Multipart() {
        /*
        depending you will have to modify the type of file to be sent and the information for it
        */
        try {
            HttpURLConnection connexion = (HttpURLConnection) sendTo.openConnection();
            connexion.setDoOutput(true);
            connexion.setRequestMethod("POST");
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
            multipartEntity.addPart(file.getName(), new FileBody(this.file));
            connexion.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + UUID.randomUUID().toString());
            multipartEntity.writeTo(connexion.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(connexion.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            this.responsePost = response.toString();
            this.codResponseSend = connexion.getResponseCode();
            this.responseCodeSituation();
            connexion.disconnect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            this.codResponseSend = 0;
            return false;
        }
    }

    /**
     * See reply GET
     */
    public void seeResponseGet() {
        System.out.println(this.responseGet);
    }

    /**
     * See reply POST
     */
    public void seeResponsePost() {
        System.out.println(this.responsePost);
    }

    /**
     * Checks the status of the POST response
     */
    public void responseCodeSituation() {
        if (this.codResponseSend == 0) {
            System.out.println("STILL NOT POSTED");
        } else if (this.codResponseSend >= 200 && this.codResponseSend <= 206) {
            System.err.println("Success sending");
        } else if (this.codResponseSend >= 207 && this.codResponseSend <= 299) {
            System.err.println("Multi-status sending");
        } else if (this.codResponseSend >= 400 && this.codResponseSend <= 499) {
            System.err.println("Customer error");
        } else if (this.codResponseSend >= 500 && this.codResponseSend <= 599) {
            System.err.println("Server error");
        } else {
            System.out.println("Response information");
        }
    }

    /**
     * @return a URL type
     */
    public URL getFishIn() {
        return this.fishIn;
    }

    /**
     * This checks the validity of the link / api before assigning
     *
     * @param fishIn String Link/api to GET
     */
    public void setFishIn(String fishIn) {
        try {
            URL url = new URL(fishIn);
            this.fishIn = url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return a URL type
     */
    public URL getSendTo() {
        return this.sendTo;
    }

    /**
     * This checks the validity of the link / api before assigning
     *
     * @param sendTo String Link/api to POST
     */
    public void setSendTo(String sendTo) {
        try {
            URL url = new URL(sendTo);
            this.sendTo = url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return The reply of the get request
     */
    public String getResponseGet() {
        return this.responseGet;
    }

    /**
     * @return Assigned FILE
     */
    public File getFile() {
        return this.file;
    }

    /**
     * @param file archives to POST
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return request return code
     */
    public int getCodResponseSend() {
        return this.codResponseSend;
    }

    /**
     * @return reply to GET request
     */
    public int getCodResponseGet() {
        return this.codResponseGet;
    }

    /**
     * @return reply to POST request
     */
    public String getResponsePost() {
        return this.responsePost;
    }
}
