/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.admingui.devtests;

import com.thoughtworks.selenium.Selenium;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 *
 * @author jasonlee
 */
public class SeleniumHelper {
    private static SeleniumHelper instance;
    private Selenium selenium;
    private WebDriver driver;
    private static final Logger logger = Logger.getLogger(SeleniumHelper.class.getName());

    private SeleniumHelper() {
    }

    public synchronized static SeleniumHelper getInstance() {
        if (instance == null) {
            instance = new SeleniumHelper();
        }

        return instance;
    }

    public Selenium getSeleniumInstance() {
        if (selenium == null) {
            logger.log(Level.INFO, "Creating new selenium instance");
            String browser = getParameter("browser", "firefox");

            if ("firefox".equals(browser)) {
                driver = new FirefoxDriver();
            } else if ("chrome".equals(browser)) {
                driver = new ChromeDriver();
            } else if ("ie".contains(browser)) {
                driver = new InternetExplorerDriver();
            }

            selenium = new WebDriverBackedSelenium(driver, getBaseUrl());
            selenium.setTimeout("90000");
            (new BaseSeleniumTestClass()).openAndWait("/", BaseSeleniumTestClass.TRIGGER_COMMON_TASKS, 480); // Make sure the server has started and the user logged in
        }

        return selenium;
    }
    
    public void releaseSeleniumInstance() {
        if (selenium != null) {
            logger.log(Level.INFO, "Releasing selenium instance");
            selenium.stop();
            selenium = null;
        }
    }

    public String getBaseUrl() {
        return "http://localhost:" + getParameter("admin.port", "4848");
    }

    public WebDriver getDriver() {
        return driver;
    }

    public static String getParameter(String paramName, String defaultValue) {
        String value = System.getProperty(paramName);

        return value != null ? value : defaultValue;
    }
}
