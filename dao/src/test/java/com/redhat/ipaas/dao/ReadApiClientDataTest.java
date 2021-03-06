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
package com.redhat.ipaas.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.ipaas.core.Json;
import com.redhat.ipaas.dao.init.ModelData;
import com.redhat.ipaas.dao.init.ReadApiClientData;
import com.redhat.ipaas.model.connection.Connector;
import com.redhat.ipaas.model.connection.ConnectorGroup;
import org.junit.Assert;
import org.junit.Test;


public class ReadApiClientDataTest {

    private final static ObjectMapper mapper = Json.mapper();

	@Test
	public void deserializeModelDataTest() throws IOException {

		//serialize
		ConnectorGroup cg = new ConnectorGroup.Builder().id("label").name("label").build();
		ModelData mdIn = new ModelData(ConnectorGroup.KIND, mapper.writeValueAsString(cg));
		Assert.assertEquals("{\"id\":\"label\",\"name\":\"label\"}", mdIn.getData());

		//deserialize
		String json = mapper.writeValueAsString(mdIn);
		ModelData mdOut = mapper.readValue(json, ModelData.class);
		Assert.assertEquals("{\"id\":\"label\",\"name\":\"label\"}", mdOut.getData());
	}

	@Test
	public void loadApiClientDataTest() throws IOException {
		List<ModelData> modelDataList = new ReadApiClientData().readDataFromFile("com/redhat/ipaas/dao/deployment.json");
		System.out.println("Found " + modelDataList.size() + " entities.");
		Assert.assertTrue("We should find some ModelData", 0 < modelDataList.size());
        List<Connector> connectorList = new ArrayList<>();
		for (ModelData md : modelDataList) {
			if (md.getKind().equalsIgnoreCase("connector")) {
				Connector cg = mapper.readValue(md.getData(), Connector.class);
				connectorList.add(cg);
			}
		}
		System.out.println("Found " + connectorList.size() + " Connectors");
		Assert.assertTrue("We should find some Connectors", 0 < connectorList.size());
	}

}
