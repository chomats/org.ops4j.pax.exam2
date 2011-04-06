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
package org.ops4j.pax.exam.spi.container;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.ops4j.pax.exam.TestProbeBuilder;
import org.ops4j.pax.exam.spi.probesupport.intern.TestProbeBuilderImpl;
import org.ops4j.store.Store;
import org.ops4j.store.StoreFactory;

/**
 * @author Toni Menzel
 * @since Jan 11, 2010
 */
public class PlumbingContext {

    private Store<InputStream> m_store;

    public PlumbingContext() {
        m_store = StoreFactory.defaultStore();
    }

    public TestProbeBuilder createProbe( Properties p )
        throws IOException
    {
        return new TestProbeBuilderImpl( p, m_store );
    }

    public TestProbeBuilder createProbe()
        throws IOException
    {
        Properties p = new Properties();
        return createProbe( p );
    }
}
