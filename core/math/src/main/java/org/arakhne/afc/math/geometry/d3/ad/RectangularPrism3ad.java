/* 
 * $Id$
 * 
 * Copyright (c) 2016, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports (SET)
 * of Universite de Technologie de Belfort-Montbeliard.
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SET.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.geometry.d3.ad;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

/**
 * TODO
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface RectangularPrism3ad<
			ST extends Shape3ad<?, ?, IE, P, V, B>,
			IT extends RectangularPrism3ad<?, ?, IE, P, V, B>,
			IE extends PathElement3ad,
			P extends Point3D<? super P, ? super V>,
			V extends Vector3D<? super V, ? super P>,
			B extends RectangularPrism3ad<ST, IT, IE, P, V, B>> extends Shape3ad<ST, IT, IE, P, V, B> {

}
