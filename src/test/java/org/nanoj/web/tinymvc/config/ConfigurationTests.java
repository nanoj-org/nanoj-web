package org.nanoj.web.tinymvc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.junit.Test;

public class ConfigurationTests {
	
	private Properties getVoidProperties() {
		Properties p = new Properties();
		return p;
	}
	private Properties getProperties1() {
		Properties p = new Properties();
		p.setProperty(Configuration.ACTIONS_PATTERN, "/action/*");
		p.setProperty(Configuration.ACTIONS_PACKAGE, "org.demo.web.actions");
		return p;
	}
	private Properties getProperties2() {
		Properties p = new Properties();
		p.setProperty(Configuration.VIEWS_SUFFIX,   "foo"); // without "." --> ".foo"
		p.setProperty(Configuration.VIEWS_FOLDER,   "/WEB-INF/foo"); // without "/" --> "/WEB-INF/foo/"
		p.setProperty(Configuration.LAYOUTS_SUFFIX, "bar"); // without "." --> ".bar"
		p.setProperty(Configuration.LAYOUTS_FOLDER, "/WEB-INF/bar"); // without "/" --> "/WEB-INF/bar/"
		return p;
	}

	@Test
	public void testDefaultValues() {
		
		Configuration config = new Configuration(getVoidProperties());
		
		assertEquals(".jsp",              config.getViewsSuffix() );
		assertEquals("/WEB-INF/views/",   config.getViewsFolder() );
		
		assertEquals(".jsp",              config.getLayoutsSuffix() );
		assertEquals("/WEB-INF/layouts/", config.getLayoutsFolder() );
		
		assertEquals("welcome", config.getDefaultAction());
		assertEquals("/*", config.getActionsPattern());
		
		assertNull(config.getActionsPackage());
		assertNull(config.getActionsProviderClassName() );
	}
	
	@Test
	public void test1() {
		
		Configuration config = new Configuration(getProperties1());
		
		assertEquals(".jsp",              config.getViewsSuffix() );
		assertEquals("/WEB-INF/views/",   config.getViewsFolder() );
		
		assertEquals(".jsp",              config.getLayoutsSuffix() );
		assertEquals("/WEB-INF/layouts/", config.getLayoutsFolder() );
		
		assertEquals("welcome",   config.getDefaultAction());
		assertEquals("/action/*", config.getActionsPattern());
		assertEquals("org.demo.web.actions", config.getActionsPackage());		
		assertNull(config.getActionsProviderClassName() );
	}

	@Test
	public void test2() {
		
		Configuration config = new Configuration(getProperties2());

		assertEquals(".foo",          config.getViewsSuffix() );
		assertEquals("/WEB-INF/foo/", config.getViewsFolder() );
		
		assertEquals(".bar",          config.getLayoutsSuffix() );
		assertEquals("/WEB-INF/bar/", config.getLayoutsFolder() );
	}
}
