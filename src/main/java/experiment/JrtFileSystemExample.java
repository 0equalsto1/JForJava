package experiment;

import java.net.URI;
import java.nio.file.*;

public class JrtFileSystemExample {
    public static void main(String[] args) {
        FileSystem jrtFileSystem = FileSystems.getFileSystem(URI.create("jrt:/"));
        Path modulesPath = jrtFileSystem.getPath("/modules");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(modulesPath)) {
            for (Path entry : stream) {
                System.out.println(entry.getFileName());
            }
        } catch (Exception _) {
        }
    }
}
