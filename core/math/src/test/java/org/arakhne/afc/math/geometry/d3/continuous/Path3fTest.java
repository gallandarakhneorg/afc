/* 
 * $Id$
 * 
 * Copyright (C) 2015 Hamza JAFFALI.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math.geometry.d3.continuous;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.junit.Test;

/**
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("static-method")
public class Path3fTest extends AbstractMathTestCase {
	
	
	
	/** Testing the intersection between the path and an AlignedBox3f
     */
	@Test
    public void intersectsAlignedBox() {
		Path3f r = new Path3f();
		r.moveTo(0f, 0f, 0f);
		r.lineTo(1f, 1f, 1f);
		r.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		r.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		r.closePath();
		
		AlignedBox3f aB = new AlignedBox3f(new Point3f(0f,0f,0f),new Point3f(2f,2f,2f));
		AlignedBox3f aB2 = new AlignedBox3f(new Point3f(-1f,-1f,-1f),new Point3f(-2f,-2f,-2f));
		
		assertTrue(r.intersects(aB));
		assertFalse(r.intersects(aB2));
    }

}
