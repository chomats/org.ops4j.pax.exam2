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
package org.ops4j.pax.exam.rbc.client.intern;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Stack;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ops4j.io.StreamUtils;
import org.ops4j.pax.exam.ProbeInvoker;
import org.ops4j.pax.exam.TestAddress;
import org.ops4j.pax.exam.rbc.Constants;
import org.ops4j.pax.exam.rbc.client.RemoteBundleContextClient;
import org.ops4j.pax.exam.rbc.internal.RemoteBundleContext;

/**
 *
 */
public class RemoteBundleContextClientImpl implements RemoteBundleContextClient {

    // TODO duplicate
    private static final String PROBE_SIGNATURE_KEY = "Probe-Signature";

    private RemoteBundleContext m_remoteBundleContext = null;

    /**
     * JCL logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger( RemoteBundleContextClient.class );

    /**
     * Timeout for looking up the remote bundle context via RMI.
     */
    final private long m_rmiLookupTimeout;
    /**
     * Remote bundle context instance.
     */
    // private RemoteBundleContext m_remoteBundleContext;

    final private Integer m_registry;

    final private Stack<Long> m_installed;
    final private String m_name;

    /**
     * Constructor.
     *
     * @param name             of container
     * @param registry         RMI registry to look at
     * @param rmiLookupTimeout timeout for looking up the remote bundle context via RMI (cannot be null)
     */
    public RemoteBundleContextClientImpl( final String name,
                                          final Integer registry,
                                          final long rmiLookupTimeout )
    {
        assert registry != null : "registry should not be null";

        m_registry = registry;
        m_name = name;
        m_rmiLookupTimeout = rmiLookupTimeout;
        m_installed = new Stack<Long>();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    private <T> T getService( final Class<T> serviceType, final String filter, final long timeout )
    {
        return (T) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class<?>[]{ serviceType },
            new InvocationHandler() {
                /**
                 * {@inheritDoc}
                 * Delegates the call to remote bundle context.
                 */
                public Object invoke( final Object proxy,
                                      final Method method,
                                      final Object[] params )
                    throws Throwable
                {
                    try {
                        return getRemoteBundleContext().remoteCall(
                            method.getDeclaringClass(),
                            method.getName(),
                            method.getParameterTypes(),
                            filter,
                            timeout,
                            params
                        );
                    } catch( InvocationTargetException e ) {
                        throw e.getCause();
                    } catch( RemoteException e ) {
                        throw new RuntimeException( "Remote exception", e );
                    } catch( Exception e ) {
                        throw new RuntimeException( "Invocation exception", e );
                    }
                }
            }
        )
            ;
    }

    public long install( InputStream stream )
    {
        // turn this into a local url because we don't want pass the stream any further.
        try {
            //URI location = m_store.getLocation( m_store.store( stream ) );
            // pack as bytecode
            byte[] packed = pack( stream );

            long id = getRemoteBundleContext().installBundle( "no", packed );
            m_installed.push( id );
            getRemoteBundleContext().startBundle( id );
            return id;
        } catch( IOException e ) {
            throw new RuntimeException( e );
        } catch( BundleException e ) {
            throw new RuntimeException( "Bundle cannot be installed", e );
        }
    }

    private byte[] pack( InputStream stream )
    {
        LOG.info( "Packing probe into memory for true RMI. Hopefully things will fill in.." );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            StreamUtils.copyStream( stream, out, true );
        } catch( IOException e ) {

        }
        return out.toByteArray();
    }

    public void cleanup()
    {
        try {
            while( !m_installed.isEmpty() ) {
                Long id = m_installed.pop();
                getRemoteBundleContext().uninstallBundle( id );
            }
        } catch( IOException e ) {
            throw new RuntimeException( e );
        } catch( BundleException e ) {
            throw new RuntimeException( "Bundle cannot be uninstalled", e );
        }

    }

    /**
     * {@inheritDoc}
     */
    public void setBundleStartLevel( final long bundleId,
                                     final int startLevel )
    {
        try {
            getRemoteBundleContext().setBundleStartLevel( bundleId, startLevel );
        } catch( RemoteException e ) {
            throw new RuntimeException( "Remote exception", e );
        } catch( BundleException e ) {
            throw new RuntimeException( "Start level cannot be set", e );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void start()
    {
        try {
            getRemoteBundleContext().startBundle( 0 );
        } catch( RemoteException e ) {
            throw new RuntimeException( "Remote exception", e );
        } catch( BundleException e ) {
            throw new RuntimeException( "System bundle cannot be started", e );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stop()
    {
        try {
            getRemoteBundleContext().stopBundle( 0 );


        } catch( RemoteException e ) {
            // If its gone, its gone. Cannot do much about it anyway.
            //throw new RuntimeException( "Remote exception", e );
        } catch( BundleException e ) {
            throw new RuntimeException( "System bundle cannot be stopped", e );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void waitForState( final long bundleId,
                              final int state,
                              final long timeoutInMillis )

    {
        try {
            getRemoteBundleContext().waitForState( bundleId, state, timeoutInMillis );
        } catch( RemoteException e ) {
            throw new RuntimeException( "waitForState", e );
        } catch( BundleException e ) {
            throw new RuntimeException( "waitForState", e );
        }
    }

    /**
     * Looks up the {@link RemoteBundleContext} via RMI. The lookup will timeout in the specified number of millis.
     *
     * @return remote bundle context
     */
    private synchronized RemoteBundleContext getRemoteBundleContext()
    {
        if( m_remoteBundleContext == null ) {

            LOG.info( "TFetching Remote Bundle Context:" );
            //!! Absolutely necesary for RMI class loading to work
            // TODO maybe use ContextClassLoaderUtils.doWithClassLoader
            Thread.currentThread().setContextClassLoader( this.getClass().getClassLoader() );
            LOG.info( "Waiting for remote bundle context.. on " + m_registry + " name: " + m_name + " timout: " + m_rmiLookupTimeout );
            // TODO create registry here
            Throwable reason = null;
            long startedTrying = System.currentTimeMillis();

            try {
                do {
                    try {
                        Registry reg = LocateRegistry.getRegistry( m_registry );
                        m_remoteBundleContext = (RemoteBundleContext) reg.lookup( m_name );
                    } catch( ConnectException e ) {
                        reason = e;
                    } catch( NotBoundException e ) {
                        reason = e;
                    }

                }
                while( m_remoteBundleContext == null && ( m_rmiLookupTimeout == Constants.WAIT_FOREVER || System.currentTimeMillis() < startedTrying + m_rmiLookupTimeout ) );
            } catch( RemoteException e ) {

                //throw new RuntimeException( "Cannot get the remote bundle context", e );
            }
            if( m_remoteBundleContext == null ) {
                throw new RuntimeException( "Cannot get the remote bundle context", reason );
            }
            LOG.info( "Remote bundle context found after " + ( System.currentTimeMillis() - startedTrying ) + " millis" );
        }
        return m_remoteBundleContext;

    }

    public void call( TestAddress address )
    {
        String filterExpression = "(" + PROBE_SIGNATURE_KEY + "=" + address.root().identifier() + ")";
        ProbeInvoker service = getService( ProbeInvoker.class, filterExpression, 5000 );
        service.call( address.arguments() );
    }

    public String getName()
    {
        return m_name;
    }
}
