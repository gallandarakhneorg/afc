/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3.d;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.afp.AbstractQuaternion3afpTest;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class Quaternion4dTest extends AbstractQuaternion3afpTest<Quaternion4d> {

	@Override
	protected Quaternion4d createQuaternion(double x, double y, double z, double w) {
		return new Quaternion4d(x, y, z, w);
	}

	@Override
	protected Quaternion4d createAxisAngle(double x, double y, double z, double angle) {
		return Quaternion4d.newAxisAngle(x, y, z, angle);
	}

	@Override
	protected Quaternion4d createEulerAngles(double attitude, double bank, double heading) {
		return Quaternion4d.newEulerAngles(attitude, bank, heading, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

}
