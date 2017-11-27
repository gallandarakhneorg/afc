/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.jasim.environment.semantics;

import java.lang.reflect.Field;

/** This class permits to create semantic objects.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SemanticFactory {

	/** Create an instance of the specified semantic.
	 * 
	 * @param <S> is the type of the semantic to create.
	 * @param semantic is the type of the semantic to create.
	 * @return the new instance or <code>null</code> if the specified type could not
	 * be created.
	 */
	public static <S extends Semantic> S newInstance(Class<S> semantic) {
		String name;
		for(Field field : semantic.getDeclaredFields()) {
			try {
				name = field.getName();
				if (name!=null && name.endsWith("SINGLETON") && //$NON-NLS-1$
						semantic.isAssignableFrom(field.getType())) {
						return semantic.cast(field.get(null));
					}
			}
			catch(Throwable _) {
				//
			}
		}
		try{
			return semantic.newInstance();
		}
		catch(Throwable _) {
			return null;
		}
	}

}