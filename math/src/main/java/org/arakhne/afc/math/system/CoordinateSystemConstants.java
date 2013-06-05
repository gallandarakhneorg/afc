/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.system;

/**
 * Represents the different 2D/3D referencials
 * used in different domains.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CoordinateSystemConstants {
	
	private CoordinateSystemConstants() {
		//
	}

	/** Replies the preferred coordinate system for
	 * <a href='http://en.wikipedia.org/wiki/Geographic_information_system">Geographical 
	 * Information System</a> (GIS).
	 * <p>
	 * GIS uses {@link CoordinateSystem2D#XY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D GIS_2D = CoordinateSystem2D.XY_RIGHT_HAND;

	/** Replies the preferred coordinate system for
	 * <a href='http://en.wikipedia.org/wiki/Geographic_information_system">Geographical 
	 * Information System</a> (GIS).
	 * <p>
	 * GIS uses {@link CoordinateSystem3D#XYZ_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D GIS_3D = CoordinateSystem3D.XYZ_RIGHT_HAND;

	/** Replies the preferred coordinate system for
	 * <a href='http://en.wikipedia.org/wiki/Geographic_information_system">Geographical 
	 * Information System</a> (GIS) with a
	 * <a href="http://en.wikipedia.org/wiki/Map_projection">conical map projection</a>.
	 * @deprecated see {@link #GIS_2D}
	 */
	@Deprecated
	public static final CoordinateSystem2D CONICAL_GIS_2D = GIS_2D;

	/** Replies the preferred coordinate system for
	 * <a href='http://en.wikipedia.org/wiki/Geographic_information_system">Geographical 
	 * Information System</a> (GIS) with a
	 * <a href="http://en.wikipedia.org/wiki/Map_projection">conical map projection</a>.
	 * @deprecated see {@link #GIS_3D}
	 */
	@Deprecated
	public static final CoordinateSystem3D CONICAL_GIS_3D = GIS_3D;

	/** Replies the preferred coordinate system for simulation spaces.
	 * <p>
	 * Simulation use {@link CoordinateSystem2D#XY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D SIMULATION_2D = CoordinateSystem2D.XY_RIGHT_HAND;

	/** Replies the preferred coordinate system for simulation spaces.
	 * <p>
	 * Simulation use {@link CoordinateSystem3D#XYZ_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D SIMULATION_3D = CoordinateSystem3D.XYZ_RIGHT_HAND;

	/** Replies the preferred coordinate system for default 3DSMAX modelers.
	 * <p>
	 * 3DSMAX uses the {@link CoordinateSystem2D#XY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D MODEL_3DMAX_2D = CoordinateSystem2D.XY_RIGHT_HAND;

	/** Replies the preferred coordinate system for default 3DSMAX modelers.
	 * <p>
	 * 3DSMAX uses the {@link CoordinateSystem3D#XYZ_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D MODEL_3DMAX_3D = CoordinateSystem3D.XYZ_RIGHT_HAND;

	/** Replies the preferred coordinate system for default DirectX viewers.
	 * <p>
	 * DirectX uses the {@link CoordinateSystem2D#XY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D DIRECTX_2D = CoordinateSystem2D.XY_RIGHT_HAND;

	/** Replies the preferred coordinate system for default DirectX viewers.
	 * <p>
	 * DirectX uses the {@link CoordinateSystem3D#XZY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D DIRECTX_3D = CoordinateSystem3D.XZY_LEFT_HAND;

	/** Replies the preferred coordinate system for default Java3D viewers.
	 * <p>
	 * Java3D uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D JAVA3D_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for default Java3D viewers.
	 * <p>
	 * Java3D uses the {@link CoordinateSystem3D#XZY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D JAVA3D_3D = CoordinateSystem3D.XZY_RIGHT_HAND;

	/** Replies the preferred coordinate system for default OpenGL viewers.
	 * <p>
	 * OpenGL uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D OPENGL_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for default OpenGL viewers.
	 * <p>
	 * OpenGL uses the {@link CoordinateSystem3D#XZY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D OPENGL_3D = CoordinateSystem3D.XZY_RIGHT_HAND;

	/** Replies the preferred coordinate system for default X3D viewers.
	 * <p>
	 * X3D uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D X3D_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for default X3D viewers.
	 * <p>
	 * X3D uses the {@link CoordinateSystem3D#XZY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D X3D_3D = CoordinateSystem3D.XZY_RIGHT_HAND;

	/** Replies the preferred coordinate system for default NASA airplane standards.
	 * <p>
	 * NASA airplane standards use the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D NASA_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for default NASA airplane standards.
	 * <p>
	 * NASA airplane standards use the {@link CoordinateSystem3D#XYZ_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D NASA_3D = CoordinateSystem3D.XYZ_LEFT_HAND;

	/** Replies the preferred coordinate system for default Collada viewers.
	 * <p>
	 * Collada uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D COLLADA_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for default Collada viewers.
	 * <p>
	 * Collada uses the {@link CoordinateSystem3D#XZY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D COLLADA_3D = CoordinateSystem3D.XZY_LEFT_HAND;

	/** Replies the preferred coordinate system for 3DVIA Virtools.
	 * <p>
	 * 3DVIA Virtools uses the {@link CoordinateSystem2D#XY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D VIRTOOLS_2D = CoordinateSystem2D.XY_RIGHT_HAND;

	/** Replies the preferred coordinate system for 3DVIA Virtools.
	 * <p>
	 * 3DVIA Virtools uses the {@link CoordinateSystem3D#XZY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D VIRTOOLS_3D = CoordinateSystem3D.XZY_LEFT_HAND;

	/** Replies the preferred coordinate system for Maya modeller.
	 * <p>
	 * Maya uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D MAYA_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for Maya modeler.
	 * <p>
	 * Maya uses the {@link CoordinateSystem3D#XZY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D MAYA_3D = CoordinateSystem3D.XZY_RIGHT_HAND;

	/** Replies the preferred coordinate system for Unity 3D modeller.
	 * <p>
	 * Unity 3D uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D UNITY3D_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for Unity 3D modeler.
	 * <p>
	 * Unity 3D uses the {@link CoordinateSystem3D#XZY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D UNITY3D_3D = CoordinateSystem3D.XZY_LEFT_HAND;

	/** Replies the preferred coordinate system for Catia V5 modeller.
	 * <p>
	 * Catia V5 uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D CATIAV5_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for Catia V5 modeler.
	 * <p>
	 * Catia V5 uses the {@link CoordinateSystem3D#XZY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D CATIAV5_3D = CoordinateSystem3D.XZY_RIGHT_HAND;

	/** Replies the preferred coordinate system for Blender modeller.
	 * <p>
	 * Blender uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D BLENDER_2D = CoordinateSystem2D.XY_RIGHT_HAND;

	/** Replies the preferred coordinate system for Blender modeler.
	 * <p>
	 * Blender uses the {@link CoordinateSystem3D#XZY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D BLENDER_3D = CoordinateSystem3D.XYZ_RIGHT_HAND;

}
