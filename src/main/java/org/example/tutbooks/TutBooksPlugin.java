package org.example.tutbooks;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.example.tutbooks.io.BookRegistry;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TutBooksPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private final BookRegistry registry = new BookRegistry();

    public TutBooksPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Starting " + this.getName() + " v" + this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        try {
            registry.loadFromResources(getClass().getClassLoader(), "tutbooks/index.json");
            LOGGER.atInfo().log("Loaded " + registry.size() + " tutorial book(s).");
            for (Map.Entry<String, List<String>> entry : registry.getErrors().entrySet()) {
                for (String error : entry.getValue()) {
                    LOGGER.atInfo().log("Book warning [" + entry.getKey() + "]: " + error);
                }
            }
        } catch (IOException ex) {
            LOGGER.atInfo().log("Failed to load tutorial books: " + ex.getMessage());
        }

        this.getCommandRegistry().registerCommand(new TutBooksCommand(registry));
    }
}
