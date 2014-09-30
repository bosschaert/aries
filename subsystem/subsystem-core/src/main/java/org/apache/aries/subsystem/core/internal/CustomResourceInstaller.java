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
package org.apache.aries.subsystem.core.internal;

import java.io.InputStream;

import org.apache.aries.subsystem.ContentHandler;
import org.osgi.resource.Resource;
import org.osgi.service.coordinator.Coordination;
import org.osgi.service.repository.RepositoryContent;

public class CustomResourceInstaller extends ResourceInstaller {
    private final ContentHandler handler;

    public CustomResourceInstaller(Coordination coordination, Resource resource, BasicSubsystem subsystem, ContentHandler handler) {
        super(coordination, resource, subsystem);
        this.handler = handler;
    }

    @Override
    public Resource install() throws Exception {
        InputStream is = ((RepositoryContent) resource).getContent();
        handler.install(is, ResourceHelper.getSymbolicNameAttribute(resource), subsystem, coordination);
        return resource;
    }
}
