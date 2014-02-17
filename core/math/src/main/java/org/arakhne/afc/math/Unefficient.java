/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When this annotation is present, the function is
 * assumed to be not optimized, according to the current
 * literature knowledge.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.SOURCE)
public @interface Unefficient {
	
	/** Defines the documentation sources from
	 * which information may be found to improve
	 * the function.
	 * @return the sources
	 */
	String[] sources() default {};
	
}