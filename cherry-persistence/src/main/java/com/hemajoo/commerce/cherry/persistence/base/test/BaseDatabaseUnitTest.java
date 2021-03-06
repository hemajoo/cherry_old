/*
 * Copyright(c) 2021 Hemajoo Digital Systems Inc.
 * --------------------------------------------------------------------------------------
 * This file is part of Hemajoo Systems Inc. projects which is licensed
 * under the Apache license version 2 and use is subject to license terms.
 * You should have received a copy of the license with the project's artifact
 * binaries and/or sources.
 *
 * License can be consulted at http://www.apache.org/licenses/LICENSE-2.0
 * --------------------------------------------------------------------------------------
 */
package com.hemajoo.commerce.cherry.persistence.base.test;

import com.hemajoo.commerce.cherry.persistence.content.DocumentRepository;
import com.hemajoo.commerce.cherry.persistence.content.DocumentService;
import com.hemajoo.commerce.cherry.persistence.content.DocumentStore;
import lombok.Getter;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.ressec.avocado.core.junit.BaseUnitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

/**
 * Base unit test for database test classes.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public abstract class BaseDatabaseUnitTest extends BaseUnitTest
{
    /**
     * Is the database initialized?
     */
    protected static boolean IS_DATABASE_INITIALIZED = false;

    /**
     * Document persistence service.
     */
    @Autowired
    protected DocumentService documentService;

    /**
     * Document content store.
     */
    @Autowired
    protected DocumentStore documentStore;

    /**
     * Document repository.
     */
    @Autowired
    protected DocumentRepository documentRepository;

    @Getter
    @Value("${hemajoo.commerce.cherry.store.location}")
    protected String baseContentStoreLocation;

    /**
     * Database data source.
     */
    @Value("${spring.datasource.url}")
    private String datasource;

    /**
     * Database user name.
     */
    @Value("${spring.datasource.username}")
    private String user;

    /**
     * Database user password.
     */
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * Database {@code Flyway} locations.
     */
    @Value("${spring.flyway.locations}")
    private String locations;

    /**
     * {@code Flyway} mode.
     */
    @Value("${spring.flyway.enabled}")
    private boolean enabled;

    /**
     * Database default schema to use for the tests.
     */
    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String schemas;

    protected void initializeDatabase()
    {
        if (enabled)
        {
            if (!IS_DATABASE_INITIALIZED)
            {
                // Ensure the cherry H2 db is available and up to date for the tests.
                Flyway flyway = Flyway.configure()
                        .dataSource(datasource,user, password)
                        .schemas(schemas)
                        .locations(locations)
                        .load();

                flyway.clean();
                flyway.migrate();

                IS_DATABASE_INITIALIZED = true;
            }
        }
    }

    @BeforeEach
    protected void setUp() throws IOException
    {
        initializeDatabase();
    }
}
