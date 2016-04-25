package org.arakhne.afc.math.geometry.d2.afp;

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
public class PathWindingRule2afpTestRule implements TestRule {

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
