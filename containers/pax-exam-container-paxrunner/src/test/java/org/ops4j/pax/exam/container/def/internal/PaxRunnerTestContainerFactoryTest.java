package org.ops4j.pax.exam.container.def.internal;

import org.junit.Test;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.TestContainerFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * Test the Pax Runner Factory implementation.
 */
public class PaxRunnerTestContainerFactoryTest {

    @Test
    public void testEmptyOptions()
        throws Exception
    {
        TestContainerFactory factory = new PaxRunnerTestContainerFactory();

        TestContainer[] containers = factory.parse();
        assertThat( containers.length, is( 1 ) );
        TestContainer container = containers[ 0 ];
        assertNotNull( container );
    }

    @Test
    public void testSetFramework()
        throws Exception
    {
        TestContainerFactory factory = new PaxRunnerTestContainerFactory();

        TestContainer[] containers = factory.parse( felix() );
        assertThat( containers.length, is( 1 ) );
        
        assertNotNull( containers[ 0 ] );
    }

    @Test
    public void testSetMoreFrameworks()
        throws Exception
    {
        TestContainerFactory factory = new PaxRunnerTestContainerFactory();

        TestContainer[] containers = factory.parse( felix(), equinox() );
        assertThat( containers.length, is( 2 ) );

        assertNotNull( containers[ 0 ] );
        assertNotNull( containers[ 1 ] );
        assertNotSame( containers[ 0 ], containers[ 1 ] );
    }
}
