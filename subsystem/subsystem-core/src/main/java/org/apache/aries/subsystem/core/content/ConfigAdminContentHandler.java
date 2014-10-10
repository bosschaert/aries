package org.apache.aries.subsystem.core.content;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.aries.subsystem.ContentHandler;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.coordinator.Coordination;
import org.osgi.service.subsystem.Subsystem;
import org.osgi.util.tracker.ServiceTracker;

public class ConfigAdminContentHandler implements ContentHandler {
    public static final String CONTENT_TYPE = "osgi.config";

    private final ServiceTracker<ConfigurationAdmin,ConfigurationAdmin> cmTracker;

    public ConfigAdminContentHandler(BundleContext ctx) {
        cmTracker = new ServiceTracker<ConfigurationAdmin, ConfigurationAdmin>(
                ctx, ConfigurationAdmin.class, null);
        cmTracker.open();
    }

    public void shutDown() {
        cmTracker.close();
    }

    @Override @SuppressWarnings({ "unchecked", "rawtypes" })
    public void install(InputStream is, String symbolicName, Subsystem subsystem, Coordination coordination) {
        // TODO potentially move this to the start() phase... although it might make sense to do this here too
        Properties p = new Properties();
        try {
            try {
                p.load(is);

                ConfigurationAdmin cm = cmTracker.getService();
                if (cm == null) {
                    // TODO log
                    throw new ServiceException(ConfigurationAdmin.class.getName(), ServiceException.UNREGISTERED);
                }
                Configuration conf = cm.getConfiguration(symbolicName, null);
                conf.update((Hashtable) p);
            } finally {
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace(); // TODO log
        }
        System.out.println("Handling ConfigAdmin content " + p);
    }

    @Override
    public void start(String symbolicName, Subsystem subsystem, Coordination coordination) {
        // TODO Auto-generated method stub
    }

    @Override
    public void stop(String symbolicName, Subsystem subsystem, Coordination coordination) {
        // TODO Auto-generated method stub
    }

    @Override
    public void uninstall(String symbolicName, Subsystem subsystem, Coordination coordination) {
        // TODO Auto-generated method stub
    }
}
