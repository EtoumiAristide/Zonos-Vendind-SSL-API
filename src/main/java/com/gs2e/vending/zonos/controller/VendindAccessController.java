package com.gs2e.vending.zonos.controller;

import com.gs2e.vending.zonos.model.RequestDTO;
import com.gs2e.vending.zonos.model.ResponseError;
import com.gs2e.vending.zonos.utils.Utilitaires;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Controller
@RequestMapping(path = "/api/zonos-vending")
@CrossOrigin(origins = "*")
public class VendindAccessController {

    private String user;
    private String userid;
    private String pincode;
    private String serialno;
    private String password;
    private String vtype;
    private String vcode;
    private String deviceno;

    private final Logger logger = LoggerFactory.getLogger(VendindAccessController.class);

    @Autowired
    private Environment environment;

    @PostMapping(produces = MediaType.TEXT_XML_VALUE)
    public ResponseEntity<Object> getMeterData(@RequestBody RequestDTO requestDTO) {

        //Initialisation de la reponse en cas d"erreur
        ResponseError responseError = new ResponseError();
        boolean isOk = true;

        //Initialisation en cas de succès
        String responseApi = "";

        //Initialisation des paramètres client
        if (requestDTO.getUnivers().equalsIgnoreCase("cie")) {
            user = environment.getProperty("cie.user");
            userid = environment.getProperty("cie.userid");
            pincode = environment.getProperty("cie.pincode");
            serialno = environment.getProperty("cie.serialno");
            password = environment.getProperty("cie.password");
            vtype = environment.getProperty("cie.vtype");
            vcode = environment.getProperty("cie.vcode");
            deviceno = environment.getProperty("cie.deviceno");
        } else {
            user = environment.getProperty("sodeci.user");
            userid = environment.getProperty("sodeci.userid");
            pincode = environment.getProperty("sodeci.pincode");
            serialno = environment.getProperty("sodeci.serialno");
            password = environment.getProperty("sodeci.password");
            vtype = environment.getProperty("sodeci.vtype");
            vcode = environment.getProperty("sodeci.vcode");
            deviceno = environment.getProperty("sodeci.deviceno");
        }

        // Replace with your actual certificate and keystore
        String certificateFile = "myCertificate.crt";
        String keystoreFile = "myKeystore.jks";
        String keystorePassword = "myPassword";

        // Create a trust manager that accepts all certificates
        X509TrustManager trustAllManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        // Create an SSL context with the trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {

            setErrorMessage(responseError, e);
            isOk = false;

            //throw new RuntimeException(e);
        }
        try {
            sslContext.init(null, new X509TrustManager[]{trustAllManager}, null);
        } catch (KeyManagementException e) {

            setErrorMessage(responseError, e);
            isOk = false;

            //throw new RuntimeException(e);
        }

        // Get the SSL socket factory
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        // Create an SSL socket
        Socket socket = null;
        try {
            socket = sslSocketFactory.createSocket(InetAddress.getByName(environment.getProperty("vending.address")), Integer.parseInt(Objects.requireNonNull(environment.getProperty("vending.port"))));
        } catch (IOException e) {

            setErrorMessage(responseError, e);
            isOk = false;

            //throw new RuntimeException(e);
        }

        //Constitute Data
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Send the request message length
        String xmlDocument = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<iceevdreq version=\"1.0\" agent=\"ssl\">\n" +
                "    <date>" + dateFormatter.format(now) + "</date>\n" +
                "    <time>" + hourFormatter.format(now) + "</time>\n" +
                "    <request>\n" +
                "        <user>" + user + "</user>\n" +
                "        <type>1</type>\n" +
                "        <refno>" + Utilitaires.calculateTimeMillis(now) + "</refno>\n" +
                "    </request>\n" +
                "    <evddetails>\n" +
                "        <serialno>" + serialno + "</serialno>\n" +
                "        <password>" + requestDTO.getUserPass() + "</password>\n" +
                "        <deviceno>" + deviceno + "</deviceno>\n" +
                "        <format maxlen=\"32\">2</format>\n" +
                "        <userid>" + requestDTO.getUserName() + "</userid>\n" +
                "        <pincode>" + pincode + "</pincode>\n" +
                "        <voucherdetails>\n" +
                "            <vtype>" + vtype + "</vtype>\n" +
                "            <vcode>" + vcode + "</vcode>\n" +
                "            <meterno type=\"number\">" + requestDTO.getNumCompteur() + "</meterno>\n" +
                "            <amount>" + requestDTO.getMontant() + "</amount>\n" +
                "        </voucherdetails>\n" +
                "    </evddetails>\n" +
                "</iceevdreq>";

        //System.out.println("Request: " + xmlDocument);

        int length = xmlDocument.length();
        String lengthString = String.format("%06d", length);

        try {
            socket.getOutputStream().write(lengthString.getBytes());

            // Send the XML request document
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.write(xmlDocument.getBytes());
            outputStream.flush();

            // Read the server response
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                //System.out.println("Response API: " + line);

                //Recherche de la position du 1er caratère "<" trouvé
                int pos = line.indexOf("<");

                responseApi = responseApi.concat(line.substring(pos));

            }

            // Close the socket
            socket.close();
        } catch (IOException e) {
            setErrorMessage(responseError, e);
            isOk = false;
            //throw new RuntimeException(e);
        }

        if (isOk) return ResponseEntity.ok().body(responseApi);
        else return ResponseEntity.internalServerError().body(responseError);

    }

    private void setErrorMessage(ResponseError responseError, Exception e) {
        responseError.setCause(e);
        responseError.setCode(500);
        responseError.setMessage("Internal Server Error");
        responseError.setException(e.getMessage());
    }
}
