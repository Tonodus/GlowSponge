package net.glowstone.plugin;

import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {
    private final File pluginFolder;
    private List<Class<?>> plugins;

    public PluginLoader(File pluginFolder) {
        this.pluginFolder = pluginFolder;
        this.plugins = new ArrayList<>();
    }

    public List<Class<?>> load() {
        for (File file : pluginFolder.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                try {
                    JarFile jarFile = new JarFile(file);
                    loadJar(jarFile, file.getAbsolutePath());
                    jarFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return plugins;
    }

    private void loadJar(JarFile jarFile, String file) {
        Enumeration<JarEntry> pClasses = jarFile.entries();
        List<String> classes = new ArrayList<>();

        while (pClasses.hasMoreElements()) {
            JarEntry entry = pClasses.nextElement();
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                String className = entry.getName().substring(0, entry.getName().length() - 6);
                className = className.replace('/', '.');
                classes.add(className);
            }
        }

        if (!classes.isEmpty()) {
            try {
                URLClassLoader classLoader = new URLClassLoader(new URL[]{new URL("jar:file:" + file + "!/")});
                for (String className : classes) {
                    loadClass(classLoader.loadClass(className));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadClass(Class<?> clazz) {
        if (clazz.getAnnotation(Plugin.class) != null) {
            plugins.add(clazz);
        }
    }


}
