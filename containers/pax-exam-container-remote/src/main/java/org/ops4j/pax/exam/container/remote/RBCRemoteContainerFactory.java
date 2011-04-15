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
package org.ops4j.pax.exam.container.remote;

import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.TestContainerFactory;

/**
 * @author Toni Menzel
 * @since Jan 26, 2010
 */
public class RBCRemoteContainerFactory implements TestContainerFactory
{

    /**
     * {@inheritDoc}
     */
    public TestContainer[] parse( final Option... options )
    {
        Parser p = new Parser( options );
        TestContainer container = new RBCRemoteContainer( new RBCRemoteTarget( p.getName(), p.getPort(), p.getRMILookupTimpout() ) );
        return new TestContainer[]{
            container
        };
    }
}
