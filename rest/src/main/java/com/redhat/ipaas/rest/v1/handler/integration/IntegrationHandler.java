/**
 * Copyright (C) 2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.ipaas.rest.v1.handler.integration;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.Path;

import com.redhat.ipaas.core.IPaasServerException;
import com.redhat.ipaas.dao.manager.DataManager;
import com.redhat.ipaas.github.GitHubService;
import com.redhat.ipaas.model.integration.Integration;
import com.redhat.ipaas.openshift.OpenShiftService;
import com.redhat.ipaas.project.converter.IntegrationToProjectConverter;
import com.redhat.ipaas.rest.v1.handler.BaseHandler;
import com.redhat.ipaas.rest.v1.operations.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Path("/integrations")
@Api(value = "integrations")
@Component
public class IntegrationHandler extends BaseHandler implements Lister<Integration>, Getter<Integration>, Creator<Integration>, Deleter<Integration>, Updater<Integration> {

    private final GitHubService gitHubService;

    private final IntegrationToProjectConverter projectConverter;

    @Value("${openshift.apiBaseUrl}")
    private String openshiftApiBaseUrl;

    @Value("${openshift.namespace}")
    private String namespace;

    private final OpenShiftService openShiftService;

    public IntegrationHandler(DataManager dataMgr, GitHubService gitHubService, IntegrationToProjectConverter projectConverter, OpenShiftService openShiftService) {
        super(dataMgr);
        this.gitHubService = gitHubService;
        this.projectConverter = projectConverter;
        this.openShiftService = openShiftService;
    }

    @Override
    public Class<Integration> resourceClass() {
        return Integration.class;
    }

    @Override
    public String resourceKind() {
        return Integration.KIND;
    }

    @Override
    public Integration create(Integration integration) {

        String secret = createSecret();
        String gitCloneUrl = ensureGitHubSetup(integration, secret);

        Integration requestedIntegration = new Integration.Builder().createFrom(integration).gitRepo(gitCloneUrl).build();
        ensureOpenShiftResources(requestedIntegration, secret);
        return Creator.super.create(requestedIntegration);
    }

    // ==========================================================================

    private String ensureGitHubSetup(Integration integration, String secret) {
        try {
            Integration integrationWithGitRepoName = ensureGitRepoName(integration);
            String repoName = integrationWithGitRepoName.getGitRepo().orElseThrow(() -> new IllegalArgumentException("Missing git repo in integration"));

            Map<String, byte[]> fileContents = projectConverter.convert(integrationWithGitRepoName);

            // Secret to be used in the build trigger
            String webHookUrl = createWebHookUrl(repoName, secret);

            // Do all github stuff at once
            return gitHubService.createOrUpdateProjectFiles(repoName, generateCommitMessage(), fileContents, webHookUrl);

        } catch (IOException e) {
            throw IPaasServerException.launderThrowable(e);
        }
    }

    private String createWebHookUrl(String bcName, String secret) {
        return String.format(
            "%s/namespaces/%s/buildconfigs/%s/webhooks/%s/github", openshiftApiBaseUrl, namespace, bcName, secret);
    }


    private Integration ensureGitRepoName(Integration integration) {
        Optional<String> repoNameOptional = integration.getGitRepo();
        if (!repoNameOptional.isPresent()) {
            String generatedRepoName = gitHubService.sanitizeRepoName(integration.getName());
            integration = new Integration.Builder().createFrom(integration).gitRepo(generatedRepoName).build();
        }
        return integration;
    }

    private String generateCommitMessage() {
        // TODO Let's generate some nice message...
        return "Updated";
    }

    private String createSecret() {
        return UUID.randomUUID().toString();
    }

    private void ensureOpenShiftResources(Integration integration, String secret) {
        integration.getGitRepo().ifPresent(gitRepo -> openShiftService.createOpenShiftResources(integration.getName(), gitRepo, secret));
    }
}
