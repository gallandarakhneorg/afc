package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.geometry.d2.PathElement2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface AbstractPathElement2X extends PathElement2D{
	
	

	/** Copy the coords into the given array, except the source point.
	 * 
	 * @param array
	 */
	public abstract void toArray(double[] array);

	/** Copy the coords into an array, except the source point.
	 * 
	 * @return the array of the points, except the source point.
	 */
	@Pure
	public abstract double[] toArray();

	@Pure
	abstract public double getFromX();

	@Pure
	abstract public double getFromY();

	@Pure
	abstract public double getToX();

	@Pure
	abstract public double getToY();

	@Pure
	abstract public double getCtrlX1();

	@Pure
	abstract public double getCtrlY1();

	@Pure
	abstract public double getCtrlX2();

	@Pure
	abstract public double getCtrlY2();


	
	abstract public void setFromX(double fromX1);

	
	abstract public void setFromY(double fromY1);

	
	abstract public void setToX(double toX1);

	
	abstract public void setToY(double toY1);

	
	abstract public void setCtrlX1(double ctrlX11);

	
	abstract public void setCtrlY1(double ctrlY11);

	
	abstract public void setCtrlX2(double ctrlX21);

	
	abstract public void setCtrlY2(double ctrlY21);
	
	
	

}
