package org.apache.aries.subsystem.itests.tb4;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put("test", "tb4");
        context.registerService(String.class, "tb4", props);
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }

}
