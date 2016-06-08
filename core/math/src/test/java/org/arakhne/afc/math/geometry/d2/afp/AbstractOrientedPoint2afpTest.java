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
package org.arakhne.afc.math.geometry.d2.afp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

@SuppressWarnings("javadoc")
public abstract class AbstractOrientedPoint2afpTest<T extends OrientedPoint2afp<?,T,?,?,?,B>,
        B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractShape2afpTest<T, B> {

    @Override
    protected T createShape() {
        return (T) createOrientedPoint(1, -2, 8, 2, -2);
    }
    
    @Test
    public void testContainsDoubleDouble(){
        assertTrue(this.shape.contains(1, -2));
        assertFalse(this.shape.contains(-2, -2));
        assertFalse(this.shape.contains(1, 8));
        assertFalse(this.shape.contains(-7, 99));
    }
    
    @Override
    @Test
    public void testClone() {
        //
    }

    @Override
    @Test
    public void equalsObject() {
        //
    }

    @Override
    @Test
    public void equalsObject_withPathIterator() {
        //
    }

    @Override
    @Test
    public void equalsToPathIterator() {}

    @Override
    @Test
    public void equalsToShape() {}

    @Override
    @Test
    public void isEmpty() {}

    @Override
    @Test
    public void clear() {}

    @Override
    @Test
    public void containsPoint2D() {}
    
    @Override
    @Test
    public void getClosestPointTo() {}
    
    @Override
    @Test
    public void getFarthestPointTo() {}

    @Override
    @Test
    public void getClosestPointToEllipse2afp() {}

    @Override
    @Test
    public void getClosestPointToCircle2afp() {}

    @Override
    @Test
    public void getClosestPointToRectangle2afp() {}

    @Override
    @Test
    public void getClosestPointToSegment2afp() {}

    @Override
    @Test
    public void getClosestPointToTriangle2afp() {}

    @Override
    @Test
    public void getClosestPointToPath2afp() {}

    @Override
    @Test
    public void getClosestPointToOrientedRectangle2afp() {}
        
    @Override
    @Test
    public void getClosestPointToParallelogram2afp() {}

    @Override
    @Test
    public void getClosestPointToRoundRectangle2afp() {}

    @Override
    @Test
    public void getClosestPointToMultiShape2afp() {}

    @Override
    @Test
    public void getDistance() {}

    @Override
    @Test
    public void getDistanceSquared() {}

    @Override
    @Test
    public void getDistanceL1() {}

    @Override
    @Test
    public void getDistanceLinf() {}

    @Override
    @Test
    public void setIT() {}

    @Override
    @Test
    public void getPathIterator() {}

    @Override
    @Test
    public void getPathIteratorTransform2D() {}

    @Override
    @Test
    public void createTransformedShape() {}

    @Override
    @Test
    public void translateVector2D() {} 

    @Override
    @Test
    public void toBoundingBox() {}
    
    @Override
    @Test
    public void toBoundingBoxB() {}

    @Override
    @Test
    public void containsRectangle2afp() {}

    @Override
    @Test
    public void containsShape2D() {}

    @Override
    @Test
    public void intersectsRectangle2afp() {}

    @Override
    @Test
    public void intersectsCircle2afp() {}

    @Override
    @Test
    public void intersectsTriangle2afp() {}

    @Override
    @Test
    public void intersectsEllipse2afp() {}

    @Override
    @Test
    public void intersectsSegment2afp() {}
    
    @Override
    @Test
    public void intersectsPath2afp() {}

    @Override
    @Test
    public void intersectsPathIterator2afp() {}

    @Override
    @Test
    public void intersectsOrientedRectangle2afp() {}

    @Override
    @Test
    public void intersectsParallelogram2afp() {}

    @Override
    @Test
    public void intersectsRoundRectangle2afp() {}

    @Override
    @Test
    public void getDistanceSquaredRectangle2afp() {}

    @Override
    @Test
    public void getDistanceSquaredCircle2afp() {} 

    @Override
    @Test
    public void getDistanceSquaredTriangle2afp() {}

    @Override
    @Test
    public void getDistanceSquaredEllipse2afp() {}

    @Override
    @Test
    public void getDistanceSquaredSegment2afp() {}
    
    @Override
    @Test
    public void getDistanceSquaredPath2afp() {}

    @Override
    @Test
    public void getDistanceSquaredOrientedRectangle2afp() {}

    @Override
    @Test
    public void getDistanceSquaredParallelogram2afp() {}

    @Override
    @Test
    public void getDistanceSquaredRoundRectangle2afp() {}

    @Override
    @Test
    public void getDistanceSquaredMultiShape2afp() {}

    @Override
    @Test
    public void translateDoubleDouble() {}

    @Override
    @Test
    public void containsDoubleDouble() {}

    @Override
    @Test
    public void intersectsShape2D() {}

    @Override
    @Test
    public void operator_addVector2D() {}

    @Override
    @Test
    public void operator_plusVector2D() {}

    @Override
    @Test
    public void operator_removeVector2D() {}

    @Override
    @Test
    public void operator_minusVector2D() {}

    @Override
    @Test
    public void operator_multiplyTransform2D() {}

    @Override
    @Test
    public void operator_andPoint2D() {} 

    @Override
    @Test
    public void operator_andShape2D() {}

    @Override
    @Test
    public void operator_upToPoint2D() {}
}
