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
package com.redhat.ipaas.dao.init;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.ipaas.model.Kind;
import com.redhat.ipaas.model.ToJson;

/**
 * Used to read the deployment.json file from the client GUI project
 *
 */
public class ModelData implements ToJson {

	private Kind kind;
	private Object data;

    public ModelData() {
        super();
    }

	public ModelData(Kind kind, Object data) {
		super();
		this.kind = kind;
		this.data = data;
	}


	public Kind getKind() {
		return kind;
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}

	@JsonRawValue
	public String getData() {
		return data == null ? null : data.toString();
	}

	public void setData(JsonNode data) {
		this.data = data;
	}
}
