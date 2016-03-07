package org.nanoj.web.tinymvc.env;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.junit.Test;
import org.nanoj.web.tinymvc.config.Configuration;
import org.nanoj.web.tinymvc.env.ActionView;

public class ActionViewTests {
	
	private Configuration getConfiguration() {
		Properties p = new Properties();
		return new Configuration(p);
	}

	@Test
	public void test1() {
		ActionView ar = new ActionView("aaa", getConfiguration());
		assertEquals("aaa", ar.getPageName());
		assertEquals("/WEB-INF/views/aaa.jsp", ar.getPagePath());
		assertNull(ar.getLayoutName());
		assertNull(ar.getLayoutPath());
		assertEquals( ar.getPagePath(), ar.getViewPath() );
	}
	
	@Test
	public void test2() {
		ActionView ar = new ActionView(" aaa ", getConfiguration());
		assertEquals("aaa", ar.getPageName());
		assertEquals("/WEB-INF/views/aaa.jsp", ar.getPagePath());
		assertNull(ar.getLayoutName());
		assertNull(ar.getLayoutPath());
		assertEquals( ar.getPagePath(), ar.getViewPath() );
	}

	@Test
	public void test2bis() {
		ActionView ar = new ActionView(" aaa : ", getConfiguration());
		assertEquals("aaa", ar.getPageName());
		assertEquals("/WEB-INF/views/aaa.jsp", ar.getPagePath());
		assertNull(ar.getLayoutName());
		assertNull(ar.getLayoutPath());
		assertEquals( ar.getPagePath(), ar.getViewPath() );
	}

	@Test
	public void test3() {
		ActionView ar = new ActionView("aaa:bbb", getConfiguration());
		assertEquals("aaa", ar.getPageName());
		assertEquals("/WEB-INF/views/aaa.jsp", ar.getPagePath());
		assertEquals("bbb", ar.getLayoutName());
		assertEquals("/WEB-INF/layouts/bbb.jsp", ar.getLayoutPath());
		assertEquals( ar.getLayoutPath(), ar.getViewPath() );
	}

	@Test
	public void test4() {
		ActionView ar = new ActionView("  aaa:bbb  ", getConfiguration());
		assertEquals("aaa", ar.getPageName());
		assertEquals("/WEB-INF/views/aaa.jsp", ar.getPagePath());
		assertEquals("bbb", ar.getLayoutName());
		assertEquals("/WEB-INF/layouts/bbb.jsp", ar.getLayoutPath());
		assertEquals( ar.getLayoutPath(), ar.getViewPath() );
	}

	@Test
	public void test5() {
		ActionView ar = new ActionView("  aaa  :  bbb  ", getConfiguration());
		assertEquals("aaa", ar.getPageName());
		assertEquals("/WEB-INF/views/aaa.jsp", ar.getPagePath());
		assertEquals("bbb", ar.getLayoutName());
		assertEquals("/WEB-INF/layouts/bbb.jsp", ar.getLayoutPath());
		assertEquals( ar.getLayoutPath(), ar.getViewPath() );
	}

	@Test(expected=IllegalArgumentException.class)
	public void test6() {
		new ActionView(":", getConfiguration());
	}

	@Test(expected=IllegalArgumentException.class)
	public void test7() {
		new ActionView(null, getConfiguration());
	}

	@Test(expected=IllegalArgumentException.class)
	public void test8() {
		new ActionView("", getConfiguration());
	}
}
