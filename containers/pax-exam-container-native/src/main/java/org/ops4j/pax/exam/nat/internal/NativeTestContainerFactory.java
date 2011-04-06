/*
 * Copyright 2009 Toni Menzel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.exam.nat.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.osgi.framework.launch.FrameworkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.TestContainerException;
import org.ops4j.pax.exam.TestContainerFactory;
import org.ops4j.pax.exam.options.ProvisionOption;

/**
 * Stateful
 *
 * If would be really cool if this native runner would accept more than one osgi fw in classpath. Though this might not work due to dangerous cp
 * issues.
 *
 * @author Toni Menzel
 * @since Jan 7, 2010
 */
public class NativeTestContainerFactory implements TestContainerFactory {

    private static Logger LOG = LoggerFactory.getLogger( NativeTestContainer.class );

    public TestContainer[] parse( Option... options )
        throws TestContainerException
    {
        NativeTestContainerParser parser = new NativeTestContainerParser( options );
        List<ProvisionOption> bundles = parser.getBundles();//new NativeTestContainerParser().get( options );
        Map<String, String> properties = parser.getSystemProperties();

        Iterator<FrameworkFactory> factories = ServiceLoader.load( FrameworkFactory.class ).iterator();
        List<TestContainer> containers = new ArrayList<TestContainer>();
        while( factories.hasNext() ) {
            FrameworkFactory f = factories.next();
            LOG.info( "Found factory " + f.toString() );
            containers.add(new NativeTestContainer( f , bundles, properties ));
        }
        return containers.toArray( new TestContainer[containers.size()] );
    }

    public void shutdown()
    {
        
    }
}
