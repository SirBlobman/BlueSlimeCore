package com.github.sirblobman.api.core.command.sirblobmancore;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.Language;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.api.language.SimpleReplacer;

import org.jetbrains.annotations.NotNull;

public final class SubCommandLanguageTest extends Command {
    private final CorePlugin plugin;

    public SubCommandLanguageTest(CorePlugin plugin) {
        super(plugin, "language-test");
        setPermissionName("sirblobman.core.command.sirblobmancore.language-test");
        this.plugin = plugin;
    }

    @NotNull
    @Override
    protected LanguageManager getLanguageManager() {
        CorePlugin corePlugin = getCorePlugin();
        return corePlugin.getLanguageManager();
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Set<String> valueSet = getOnlinePlayerNames();
            return getMatching(args[0], valueSet);
        }

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        CommandSender target = sender;
        if (args.length > 0) {
            String targetName = args[0];
            target = findTarget(sender, targetName);
            if (target == null) {
                Replacer replacer = new SimpleReplacer("{target}", targetName);
                sendMessage(sender, "error.invalid-target", replacer);
                return true;
            }
        }

        LanguageManager languageManager = getLanguageManager();
        Language language = languageManager.getLanguage(target);
        if (language == null) {
            languageManager.sendMessage(sender, "language-test.invalid-language", null);
            return true;
        }

        String languageCode = language.getLanguageCode();
        Replacer languageCodeReplacer = new SimpleReplacer("{language_code}", languageCode);
        languageManager.sendMessage(target, "language-test.language-code", languageCodeReplacer);

        Optional<Locale> optionalJavaLocale = language.getJavaLocale();
        if (optionalJavaLocale.isPresent()) {
            Locale javaLocale = optionalJavaLocale.get();
            String javaLocaleTag = javaLocale.toLanguageTag();
            Replacer javaLocaleReplacer = new SimpleReplacer("{java_locale}", javaLocaleTag);
            languageManager.sendMessage(target, "language-test.java-locale", javaLocaleReplacer);
        }

        if (target instanceof Player) {
            Player targetPlayer = (Player) target;
            languageManager.sendActionBar(targetPlayer, "language-test.action-bar", null);
            languageManager.sendSound(targetPlayer, "language-test.sound");
            languageManager.sendTitle(targetPlayer, "language-test.title", null);
        }

        languageManager.sendMessage(target, "language-test.message", null);
        languageManager.broadcastMessage("language-test.broadcast", null, null);
        sendMessage(sender, "language-test.complete", null);
        return true;
    }

    private CorePlugin getCorePlugin() {
        return this.plugin;
    }
}
