package com.SirBlobman.api.nms;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class MultiVersionHandler<P extends JavaPlugin> {
    private final P plugin;
    private final AbstractNMS nmsInterface;
    public MultiVersionHandler(P plugin) {
        this.plugin = plugin;
        this.nmsInterface = setupInterface();
    }
    
    public P getPlugin() {
        return this.plugin;
    }
    
    public AbstractNMS getInterface() {
        return this.nmsInterface;
    }
    
    private AbstractNMS setupInterface() {
        Logger logger = this.plugin.getLogger();
        Class<? extends AbstractNMS> interfaceClass = getInterfaceClass();
        if(interfaceClass == null) {
            logger.log(Level.SEVERE, "An error ocurred while initializing the NMS interface!", new IllegalStateException("null interface class!"));
            return null;
        }
        
        try {
            JavaPlugin plugin = getPlugin();
            Constructor<? extends AbstractNMS> constructor = interfaceClass.getConstructor(JavaPlugin.class);
            AbstractNMS abstractNMS = constructor.newInstance(plugin);
            
            logger.info("Successfully found NMS Handler class '" + interfaceClass.getName() + "'.");
            return abstractNMS;
        } catch(ReflectiveOperationException ex) {
            logger.log(Level.SEVERE, "An error ocurred while initializing the NMS interface!", ex);
            return null;
        }
    }
    
    private Class<? extends AbstractNMS> getInterfaceClass() {
        Logger logger = this.plugin.getLogger();
        String nmsVersion = VersionUtil.getNetMinecraftServerVersion();
        logger.info("Searching for a handler that matches NMS version " + nmsVersion);
        
        try {
            String className = ("com.SirBlobman.api.nms.NMS_" + nmsVersion);
            Class<?> interfaceClass = Class.forName(className);
            return interfaceClass.asSubclass(AbstractNMS.class);
        } catch(ClassNotFoundException | ClassCastException ex) {
            logger.log(Level.WARNING, "A handler could not be found for NMS version " + nmsVersion + ": ", ex);
            logger.info("Attempting to find a fallback handler...");
        }
        
        try {
            Class<?> interfaceClass = Class.forName("com.SirBlobman.api.nms.NMS_Fallback");
            Class<? extends AbstractNMS> nmsClass = interfaceClass.asSubclass(AbstractNMS.class);
            
            logger.info("Using fallback NMS handler for version " + nmsVersion);
            logger.info("Please contact SirBlobman and ask him to add support for it.");
            return nmsClass;
        } catch(ClassNotFoundException | ClassCastException ex) {
            logger.log(Level.SEVERE, "The fallback handler could not be found, please contact SirBlobman!", ex);
            return null;
        }
    }
}