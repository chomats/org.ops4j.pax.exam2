/*
 * Copyright 2008 Alin Dreghiciu.
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
package org.ops4j.pax.exam;

/**
 * Management of an OSGi framework that can be used as a integration regression container.
 * Each container is also a test target.
 *
 * When constucting TestContainers, it is good practice to not put the parsing part (from Option[]) into the implementation.
 * Instead, make the native container construction really simple and tied to the underlying container.
 *
 * @author Alin Dreghiciu (adreghiciu@gmail.com)
 * @author Toni Menzel (toni@okidokiteam.com)
 * @since 0.3.0, December 09, 2008
 */
public interface TestContainer extends TestTarget {

    /**
     * Sets the start level for a bundle.
     *
     * @param bundleId   bundle id
     * @param startLevel start level
     *
     * @throws TestContainerException if startlevel cannot be set
     */
    void setBundleStartLevel( long bundleId, int startLevel )
        throws TestContainerException;

    /**
     * Starts the regression container.
     *
     * @return this for fluent api
     *
     * @throws TimeoutException - if timeout occured and the regression container cannot be started
     */
    TestContainer start()
        throws TimeoutException;

    /**
     * Stops the regression container. Implementations should take care of calling cleanup() internally, too.
     *
     * @return this for fluent api
     *
     * @throws TimeoutException - if timeout occured and the regression container cannot be stopped
     */
    TestContainer stop()
        throws TimeoutException;


}
