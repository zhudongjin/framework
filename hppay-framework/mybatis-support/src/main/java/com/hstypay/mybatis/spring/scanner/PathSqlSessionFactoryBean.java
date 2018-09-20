package com.hstypay.mybatis.spring.scanner;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathSqlSessionFactoryBean extends SqlSessionFactoryBean {

	private static final Logger logger = LoggerFactory.getLogger(PathSqlSessionFactoryBean.class);

	static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

	private Environment environment;

	public PathSqlSessionFactoryBean() {
		this(new StandardEnvironment());
	}

	public PathSqlSessionFactoryBean(Environment environment) {
		this.environment = environment;
	}

	protected String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(this.environment.resolveRequiredPlaceholders(basePackage));
	}

	protected List<Class<?>> doScan(String basePackage) {
		List<Class<?>> clazzes = new ArrayList<Class<?>>();
		try {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage) + "/" + DEFAULT_RESOURCE_PATTERN;
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			for (Resource resource : resources) {
				if (logger.isTraceEnabled()) {
					logger.trace("Scanning " + resource);
				}
				if (resource.isReadable()) {
					MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
					String className = metadataReader.getClassMetadata().getClassName();
					try {
						Class<?> clazz = Class.forName(className);
						clazzes.add(clazz);
					} catch (ClassNotFoundException e) {
						if (logger.isTraceEnabled()) {
							logger.trace("class not found for " + className, e);
						}
					}
				} else {
					if (logger.isTraceEnabled()) {
						logger.trace("Ignored because not readable: " + resource);
					}
				}
			}
		} catch (IOException e) {
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", e);
		}

		return clazzes;
	}

	@Override
	public void setTypeAliasesPackage(String typeAliasesPackage) {
		if (org.apache.commons.lang3.StringUtils.isBlank(typeAliasesPackage) || !typeAliasesPackage.contains("*")) {
			super.setTypeAliasesPackage(typeAliasesPackage);
			return;
		}
		String[] scanPackageArray = StringUtils.tokenizeToStringArray(typeAliasesPackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
		List<Class<?>> scanClazzes = new ArrayList<Class<?>>();
		for (String basePackage : scanPackageArray) {
			List<Class<?>> clazzes = this.doScan(basePackage);
			if (null != clazzes && clazzes.size() > 0) {
				scanClazzes.addAll(clazzes);
			}
		}
		super.setTypeAliasesPackage(null);
		super.setTypeAliases(scanClazzes.toArray(new Class<?>[scanClazzes.size()]));
	}

	public void setEnvironment(Environment environment) {
		Assert.notNull(environment, "Environment must not be null");
		this.environment = environment;
	}

	public Environment getEnvironment() {
		return this.environment;
	}
}
