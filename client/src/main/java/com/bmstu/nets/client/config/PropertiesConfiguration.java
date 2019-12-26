package com.bmstu.nets.client.config;

import com.bmstu.nets.common.logger.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Properties;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;
import static com.google.common.collect.Maps.newHashMap;

public class PropertiesConfiguration {
    private static final Logger logger = getLogger(PropertiesConfiguration.class);
    private static final String PROPERTIES_PATH = "application.properties";

    private final Gson gson = new GsonBuilder().create();

    private final Properties properties;

    private static class PropertiesConfigurationHolder {
        static final PropertiesConfiguration INSTANCE = new PropertiesConfiguration();
    }

    public static PropertiesConfiguration instance() {
        return PropertiesConfigurationHolder.INSTANCE;
    }

    private PropertiesConfiguration() {
        this.properties = new Properties();
        ClassLoader loader = this.getClass().getClassLoader();
        loadProperties(loader, this.properties, PROPERTIES_PATH);
    }

    @Nonnull
    public <C> C loadProperties(@Nonnull Class<C> clazz) {
        return loadProperties(properties, clazz);
    }

    @Nonnull
    private <C> C loadProperties(@Nonnull Properties properties, @Nonnull Class<C> clazz) {
        final Map<String, String> propertiesMap = loadPropertiesToMap(properties);
        return gson.fromJson(JSONObject.toJSONString(propertiesMap), clazz);
    }

    private void loadProperties(@Nonnull ClassLoader loader, @Nonnull Properties properties, @Nonnull String path) {
        try {
            java.net.URL url = loader.getResource(path);
            if (url == null) {
                logger.error("Could not find configuration file: {}", path);
                return;
            }
            properties.load(url.openStream());
        } catch (Exception e) {
            logger.error("Could not load configuration file: {}", path);
        }
    }

    @Nonnull
    private Map<String, String> loadPropertiesToMap(@Nonnull Properties properties) {
        final Map<String, String> propertiesMap = newHashMap();
        for (final String name : properties.stringPropertyNames()) {
            propertiesMap.put(name, properties.getProperty(name));
        }
        return propertiesMap;
    }
}
