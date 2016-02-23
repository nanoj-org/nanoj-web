package org.nanoj.web.tinymvc.provider ;

import org.nanoj.injector.Injector;
import org.nanoj.injector.InjectorFactory;
import org.nanoj.web.tinymvc.TinyMvcException;

/**
 * Utility class 
 * 
 * @author Laurent Guerin
 *
 */
public class InstanceProvider {

	private final static Injector injector = InjectorFactory.createInjector("tiny-mvc-injector");

	/**
	 * Uses the current class loader to load the given class
	 * @param className
	 * @return
	 */
	private final static Class<?> loadClass(String className ) {
		if ( className != null ) { 
			//--- Try to load the class
			ClassLoader classLoader = InstanceProvider.class.getClassLoader() ;
			Class<?> clazz = null ;
			try {
				clazz = classLoader.loadClass( className ) ;
			} catch (ClassNotFoundException e) {
				throw new TinyMvcException("Cannot load class '" + className + "' (class not found)" );
			}
			return clazz ;
		}
		else {
			throw new TinyMvcException("Class name is null" );
		}
	}
	
	/**
	 * Creates an instance of the given class name and check it's a subclass of the given super class
	 * @param className
	 * @param superClass
	 * @return
	 */
	public final static <T> T createInstance(String className, Class<T> superClass ) {
		
		if ( null == superClass ) {
			throw new TinyMvcException("Super class is null" );
		}
		
		Class<?> clazz = loadClass(className) ;
		
		return createInstance(clazz, superClass );
	}

	/**
	 * Creates an instance of the given class only if it's a subclass of the given super class
	 * @param clazz
	 * @param superClass
	 * @return
	 */
	//@SuppressWarnings("unchecked")
	public final static <T> T createInstance(Class<?> clazz, Class<T> superClass ) {
		
		if ( superClass.isAssignableFrom(clazz) ) {
			@SuppressWarnings("unchecked")
			T instance = (T) createInstance( clazz ) ;
			return instance ;
		}
		else {
			throw new TinyMvcException("Class '" + clazz.getCanonicalName() + "' doesn't extend/implement '" 
				+ superClass.getCanonicalName() + "'" );
		}
	}

	/**
	 * Creates an instance of the given class
	 * @param clazz
	 * @return
	 */
	public final static <T> T createInstance( Class<T> clazz ) {
//		if ( clazz != null ) { 
//			T instance = null ;
//			try {
//				instance = clazz.newInstance();
//			} catch (InstantiationException e) {
//				throw new TinyMvcException("Cannot create " + clazz.getCanonicalName() + " instance (InstantiationException)", e );
//			} catch (IllegalAccessException e) {
//				throw new TinyMvcException("Cannot create " + clazz.getCanonicalName() + " instance (IllegalAccessException)", e );
//			}
//			return instance ;
//		}
//		else {
//			throw new TinyMvcException("Class is null" );
//		}
		
		return injector.getInstance(clazz);
	}
	
}
