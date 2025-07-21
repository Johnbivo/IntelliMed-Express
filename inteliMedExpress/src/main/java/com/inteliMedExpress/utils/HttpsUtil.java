package com.inteliMedExpress.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Utility class for handling SSL certificates and HTTPS security setup
 */
public class HttpsUtil {
    private static final String CLASS_NAME = HttpsUtil.class.getSimpleName();
    private static boolean sslInitialized = false;

    /**
     * Initialize SSL context with the server certificate
     * This method should be called once at application startup
     */
    public static void setupSSL() {
        if (sslInitialized) {
            return; // Only initialize once
        }

        try {
            // Load the certificate from the resources
            InputStream certStream = HttpsUtil.class.getResourceAsStream("/com/inteliMedExpress/resources/certs/server_certificate.cer");

            if (certStream == null) {
                AppLogger.error(CLASS_NAME, "Certificate file not found!");
                throw new IOException("Certificate file not found!");
            }

            // Create a certificate factory
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(certStream);
            certStream.close();

            // Create a KeyStore containing our trusted certificate
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("server", cert);

            // Create a TrustManager that trusts our certificate
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            // Create an SSLContext with our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            // Set this as the default SSL socket factory
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // Disabling hostname verification, doesn't work otherwise, at least in testing
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            sslInitialized = true;
            AppLogger.info(CLASS_NAME, "SSL setup complete with specific certificate and hostname verification disabled");
        } catch (Exception e) {
            AppLogger.error(CLASS_NAME, "Error setting up SSL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Check if SSL has been initialized
     * @return true if SSL is initialized, false otherwise
     */
    public static boolean isSSLInitialized() {
        return sslInitialized;
    }
}
