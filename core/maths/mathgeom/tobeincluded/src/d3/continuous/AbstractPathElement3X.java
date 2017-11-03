package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.PathElement3D;
import org.eclipse.xtext.xbase.lib.Pure;

public interface AbstractPathElement3X extends PathElement3D {


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
	abstract public double getFromZ();


	@Pure
	abstract public double getToX();


	@Pure
	abstract public double getToY();

	@Pure
	abstract public double getToZ();


	@Pure
	abstract public double getCtrlX1();


	@Pure
	abstract public double getCtrlY1();

	@Pure
	abstract public double getCtrlZ1();


	@Pure
	abstract public double getCtrlX2();


	@Pure
	abstract public double getCtrlY2();

	@Pure
	abstract public double getCtrlZ2();


	
	abstract public void setFromX(double fromX1);

	
	abstract public void setFromY(double fromY1);
	
	abstract public void setFromZ(double fromZ1);

	
	abstract public void setToX(double toX1);

	
	abstract public void setToY(double toY1);
	
	abstract public void setToZ(double toZ1);

	
	abstract public void setCtrlX1(double ctrlX11);

	
	abstract public void setCtrlY1(double ctrlY11);
	
	abstract public void setCtrlZ1(double ctrlZ11);

	
	abstract public void setCtrlX2(double ctrlX21);

	
	abstract public void setCtrlY2(double ctrlY21);
	
	
	abstract public void setCtrlZ2(double ctrlZ21);
}
