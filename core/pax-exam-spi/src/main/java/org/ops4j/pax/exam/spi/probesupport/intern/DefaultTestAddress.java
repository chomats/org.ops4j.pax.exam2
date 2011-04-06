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
package org.ops4j.pax.exam.spi.probesupport.intern;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ops4j.pax.exam.TestAddress;

/**
 *
 */
public class DefaultTestAddress implements TestAddress {

    private static Logger LOG = LoggerFactory.getLogger( DefaultTestAddress.class );

    final private String m_sig;
    final private TestAddress m_root;
    final private String m_caption;

    public DefaultTestAddress(String caption)
    {
        this( null, caption );
    }

    public DefaultTestAddress( final TestAddress parent, String caption )
    {
        m_sig = calculate();
        if (parent != null) {
            m_caption = parent.caption() + ":" + caption;
        }else {
            m_caption = caption;
        }
        
        m_root = calculateRoot( parent );
        LOG.info( "NEW ADDRESS= " + m_sig + " parent=" + parent + " root=" + m_root);
    }

    private String calculate()
    {
        // just a temporary provider. ID should be given from a passed in context.
        return "PaxExam-" + UUID.randomUUID().toString();
    }

    @Override
    public int hashCode()
    {
        return m_sig.hashCode();
    }

    @Override
    public boolean equals( Object o )
    {
        if( o instanceof TestAddress ) {
            return o.equals( m_sig );
        }
        return false;
    }

    public String identifier()
    {
        return m_sig;
    }

    public String caption()
    {
        return m_caption;
    }

    private TestAddress calculateRoot( TestAddress parent )
    {
        if( parent != null ) {
            return parent.root();
        }
        else {
            return this;
        }
    }

    public TestAddress root()
    {
        return m_root;
    }

    @Override
    public String toString()
    {
        return "[TestAddress:" + m_sig + " root:" + m_root.identifier() + "]";
    }
}
