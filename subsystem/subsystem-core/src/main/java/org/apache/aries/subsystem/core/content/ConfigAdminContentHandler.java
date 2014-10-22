/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.aries.subsystem.core.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
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
    public static final String FELIXCM_CONTENT_TYPE = "felix.cm.config";
    public static final String PROPERTIES_CONTENT_TYPE = "osgi.config.properties";
    public static final String[] CONTENT_TYPES = {PROPERTIES_CONTENT_TYPE, FELIXCM_CONTENT_TYPE};

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
    public void install(InputStream is, String symbolicName, String contentType, Subsystem subsystem, Coordination coordination) {
        // TODO potentially move this to the start() phase... although it might make sense to do this here too

        Dictionary configDict = null;
        try {
            if (PROPERTIES_CONTENT_TYPE.equals(contentType)) {
                Properties p = new Properties();
                p.load(is);
                configDict = p;
            } else if (FELIXCM_CONTENT_TYPE.equals(contentType)) {
                configDict = ConfigurationHandler.read(is);
            }

            ConfigurationAdmin cm = cmTracker.getService();
            if (cm == null) {
                // TODO log
                throw new ServiceException(ConfigurationAdmin.class.getName(), ServiceException.UNREGISTERED);
            }
            Configuration conf = cm.getConfiguration(symbolicName, null);
            System.out.println("Handling ConfigAdmin content " + configDict);
            conf.update(configDict);
        } catch (IOException e) {
            e.printStackTrace(); // TODO log
        } finally {
            try { is.close(); } catch (IOException ioe) {}
        }
    }

    @Override
    public void start(String symbolicName, String contentType, Subsystem subsystem, Coordination coordination) {
        // TODO Auto-generated method stub
    }

    @Override
    public void stop(String symbolicName, String contentType, Subsystem subsystem, Coordination coordination) {
        // TODO Auto-generated method stub
    }

    @Override
    public void uninstall(String symbolicName, String contentType, Subsystem subsystem, Coordination coordination) {
        // TODO Auto-generated method stub
    }
}
