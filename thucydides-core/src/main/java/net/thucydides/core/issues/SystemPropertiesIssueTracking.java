package net.thucydides.core.issues;

import com.google.inject.Inject;
import net.thucydides.core.ThucydidesSystemProperties;
import net.thucydides.core.ThucydidesSystemProperty;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.util.EnvironmentVariables;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Determine the issue tracking URL formats for a project.
 */
public class SystemPropertiesIssueTracking implements IssueTracking {

    private final EnvironmentVariables environmentVariables;

    protected ThucydidesSystemProperties getSystemProperties() {
        return ThucydidesSystemProperties.getProperties();
    }

    public SystemPropertiesIssueTracking() {
        this(Injectors.getInjector().getInstance(EnvironmentVariables.class));
    }

    @Inject
    public SystemPropertiesIssueTracking(EnvironmentVariables environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public String getIssueTrackerUrl() {


        if (jiraUrlDefined()) {
            return environmentVariables.getProperty(ThucydidesSystemProperty.JIRA_URL.getPropertyName())
                                         + "/browse/" + getJiraProjectSuffix() + "{0}";
        } else {
            return environmentVariables.getProperty(ThucydidesSystemProperty.ISSUE_TRACKER_URL.getPropertyName());
        }
    }

    private String getJiraProjectSuffix() {
        if (jiraProjectDefined()) {
            return environmentVariables.getProperty(ThucydidesSystemProperty.JIRA_PROJECT.getPropertyName()) + "-";
        } else {
            return "";
        }
    }

    private boolean jiraUrlDefined() {
       return !isEmpty(environmentVariables.getProperty(ThucydidesSystemProperty.JIRA_URL.getPropertyName()));
    }

    private boolean jiraProjectDefined() {
       return !isEmpty(environmentVariables.getProperty(ThucydidesSystemProperty.JIRA_PROJECT.getPropertyName()));
    }
}