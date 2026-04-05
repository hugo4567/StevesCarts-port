import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DownloadManifest {
    public static void main(String[] args) throws Exception {
        String url = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json";
        String cacheDir = System.getProperty("user.home") + "\\.gradle\\caches\\fabric-loom";
        Path cachePath = Paths.get(cacheDir);
        Path manifestPath = cachePath.resolve("version_manifest.json");
        
        // Créer le dossier si nécessaire
        Files.createDirectories(cachePath);
        
        System.out.println("Téléchargement du manifest Minecraft...");
        System.out.println("URL: " + url);
        System.out.println("Destination: " + manifestPath);
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            Files.writeString(manifestPath, response.body());
            System.out.println("✓ Téléchargé: " + response.body().length() + " bytes");
            System.out.println("✓ Fichier créé: " + manifestPath);
            
            // Rendre le fichier en lecture seule pour empêcher Loom de le supprimer
            manifestPath.toFile().setReadOnly();
            System.out.println("✓ Fichier protégé en lecture seule");
        } else {
            System.out.println("✗ Erreur HTTP: " + response.statusCode());
        }
    }
}
