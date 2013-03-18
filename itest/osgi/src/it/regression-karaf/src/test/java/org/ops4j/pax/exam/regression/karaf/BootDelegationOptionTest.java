/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ops4j.pax.exam.regression.karaf;

import static junit.framework.Assert.assertEquals;
import static org.apache.karaf.tooling.exam.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.CoreOptions.bootDelegationPackage;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.regression.karaf.RegressionConfiguration.regressionDefaults;

import java.io.FileInputStream;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;

@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class BootDelegationOptionTest {

    @Configuration
    public Option[] config() {
        return new Option[]{
            regressionDefaults(),
                    bootDelegationPackage("com.sun.*") };
    }

    @Test
    public void test() throws Exception {
        Properties prop = new Properties();
        prop.load(new FileInputStream("etc/config.properties"));
        String delegation = prop.get("org.osgi.framework.bootdelegation").toString();
        assertEquals(new StringBuilder()
            .append("org.apache.karaf.jaas.boot,")
            .append("org.apache.karaf.jaas.boot.principal,")
            .append("sun.*,")
            .append("com.sun.*,")
            .append("javax.transaction,")
            .append("javax.transaction.*,")
            .append("sun.*,")
            .append("com.sun.*")
            .toString(),
            delegation);
    }

}
