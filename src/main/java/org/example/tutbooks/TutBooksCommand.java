package org.example.tutbooks;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.example.tutbooks.io.BookRegistry;

import javax.annotation.Nonnull;
import java.util.List;

public class TutBooksCommand extends CommandBase {
    private final BookRegistry registry;

    public TutBooksCommand(BookRegistry registry) {
        super("tutbooks", "Lists tutorial books loaded by TuTBooks.");
        this.setPermissionGroup(GameMode.Adventure);
        this.registry = registry;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        List<String> ids = registry.getIdsSorted();
        if (ids.isEmpty()) {
            ctx.sendMessage(Message.raw("TuTBooks: no books loaded."));
            return;
        }
        ctx.sendMessage(Message.raw("TuTBooks books: " + String.join(", ", ids)));
    }
}
