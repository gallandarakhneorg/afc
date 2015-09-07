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
package org.arakhne.afc.math.geometry.d3.splineold;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.arakhne.afc.math.geometry.d3.Point3D;

/**
 * This class implements the Lagrange algorithm.
 * This Lagrange interpolation is based on the Aitken algorithm:
 * <code>Pij = Pij-1 (ti+j - t) / (ti+j - ti) + Pi+1j-1 (t - ti) / (ti+j - ti) ,
 * j =[1, n]     i = [0, n-j]</code> 
 *
 * @author $Author: nvieval$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class LagrangeSpline extends AbstractSpline<LagrangeSpline> {

	private static final long serialVersionUID = 8222496891349310032L;

	/**
	 */
	public LagrangeSpline() {
		//
	}

	/**
	 * @param points the control points of the spline.
	 */
	public LagrangeSpline(Point3D... points) {
		super(points);
	}

	/**
	 * @param points the control points of the spline.
	 */
	public LagrangeSpline(Iterable<? extends Point3D> points) {
		super(points);
	}

    /**
     * Change the control points and recompute the spline's points.
     *
     * @param controlPoints the control points.
     */
    public void setControlPoints(Iterable<? extends Point3D> controlPoints) {
    	this.controlPoints.clear();
    	for (Point3D p : controlPoints) {
    		this.controlPoints.add(p.clone());
    	}
    	reset();
    }
    
    /**
     * Change the control points and recompute the spline's points.
     *
     * @param controlPoints the control points.
     */
    public void setControlPoints(Point3D... controlPoints) {
    	setControlPoints(Arrays.asList(controlPoints));
    }

    @Override
	protected void ensurePoints() {
		if (this.points == null) {
			int N = this.controlPoints.size(); // Knots count
			int Nm = N - 1;

			int totalSize = (int)Math.round(this.controlPoints.size() / getDiscretizationFactor());

			List<Point3D> res = new ArrayList<>(totalSize);

			double[] ti = new double[N];

			for (int i = 0; i < N; ++i) {
				ti[i] = (double) i / Nm;
			}

			double t, step, P;
			Point3D Pi, Pj;

			t = 0.;
			step = 1. / (totalSize - 1);

			// basic function calculation
			for (int k = 0; k < totalSize; ++k) {
				Pi = this.controlPoints.get(0).clone();
				Pi.set(0, 0, 0);

				for (int j = 0; j < N; ++j) {
					Pj = this.controlPoints.get(j);
					P = 1;
					for (int i = 0; i < N; ++i) {
						if (i != j) {
							P = P * (t - ti[i]) / (ti[j] - ti[i]);
						}
					}

					Pi.set(
							Pi.getX() + (Pj.getX() * P),
							Pi.getY() + (Pj.getY() * P),
							Pi.getZ() + (Pj.getZ() * P));
				}

				res.add(Pi);

				t += step;
			}

			assert(res.size() == totalSize);

			this.points = res;
		}
	}


}
