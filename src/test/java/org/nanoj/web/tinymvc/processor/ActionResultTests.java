package org.nanoj.web.tinymvc.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.junit.Test;
import org.nanoj.web.tinymvc.config.Configuration;

public class ActionResultTests {
	
	private Configuration getConfiguration() {
		Properties p = new Properties();
		return new Configuration(p);
	}

	@Test
	public void test1() {
		ActionResult ar = new ActionResult("aaa", getConfiguration());
		assertEquals("aaa", ar.getView());
		assertEquals("/WEB-INF/views/aaa.jsp", ar.getViewFullPath());
		assertNull(ar.getLayout());
		assertNull(ar.getLayoutFullPath());
		assertEquals( ar.getViewFullPath(), ar.getTargetFullPath() );
	}
	
	@Test
	public void test2() {
		ActionResult ar = new ActionResult(" aaa ", getConfiguration());
		assertEquals("aaa", ar.getView());
		assertEquals("/WEB-INF/views/aaa.jsp", ar.getViewFullPath());
		assertNull(ar.getLayout());
		assertNull(ar.getLayoutFullPath());
		assertEquals( ar.getViewFullPath(), ar.getTargetFullPath() );
	}

	@Test
	public void test2bis() {
		ActionResult ar = new ActionResult(" aaa : ", getConfiguration());
		assertEquals("aaa", ar.getView());
		assertEquals("/WEB-INF/views/aaa.jsp", ar.getViewFullPath());
		assertNull(ar.getLayout());
		assertNull(ar.getLayoutFullPath());
		assertEquals( ar.getViewFullPath(), ar.getTargetFullPath() );
	}

	@Test
	public void test3() {
		ActionResult ar = new ActionResult("aaa:bbb", getConfiguration());
		assertEquals("aaa", ar.getView());
		assertEquals("/WEB-INF/views/aaa.jsp", ar.getViewFullPath());
		assertEquals("bbb", ar.getLayout());
		assertEquals("/WEB-INF/layouts/bbb.jsp", ar.getLayoutFullPath());
		assertEquals( ar.getLayoutFullPath(), ar.getTargetFullPath() );
	}

	@Test
	public void test4() {
		ActionResult ar = new ActionResult("  aaa:bbb  ", getConfiguration());
		assertEquals("aaa", ar.getView());
		assertEquals("/WEB-INF/views/aaa.jsp", ar.getViewFullPath());
		assertEquals("bbb", ar.getLayout());
		assertEquals("/WEB-INF/layouts/bbb.jsp", ar.getLayoutFullPath());
		assertEquals( ar.getLayoutFullPath(), ar.getTargetFullPath() );
	}

	@Test
	public void test5() {
		ActionResult ar = new ActionResult("  aaa  :  bbb  ", getConfiguration());
		assertEquals("aaa", ar.getView());
		assertEquals("/WEB-INF/views/aaa.jsp", ar.getViewFullPath());
		assertEquals("bbb", ar.getLayout());
		assertEquals("/WEB-INF/layouts/bbb.jsp", ar.getLayoutFullPath());
		assertEquals( ar.getLayoutFullPath(), ar.getTargetFullPath() );
	}

	@Test(expected=IllegalArgumentException.class)
	public void test6() {
		new ActionResult(":", getConfiguration());
	}

	@Test(expected=IllegalArgumentException.class)
	public void test7() {
		new ActionResult(null, getConfiguration());
	}

	@Test(expected=IllegalArgumentException.class)
	public void test8() {
		new ActionResult("", getConfiguration());
	}
}
