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
package fr.utbm.set.jasim.environment.interfaces.body;

import java.util.ArrayList;

import fr.utbm.set.collection.ArrayUtil;
import fr.utbm.set.geom.PseudoHamelDimension;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception1D;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception1D5;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception2D;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception3D;
import fr.utbm.set.jasim.environment.semantics.BodyType;
import fr.utbm.set.jasim.environment.semantics.ObjectType;

/**
 * Utility functions for all agent bodies.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AgentBodyUtil {
	
	/** Extract the body type for a list of semantics.
	 * 
	 * @param types are the types to filter.
	 * @return the main BodyType extracted from the given list of types.
	 * @throws IllegalArgumentException when no BodyType was found.
	 */
	public static BodyType extractBodyType(ObjectType... types) {
		for(ObjectType type : types) {
			if (type instanceof BodyType) {
				return (BodyType)type;
			}
		}
		throw new IllegalArgumentException("no BodyType find in the list of semantics"); //$NON-NLS-1$
	}

	/** Remove the body type from a list of semantics.
	 * 
	 * @param types are the types to filter.
	 * @return the list of semantics without a BodyType.
	 * @throws IllegalArgumentException when no BodyType was found.
	 */
	public static ObjectType[] removeBodyType(ObjectType... types) {
		ArrayList<ObjectType> list = new ArrayList<ObjectType>(types.length);
		boolean found = false;
		for(ObjectType type : types) {
			if (!(type instanceof BodyType) || found) {
				list.add(type);
				found = true;
			}
		}
		return ArrayUtil.toArray(list, ObjectType.class);
	}

	/** Replies the dimension of a perception.
	 * 
	 * @param perceptionType
	 * @return the dimension
	 * @throws IllegalArgumentException if the given type is not supported
	 */
	public static PseudoHamelDimension toDimension(Class<? extends Perception> perceptionType) {
		if (Perception3D.class.isAssignableFrom(perceptionType))
			return PseudoHamelDimension.DIMENSION_3D;
		if (Perception2D.class.isAssignableFrom(perceptionType))
			return PseudoHamelDimension.DIMENSION_2D;
		if (Perception1D5.class.isAssignableFrom(perceptionType))
			return PseudoHamelDimension.DIMENSION_1D5;
		if (Perception1D.class.isAssignableFrom(perceptionType))
			return PseudoHamelDimension.DIMENSION_1D;
		throw new IllegalArgumentException();
	}

	/** Replies the perception type for a dimension.
	 * 
	 * @param dimension
	 * @return the perception type
	 * @throws IllegalArgumentException if the given type is not supported
	 */
	public static Class<? extends Perception> toPerceptionType(PseudoHamelDimension dimension) {
		switch(dimension) {
		case DIMENSION_1D:
			return Perception1D.class;
		case DIMENSION_1D5:
			return Perception1D5.class;
		case DIMENSION_2D:
			return Perception2D.class;
		case DIMENSION_2D5:
		case DIMENSION_3D:
			return Perception3D.class;
		default:
		}
		throw new IllegalArgumentException();
	}

}
