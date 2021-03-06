package net.thucydides.core.webdriver

import spock.lang.Specification
import net.thucydides.core.annotations.Managed
import org.openqa.selenium.WebDriver
import net.thucydides.core.pages.Pages
import net.thucydides.core.annotations.ManagedPages
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.junit.Test

import static org.mockito.Mockito.verify
import net.thucydides.core.util.MockEnvironmentVariables

class WhenProvidingWebdriverSupportForThucydidesExtensions extends Specification {


    def "should allow plugins to instantiate a webdriver manager"() {
        when: "we initialize Thucydides support"
        ThucydidesWebDriverSupport.initialize()
        then: "we can obtain a webdriver manager instance"
        ThucydidesWebDriverSupport.getWebdriverManager() != null
    }

    def "should allow plugins to obtain a webdriver driver instance"() {
        when: "we initialize Thucydides support"
        ThucydidesWebDriverSupport.initialize()
        then: "we can obtain a webdriver firefoxDriver"
        ThucydidesWebDriverSupport.getDriver() != null
    }

    def "should allow plugins to obtain a page factory instance"() {
        when: "we initialize Thucydides support"
        ThucydidesWebDriverSupport.initialize()
        then: "we can obtain a page factory"
        ThucydidesWebDriverSupport.getPages() != null
    }

    class ObjectWithManagedWebdriver {
        @Managed
        WebDriver driver;
    }

    def "should inject the webdriver instance into the managed field of a specified object"() {
        given: "we initialize Thucydides support"
        ThucydidesWebDriverSupport.initialize()
        and: "we have an object with a @Managed WebDriver field"
        def testCase = new ObjectWithManagedWebdriver()
        when: "we inject the webdriver fields into this class"
        ThucydidesWebDriverSupport.initializeFieldsIn(testCase)
        then: "the webdriver field should be initialized"
        testCase.driver != null
    }

    class ObjectWithManagedPageFactory {
        @ManagedPages
        Pages pages;
    }

    def "should inject the pages instance into the managed field of a specified object"() {
        given: "we initialize Thucydides support"
        ThucydidesWebDriverSupport.initialize()
        and: "we have an object with a @ManagedPages page factory field"
        def testCase = new ObjectWithManagedPageFactory()
        when: "we inject the webdriver fields into this class"
        ThucydidesWebDriverSupport.initializeFieldsIn(testCase)
        then: "the webdriver field should be initialized"
        testCase.pages != null
    }

    def "should prepare a step factory"() {
        given: "we initialize Thucydides support"
        ThucydidesWebDriverSupport.initialize()
        when: "we need a step factory"
        def stepFactory = ThucydidesWebDriverSupport.getStepFactory()
        then: "the support class should provide one"
        stepFactory != null
        and: "the step factory page factory should have been set"
        stepFactory.pages == ThucydidesWebDriverSupport.getPages()
    }

    def "should provide access to the webdriver instance"() {
        when: "we initialize Thucydides support"
        ThucydidesWebDriverSupport.initialize()
        then: "we should be able to access the current firefoxDriver"
        ThucydidesWebDriverSupport.getDriver() != null
    }

    def "should not initialize agail if already initialized with a given driver"() {
        when: "we initialize Thucydides support"
        ThucydidesWebDriverSupport.initialize()
        def firstDriver = ThucydidesWebDriverSupport.driver;
        and: "we initialize support again"
        ThucydidesWebDriverSupport.initialize()
        def secondDriver = ThucydidesWebDriverSupport.driver;
        then: "the firefoxDriver should be unchanged"
        secondDriver == firstDriver
    }

    def "should be able to define the driver"() {
        when: "we initialize Thucydides support with a specified firefoxDriver"
        ThucydidesWebDriverSupport.initialize("htmlunit")
        then: "the provided firefoxDriver should be of the specified type"
        ThucydidesWebDriverSupport.driver.driverClass == HtmlUnitDriver
    }


}
