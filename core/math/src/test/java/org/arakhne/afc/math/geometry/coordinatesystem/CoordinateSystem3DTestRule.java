package org.arakhne.afc.math.geometry.coordinatesystem;

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
public class CoordinateSystem3DTestRule implements TestRule {

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                for (CoordinateSystem3D coordinateSystem : CoordinateSystem3D.values()) {
                	CoordinateSystem3D.setDefaultCoordinateSystem(coordinateSystem);
                	try {
                		base.evaluate();
                	} catch (AssumptionViolatedException exception) {
                		// Ignore the exception related to the "ignore test"
                	}
                }
            }
        };
    }

}
