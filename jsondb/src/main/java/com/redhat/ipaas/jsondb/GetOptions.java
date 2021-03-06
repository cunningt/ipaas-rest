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
package com.redhat.ipaas.jsondb;

import java.io.Serializable;
import java.util.Optional;

import org.immutables.value.Value;

/**
 * Options that can be configured on the {@link JsonDB#getAsString(String, GetOptions)}.
 */
@Value.Immutable
public interface GetOptions extends Serializable {

    Optional<Boolean> prettyPrint();
    GetOptions withPrettyPrint(boolean value);

// TODO: implement these later.
//
//    Optional<Boolean> asList();
//    GetOptions withAsList(boolean value);
//
//    Optional<Boolean> shallow();
//    GetOptions withShallow(boolean value);

    Optional<String> callback();
    GetOptions withCallback(String value);

    public static ImmutableGetOptions.Builder builder() {
        return ImmutableGetOptions.builder();
    }
}
