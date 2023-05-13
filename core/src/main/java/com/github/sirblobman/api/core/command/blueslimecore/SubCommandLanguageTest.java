package com.github.sirblobman.api.core.command.blueslimecore;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.Language;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.language.replacer.StringReplacer;

public final class SubCommandLanguageTest extends Command {
    private final CorePlugin plugin;

    public SubCommandLanguageTest(@NotNull CorePlugin plugin) {
        super(plugin, "language-test");
        setPermissionName("blue.slime.core.command.blueslimecore.language-test");
        this.plugin = plugin;
    }

    @Override
    protected @NotNull LanguageManager getLanguageManager() {
        CorePlugin plugin = getCorePlugin();
        return plugin.getLanguageManager();
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull CommandSender sender, String @NotNull [] args) {
        if (args.length == 1) {
            Set<String> valueSet = getOnlinePlayerNames();
            return getMatching(args[0], valueSet);
        }

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        CommandSender target = sender;
        if (args.length > 0) {
            String targetName = args[0];
            target = findTarget(sender, targetName);
            if (target == null) {
                return true;
            }
        }

        LanguageManager languageManager = getLanguageManager();
        Language language = languageManager.getLanguage(target);
        if (language == null) {
            languageManager.sendMessage(sender, "language-test.invalid-language");
            return true;
        }

        sendTests(target, language);
        sendMessage(sender, "language-test.complete");
        return true;
    }

    private @NotNull CorePlugin getCorePlugin() {
        return this.plugin;
    }

    private void sendTests(@NotNull CommandSender audience, @NotNull Language language) {
        LanguageManager languageManager = getLanguageManager();
        String languageCode = language.getLanguageName();
        Replacer codeReplacer = new StringReplacer("{language_code}", languageCode);
        languageManager.sendMessage(audience, "language-test.language-code", codeReplacer);

        Locale javaLocale = language.getJavaLocale();
        String javaLocaleTag = javaLocale.toLanguageTag();
        Replacer localeReplacer = new StringReplacer("{java_locale}", javaLocaleTag);
        languageManager.sendMessage(audience, "language-test.java-locale", localeReplacer);

        languageManager.sendActionBar(audience, "language-test.action-bar");
        languageManager.sendSound(audience, "language-test.sound");
        languageManager.sendTitle(audience, "language-test.title");

        languageManager.sendMessage(audience, "language-test.message");
        languageManager.broadcastMessage("language-test.broadcast", null);
    }
}
