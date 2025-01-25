package save;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveManager {
    private static final String SAVE_FILE_PREFIX = "save";
    private static final String SAVE_FILE_EXTENSION = ".txt"; // change to .txt for clarity

    // No longer using directories, just point to src folder
    public static Path getSaveFilePath(int slotNumber) {
        return Paths.get("src", SAVE_FILE_PREFIX + slotNumber + SAVE_FILE_EXTENSION);
    }

    // No longer needed to ensure directories exist, but if you want a check:
    public static void ensureSaveDirectoryExists() throws IOException {
        // If you really want to ensure src exists (it should):
        Path srcDir = Paths.get("src");
        if (!Files.exists(srcDir)) {
            // Not typically expected, but you can create it if you want
            Files.createDirectory(srcDir);
        }
    }

    public static boolean isSlotOccupied(int slotNumber) {
        Path saveFile = getSaveFilePath(slotNumber);
        return Files.exists(saveFile);
    }

    public static void deleteSaveSlot(int slotNumber) throws IOException {
        Path saveFile = getSaveFilePath(slotNumber);
        if (Files.exists(saveFile)) {
            Files.delete(saveFile);
        }
    }
}
