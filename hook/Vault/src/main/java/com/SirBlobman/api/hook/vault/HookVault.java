package com.SirBlobman.api.hook.vault;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.SirBlobman.api.handler.Hook;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class HookVault<P extends JavaPlugin> extends Hook<P, Vault> {
    private Chat chatHandler;
    private Economy economyHandler;
    private Permission permissionHandler;
    public HookVault(P plugin) {
        super(plugin, Vault.class);
        this.chatHandler = null;
        this.economyHandler = null;
        this.permissionHandler = null;
    }
    
    public boolean setupChatHandler() {
        if(this.chatHandler != null) return true;
        return ((this.chatHandler = hookType(Chat.class, "chat")) != null);
    }
    
    public boolean setupEconomyHandler() {
        if(this.economyHandler != null) return true;
        return ((this.economyHandler = hookType(Economy.class, "economy")) != null);
    }
    
    public boolean setupPermissionHandler() {
        if(this.permissionHandler != null) return true;
        return ((this.permissionHandler = hookType(Permission.class, "permission")) != null);
    }
    
    public Chat getChatHandler() {
        return this.chatHandler;
    }
    
    public Economy getEconomyHandler() {
        return this.economyHandler;
    }
    
    public Permission getPermissionHandler() {
        return this.permissionHandler;
    }
    
    private boolean checkVault() {
        if(isHookEnabled()) return false;
        
        Logger logger = getLogger();
        logger.warning("Vault is not enabled correctly!");
        return true;
    }
    
    private <T> T hookType(Class<T> type, String typeName) {
        if(checkVault()) return null;
    
        try {
            ServicesManager servicesManager = Bukkit.getServicesManager();
            RegisteredServiceProvider<T> registration = servicesManager.getRegistration(type);
            if(registration == null) {
                Logger logger = getLogger();
                logger.warning("A Vault " + typeName + " handler is not registered.");
                return null;
            }
            
            T provider = registration.getProvider();
            if(provider == null) {
                Logger logger = getLogger();
                logger.warning("A Vault " + typeName + " handler is invalid.");
                return null;
            }
        
            Plugin plugin = registration.getPlugin();
            PluginDescriptionFile description = plugin.getDescription();
            String fullName = description.getFullName();
            String providerName = getProviderName(provider);
        
            Logger logger = getLogger();
            logger.info("Successfully hooked into " + typeName + " handler '" + providerName + "' from plugin '" + fullName + "'.");
            return provider;
        } catch(Exception ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING,"An error occurred while setting up the Vault " + typeName + " handler:", ex);
            return null;
        }
    }
    
    private String getProviderName(Object object) {
        try {
            Class<?> objectClass = object.getClass();
            Method method_getName = objectClass.getMethod("getName");
            return (String) method_getName.invoke(object);
        } catch(Exception ex) {
            return object.toString();
        }
    }
}