package org.arakhne.afc.math.geometry.d3;

import java.util.Iterator;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.eclipse.xtext.xbase.lib.Pure;

/** This interface describes an interator on path elements.
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @param <T> the type of the path elements.
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface PathIterator3D<T extends PathElement3D> extends Iterator<T> {
	/** Replies the winding rule for the path.
	 * 
	 * @return the winding rule for the path.
	 */
	@Pure
	PathWindingRule getWindingRule();

	/** Replies the path is composed only by
	 * one <code>MOVE_TO</code>, and a sequence of <code>LINE_TO</code>
	 * primitives.
	 * 
	 * @return <code>true</code> if the path does not
	 * contain curve primitives, <code>false</code>
	 * otherwise.
	 */
	@Pure
	boolean isPolyline();

	/** Replies the path contains a curve.
	 * 
	 * @return <code>true</code> if the path contains
	 * curve primitives, <code>false</code>
	 * otherwise.
	 */
	@Pure
	boolean isCurved();

	/** Replies the path has multiple parts, i.e. multiple <code>MOVE_TO</code> are inside.
	 * primitives.
	 * 
	 * @return <code>true</code> if the path has multiple move-to primitive, <code>false</code>
	 * otherwise.
	 */
	@Pure
	boolean isMultiParts();

	/** Replies the path is composed only by
	 * one <code>MOVE_TO</code>, a sequence of <code>LINE_TO</code>
	 * or <code>QUAD_TO</code> or <code>CURVE_TO</code>, and a
	 * single <code>CLOSE</code> primitives.
	 * 
	 * @return <code>true</code> if the path does not
	 * contain curve primitives, <code>false</code>
	 * otherwise.
	 */
	@Pure
	boolean isPolygon();

	/** Replies a reset instance of this iterator.
	 *
	 * <p>The reset instance enables to restart iterations with the replied iterator.
	 *
	 * @return the reset iterator.
	 */
	PathIterator3D<T> restartIterations();
}
