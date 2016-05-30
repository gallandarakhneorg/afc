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

package org.arakhne.afc.math.geometry;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/** Enable tu run a unit test for all the types of a coordinate system 2D.
 * 
 * @see CoordinateSystem2D
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class PathWindingRuleTestRule implements TestRule {

	/** Current path winding rule.
	 *
	 * <p>This field may be used by the tets for initializing the paths.
	 */
	public static PathWindingRule CURRENT_RULE;
	
    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                for (PathWindingRule rule : PathWindingRule.values()) {
                	try {
                		CURRENT_RULE = rule;
                		base.evaluate();
                	} catch (AssumptionViolatedException exception) {
                		// Ignore the exception related to the "ignore test"
                	}
                }
            }
        };
    }

}
