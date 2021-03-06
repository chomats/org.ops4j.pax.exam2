/*
 * Copyright 2011 Toni Menzel.
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
package org.ops4j.pax.exam.spi.reactors;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.ops4j.pax.exam.TestAddress;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.TestProbeProvider;
import org.ops4j.pax.exam.spi.StagedExamReactor;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 */
public abstract class BaseStagedReactorTest {

    abstract protected StagedExamReactor getReactor( List<TestContainer> containers, List<TestProbeProvider> providers );

    @Test
    public void testEmptyContainersAndProviders()
    {
        List<TestContainer> containers = new ArrayList<TestContainer>();
        List<TestProbeProvider> providers = new ArrayList<TestProbeProvider>();

        StagedExamReactor reactor = getReactor( containers, providers );
        assertThat( reactor.getTargets().size(), is( 0 ) );
    }

    @Test( expected = AssertionError.class )
    public void testInvokeNull()
        throws Exception
    {
        List<TestContainer> containers = new ArrayList<TestContainer>();
        List<TestProbeProvider> providers = new ArrayList<TestProbeProvider>();

        StagedExamReactor reactor = getReactor( containers, providers );
        reactor.invoke( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testInvokeInvalid()
        throws Exception
    {
        List<TestContainer> containers = new ArrayList<TestContainer>();
        List<TestProbeProvider> providers = new ArrayList<TestProbeProvider>();

        StagedExamReactor reactor = getReactor( containers, providers );
        TestAddress dummy = mock( TestAddress.class );
        reactor.invoke( dummy );
    }
}
