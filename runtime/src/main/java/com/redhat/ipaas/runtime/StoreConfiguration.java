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
package com.redhat.ipaas.runtime;

import javax.sql.DataSource;

import com.redhat.ipaas.jsondb.JsonDB;
import com.redhat.ipaas.jsondb.impl.SqlJsonDB;
import org.skife.jdbi.v2.DBI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Creates and configures a DBI object
 */
@Configuration
public class StoreConfiguration {

    @Bean
    public DBI dbiBean(@Autowired DataSource dataSource) {
        DBI dbi = new DBI(dataSource);
        return dbi;
    }

    @Bean
    @Autowired
    public JsonDB realTimeDB(DBI dbi) {
        return init(new SqlJsonDB(dbi, null));
    }

    private SqlJsonDB init(SqlJsonDB jsondb) {
        try {
            jsondb.createTables();
        } catch (Exception ignore) {
        }
        return jsondb;
    }

}
