package utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Xác định đúng thư mục data khi chạy bằng run.bat hoặc trực tiếp từ IDE. */
public final class ProjectPaths {
    private ProjectPaths() {
    }

    public static Path projectDirectory() {
        Path current=Paths.get("").toAbsolutePath().normalize();
        if(Files.isDirectory(current.resolve("src"))&&Files.isDirectory(current.resolve("data")))return current;
        Path nested=current.resolve("SalesManagement");
        if(Files.isDirectory(nested.resolve("src"))&&Files.isDirectory(nested.resolve("data")))return nested;
        return current;
    }

    public static Path dataDirectory() {
        return projectDirectory().resolve("data");
    }
}
