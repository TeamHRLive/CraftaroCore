package com.craftaro.core.hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class HookManager<T extends PluginHook> {

    protected final Plugin plugin;
    protected final Map<String, T> registeredHooks = new HashMap<>();

    public HookManager(Plugin plugin) {
        this.plugin = plugin;
        registerDefaultHooks();
    }

    public T getHookByName(String name) {
        if (registeredHooks.containsKey(name)) {
            return registeredHooks.get(name);
        }

        return getDummyHook();
    }

    public Collection<T> getAllHooks() {
        return registeredHooks.values();
    }

    public void registerHook(Class<? extends T> hook) {
        registerHook(null, hook);
    }

    public void registerHook(String requiredPlugin, Class<? extends T> hookClazz) {
        if (requiredPlugin != null && !Bukkit.getPluginManager().isPluginEnabled(requiredPlugin)) {
            return;
        }

        T hook = null;
        try {
            hook = hookClazz.getConstructor(Plugin.class).newInstance(plugin);
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (!hook.enableHook()) {
            return;
        }

        registeredHooks.put(hook.getHookName(), hook);
    }

    protected abstract void registerDefaultHooks();
    protected abstract T getDummyHook();
}
