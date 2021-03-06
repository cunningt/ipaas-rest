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

import javax.ws.rs.Path;

import com.redhat.ipaas.dao.manager.DataManager;
import com.redhat.ipaas.model.integration.IntegrationTemplate;
import com.redhat.ipaas.rest.v1.handler.BaseHandler;
import com.redhat.ipaas.rest.v1.operations.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/integrationtemplates")
@Api(value = "integrationtemplates")
@Component
public class IntegrationTemplateHandler extends BaseHandler implements Lister<IntegrationTemplate>, Getter<IntegrationTemplate>, Creator<IntegrationTemplate>, Deleter<IntegrationTemplate>, Updater<IntegrationTemplate> {

    public IntegrationTemplateHandler(DataManager dataMgr) {
        super(dataMgr);
    }

    @Override
    public Class<IntegrationTemplate> resourceClass() {
        return IntegrationTemplate.class;
    }

    @Override
    public String resourceKind() {
        return IntegrationTemplate.KIND;
    }

}
