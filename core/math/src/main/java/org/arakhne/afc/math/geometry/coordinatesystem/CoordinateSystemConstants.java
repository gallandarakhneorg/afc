/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.coordinatesystem;

/**
 * Represents the different 2D/3D referencials
 * used in different domains.
 *
 * @author $Author: cbohrhauer$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public final class CoordinateSystemConstants {

	/** Replies the preferred coordinate system for
	 * <a href="http://en.wikipedia.org/wiki/Geographic_information_system">Geographical
	 * Information System</a> (GIS).
	 *
	 * <p>GIS uses {@link CoordinateSystem2D#XY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D GIS_2D = CoordinateSystem2D.XY_RIGHT_HAND;

	/** Replies the preferred coordinate system for
	 * <a href="http://en.wikipedia.org/wiki/Geographic_information_system">Geographical
	 * Information System</a> (GIS).
	 *
	 * <p>GIS uses {@link CoordinateSystem3D#XYZ_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D GIS_3D = CoordinateSystem3D.XYZ_RIGHT_HAND;

	/** Replies the preferred coordinate system for simulation spaces.
	 *
	 * <p>Simulation use {@link CoordinateSystem2D#XY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D SIMULATION_2D = CoordinateSystem2D.XY_RIGHT_HAND;

	/** Replies the preferred coordinate system for simulation spaces.
	 *
	 * <p>Simulation use {@link CoordinateSystem3D#XYZ_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D SIMULATION_3D = CoordinateSystem3D.XYZ_RIGHT_HAND;

	/** Replies the preferred coordinate system for default 3DSMAX modelers.
	 *
	 * <p>3DSMAX uses the {@link CoordinateSystem2D#XY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D MODEL_3DMAX_2D = CoordinateSystem2D.XY_RIGHT_HAND;

	/** Replies the preferred coordinate system for default 3DSMAX modelers.
	 *
	 * <p>3DSMAX uses the {@link CoordinateSystem3D#XYZ_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D MODEL_3DMAX_3D = CoordinateSystem3D.XYZ_RIGHT_HAND;

	/** Replies the preferred coordinate system for default DirectX viewers.
	 *
	 * <p>DirectX uses the {@link CoordinateSystem2D#XY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D DIRECTX_2D = CoordinateSystem2D.XY_RIGHT_HAND;

	/** Replies the preferred coordinate system for default DirectX viewers.
	 *
	 * <p>DirectX uses the {@link CoordinateSystem3D#XZY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D DIRECTX_3D = CoordinateSystem3D.XZY_LEFT_HAND;

	/** Replies the preferred coordinate system for default Java3D viewers.
	 *
	 * <p>Java3D uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D JAVA3D_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for default Java3D viewers.
	 *
	 * <p>Java3D uses the {@link CoordinateSystem3D#XZY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D JAVA3D_3D = CoordinateSystem3D.XZY_RIGHT_HAND;

	/** Replies the preferred coordinate system for default OpenGL viewers.
	 *
	 * <p>OpenGL uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D OPENGL_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for default OpenGL viewers.
	 *
	 * <p>OpenGL uses the {@link CoordinateSystem3D#XZY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D OPENGL_3D = CoordinateSystem3D.XZY_RIGHT_HAND;

	/** Replies the preferred coordinate system for default X3D viewers.
	 *
	 * <p>X3D uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D X3D_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for default X3D viewers.
	 *
	 * <p>X3D uses the {@link CoordinateSystem3D#XZY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D X3D_3D = CoordinateSystem3D.XZY_RIGHT_HAND;

	/** Replies the preferred coordinate system for default NASA airplane standards.
	 *
	 * <p>NASA airplane standards use the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D NASA_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for default NASA airplane standards.
	 *
	 * <p>NASA airplane standards use the {@link CoordinateSystem3D#XYZ_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D NASA_3D = CoordinateSystem3D.XYZ_LEFT_HAND;

	/** Replies the preferred coordinate system for default Collada viewers.
	 *
	 * <p>Collada uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D COLLADA_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for default Collada viewers.
	 *
	 * <p>Collada uses the {@link CoordinateSystem3D#XZY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D COLLADA_3D = CoordinateSystem3D.XZY_LEFT_HAND;

	/** Replies the preferred coordinate system for 3DVIA Virtools.
	 *
	 * <p>3DVIA Virtools uses the {@link CoordinateSystem2D#XY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D VIRTOOLS_2D = CoordinateSystem2D.XY_RIGHT_HAND;

	/** Replies the preferred coordinate system for 3DVIA Virtools.
	 *
	 * <p>3DVIA Virtools uses the {@link CoordinateSystem3D#XZY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D VIRTOOLS_3D = CoordinateSystem3D.XZY_LEFT_HAND;

	/** Replies the preferred coordinate system for Maya modeller.
	 *
	 * <p>Maya uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D MAYA_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for Maya modeler.
	 *
	 * <p>Maya uses the {@link CoordinateSystem3D#XZY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D MAYA_3D = CoordinateSystem3D.XZY_RIGHT_HAND;

	/** Replies the preferred coordinate system for Unity 3D modeller.
	 *
	 * <p>Unity 3D uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D UNITY3D_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for Unity 3D modeler.
	 *
	 * <p>Unity 3D uses the {@link CoordinateSystem3D#XZY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D UNITY3D_3D = CoordinateSystem3D.XZY_LEFT_HAND;

	/** Replies the preferred coordinate system for Catia V5 modeller.
	 *
	 * <p>Catia V5 uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D CATIAV5_2D = CoordinateSystem2D.XY_LEFT_HAND;

	/** Replies the preferred coordinate system for Catia V5 modeler.
	 *
	 * <p>Catia V5 uses the {@link CoordinateSystem3D#XZY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D CATIAV5_3D = CoordinateSystem3D.XZY_RIGHT_HAND;

	/** Replies the preferred coordinate system for Blender modeller.
	 *
	 * <p>Blender uses the {@link CoordinateSystem2D#XY_LEFT_HAND} coordinate system.
	 */
	public static final CoordinateSystem2D BLENDER_2D = CoordinateSystem2D.XY_RIGHT_HAND;

	/** Replies the preferred coordinate system for Blender modeler.
	 *
	 * <p>Blender uses the {@link CoordinateSystem3D#XZY_RIGHT_HAND} coordinate system.
	 */
	public static final CoordinateSystem3D BLENDER_3D = CoordinateSystem3D.XYZ_RIGHT_HAND;

	private CoordinateSystemConstants() {
		//
	}

}
