package studia.ium.warehouseandroidclient.application.configuration.properties;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesProvider {
    private Properties properties = new Properties();

    public PropertiesProvider(Context context, String configPropertiesFileName) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(configPropertiesFileName);
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not load properties file");
        }
    }

    public String getStringProperty(String name) {
        return properties.getProperty(name);
    }
}
