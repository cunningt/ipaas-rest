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
package com.redhat.ipaas.rest.v1.operations;

import javax.ws.rs.*;

import com.redhat.ipaas.dao.manager.WithDataManager;
import com.redhat.ipaas.model.WithId;

public interface Deleter<T extends WithId> extends Resource<T>, WithDataManager {

    @DELETE
    @Consumes("application/json")
    @Path(value = "/{id}")
    default void delete(@PathParam("id") String id) {
        getDataManager().delete(resourceKind(), id);
    }

}
