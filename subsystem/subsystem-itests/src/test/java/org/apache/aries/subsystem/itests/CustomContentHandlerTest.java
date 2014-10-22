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
package org.apache.aries.subsystem.itests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import org.apache.aries.subsystem.ContentHandler;
import org.junit.Test;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.namespace.IdentityNamespace;
import org.osgi.resource.Capability;
import org.osgi.resource.Resource;
import org.osgi.service.coordinator.Coordination;
import org.osgi.service.subsystem.Subsystem;

public class CustomContentHandlerTest extends SubsystemTest {

    @Override
    protected void createApplications() throws Exception {
        createApplication("customContent", "custom1.sausages", "customContentBundleA.jar");
    }

    @Test
    public void testCustomContentHandler() throws Exception {
        SausagesContentHandler handler = new SausagesContentHandler();

        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put(ContentHandler.CONTENT_TYPE_PROPERTY, "foo.sausages");
        ServiceRegistration<ContentHandler> reg = bundleContext.registerService(ContentHandler.class, handler, props);

        assertEquals("Precondition", 0, handler.calls.size());
        Subsystem subsystem = installSubsystemFromFile("customContent.esa");
        try {
            assertEquals(Arrays.asList("install:customContent1 sausages = 1"), handler.calls);

            Collection<Resource> constituents = subsystem.getConstituents();
            assertEquals("The custom content should not show up as a subsystem constituent",
                    1, constituents.size());

            boolean foundBundle = false;
            for (Resource c : constituents) {
                for(Capability idCap : c.getCapabilities(IdentityNamespace.IDENTITY_NAMESPACE)) {
                    Object name = idCap.getAttributes().get(IdentityNamespace.IDENTITY_NAMESPACE);
                    if ("org.apache.aries.subsystem.itests.customcontent.bundleA".equals(name))
                        foundBundle = true;
                }
            }
            assertTrue(foundBundle);
        } finally {
            uninstallSubsystemSilently(subsystem);
            reg.unregister();
        }
    }

    static class SausagesContentHandler implements ContentHandler {
        List<String> calls = new ArrayList<String>();

        private static String convertStreamToString(InputStream is) {
            Scanner s = new Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }

        @Override
        public void install(InputStream is, String symbolicName, String type, Subsystem subsystem, Coordination coordination) {
            String content = convertStreamToString(is);
            calls.add(("install:" + symbolicName + " " + content).trim());
        }

        @Override
        public void start(String symbolicName, String type, Subsystem subsystem, Coordination coordination) {
            calls.add("start:" + symbolicName);
        }

        @Override
        public void stop(String symbolicName, String type, Subsystem subsystem, Coordination coordination) {
            calls.add("stop:" + symbolicName);
        }

        @Override
        public void uninstall(String symbolicName, String type, Subsystem subsystem, Coordination coordination) {
            calls.add("uninstall:" + symbolicName);
        }
    }
}
