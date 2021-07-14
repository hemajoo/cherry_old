/*
 * (C) Copyright Hemajoo Systems Inc. 2021 - All Rights Reserved
 * -----------------------------------------------------------------------------------------------
 * All information contained herein is, and remains the property of
 * Hemajoo Inc. and its suppliers, if any. The intellectual and technical
 * concepts contained herein are proprietary to Hemajoo Inc. and its
 * suppliers and may be covered by U.S. and Foreign Patents, patents
 * in process, and are protected by trade secret or copyright law.
 *
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained from
 * Hemajoo Systems Inc.
 * -----------------------------------------------------------------------------------------------
 */
package com.hemajoo.commerce.cherry.persistence.test.configuration;

import com.hemajoo.commerce.cherry.commons.exception.ContentStoreException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.content.fs.io.FileSystemResourceLoader;
import org.springframework.context.annotation.*;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

/**
 * A {@code Spring} configuration containing definitions for the persistence layer for the {@code test} environment.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@Configuration
@Profile("test")
@ActiveProfiles("test")
@PropertySource("classpath:cherry-test.properties")
@ComponentScan(basePackages = "com.hemajoo.commerce.cherry.persistence.*")
@EnableJpaRepositories(basePackages = "com.hemajoo.commerce.cherry.persistence.*")
@EntityScan(basePackages = "com.hemajoo.commerce.cherry.persistence.*")
@EnableFilesystemStores(basePackages = "com.hemajoo.commerce.cherry.persistence.*")
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class TestPersistenceConfiguration
{
    @Getter
    @Value("${hemajoo.commerce.cherry.store.location}")
    private String baseContentStoreLocation;

    @Bean
    public AuditorAware<String> auditorProvider()
    {
        return new TestJpaAuditor();
    }

    /**
     * Base file base path for the content store.
     * @return File base path.
     * @throws ContentStoreException Raised if required content store properties are not defined!
     */
    @Bean
    public File fileSystemRoot() throws ContentStoreException
    {
        if (baseContentStoreLocation == null || baseContentStoreLocation.isEmpty())
        {
            throw new ContentStoreException("Property: 'hemajoo.commerce.cherry.store.location' cannot be null or empty!");
        }

        // Clear the content store for a test environment
        Arrays.stream(
                Objects.requireNonNull(
                        new File(baseContentStoreLocation).listFiles())).forEach(File::delete);

        return new File(baseContentStoreLocation);
    }

    /**
     * Returns the content store file system resource loader.
     * @return File system resource loader.
     * @throws ContentStoreException Raised if required content store properties are not defined!
     */
    @Bean
    FileSystemResourceLoader fileSystemResourceLoader() throws ContentStoreException
    {
        return new FileSystemResourceLoader(fileSystemRoot().getAbsolutePath());
    }
}