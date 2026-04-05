import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.security.cert.Certificate;

public class SSLTest {
    public static void main(String[] args) {
        System.out.println("=== Test SSL avec java.net.http.HttpClient (comme Fabric Loom) ===\n");
        
        String url = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json";
        
        // Test 1: Client HTTP standard (comme Loom l'utilise)
        System.out.println("1. Test avec HttpClient standard (STRICT SSL):");
        testWithStandardClient(url);
        
        // Test 2: Client HTTP avec SSL debugging
        System.out.println("\n2. Test avec SSL debugging activé:");
        System.setProperty("javax.net.debug", "ssl,handshake");
        testWithStandardClient(url);
        
        // Test 3: Afficher les détails du certificat
        System.out.println("\n3. Analyse du certificat SSL:");
        analyzeCertificate(url);
        
        // Test 4: Client qui accepte tout (pour voir si ça passe)
        System.out.println("\n4. Test avec acceptation de tous les certificats:");
        testWithTrustAll(url);
    }
    
    static void testWithStandardClient(String url) {
        try {
            HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("  ✓ SUCCÈS - Status: " + response.statusCode());
            System.out.println("  Taille réponse: " + response.body().length() + " bytes");
        } catch (Exception e) {
            System.out.println("  ✗ ÉCHEC: " + e.getClass().getName());
            System.out.println("  Message: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("  Cause: " + e.getCause().getMessage());
                if (e.getCause().getCause() != null) {
                    System.out.println("  Cause racine: " + e.getCause().getCause().getMessage());
                }
            }
        }
    }
    
    static void analyzeCertificate(String url) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            
            // Custom TrustManager pour capturer le certificat
            X509Certificate[] serverCerts = new X509Certificate[1];
            TrustManager[] trustManagers = new TrustManager[] {
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        serverCerts[0] = chain[0];
                        System.out.println("  Certificat reçu:");
                        System.out.println("    Sujet: " + chain[0].getSubjectDN());
                        System.out.println("    Émetteur: " + chain[0].getIssuerDN());
                        System.out.println("    SAN (Subject Alt Names):");
                        try {
                            var san = chain[0].getSubjectAlternativeNames();
                            if (san != null) {
                                san.forEach(name -> System.out.println("      " + name));
                            } else {
                                System.out.println("      (aucun)");
                            }
                        } catch (Exception e) {
                            System.out.println("      Erreur lecture SAN: " + e.getMessage());
                        }
                        
                        // Validation normale (va échouer si mauvais certificat)
                        try {
                            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                            tmf.init((java.security.KeyStore) null);
                            X509TrustManager defaultTm = (X509TrustManager) tmf.getTrustManagers()[0];
                            defaultTm.checkServerTrusted(chain, authType);
                            System.out.println("    ✓ Certificat VALIDE selon Java");
                        } catch (Exception e) {
                            System.out.println("    ✗ Certificat INVALIDE: " + e.getMessage());
                            System.out.println("    [!] UN PROGRAMME INTERCEPTE LA CONNEXION!");
                        }
                    }
                    
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
            };
            
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            
            HttpClient client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
            
            client.send(request, HttpResponse.BodyHandlers.ofString());
            
        } catch (Exception e) {
            System.out.println("  Erreur analyse: " + e.getMessage());
        }
    }
    
    static void testWithTrustAll(String url) {
        try {
            // Créer un SSL context qui accepte TOUT
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                }
            };
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            
            HttpClient client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("  ✓ SUCCÈS avec trust-all - Status: " + response.statusCode());
            System.out.println("  Cela confirme que le problème est SSL/certificat");
        } catch (Exception e) {
            System.out.println("  ✗ ÉCHEC même avec trust-all: " + e.getMessage());
            System.out.println("  Le problème n'est PAS SSL!");
        }
    }
}
