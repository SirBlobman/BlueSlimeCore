package com.github.sirblobman.bossbar.legacy.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.Server;

public abstract class Reflection {
    public static String getVersion() {
        Server server = Bukkit.getServer();
        Class<?> class_Server = server.getClass();
        Package package_Server = class_Server.getPackage();

        String packageName = package_Server.getName();
        int lastIndexOf = packageName.lastIndexOf('.');
        return packageName.substring(lastIndexOf + 1) + ".";
    }
    
    public static Class<?> getNMSClass(String className) {
        try {
            return getNMSClassWithException(className);
        } catch(Exception ex) {
            return null;
        }
    }
    
    public static Class<?> getNMSClassWithException(String className) throws Exception {
        String version = getVersion();
        String fullClassName = ("net.minecraft.server." + version + className);
        return Class.forName(fullClassName);
    }

    public static Object getHandle(Object object) {
        if(object == null) return null;

        try {
            Class<?> class_object = object.getClass();
            Method method_getHandle = getMethod(class_object, "getHandle");
            if(method_getHandle == null) return null;

            return method_getHandle.invoke(object);
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... methodParameters) {
        Method[] methodArray = clazz.getMethods();
        for(Method method : methodArray) {
            String loopMethodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            if(loopMethodName.equals(methodName) && (methodParameters.length == 0
                    || classListEqual(methodParameters, parameterTypes))) {
                method.setAccessible(true);
                return method;
            }
        }

        return null;
    }
    
    public static boolean classListEqual(Class<?>[] list1, Class<?>[] list2) {
        if(list1.length != list2.length) return false;
        int listLength = list1.length;

        for(int i = 0; i < listLength; ++i) {
            if(list1[i] != list2[i]) {
                return false;
            }
        }

        return true;
    }
}
