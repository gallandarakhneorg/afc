package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.PathElement2D;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


@SuppressWarnings("restriction")
public abstract class AbstractPathElement2D implements PathElement2D {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8930724258123820092L;
	
	/** Source point.
	 */
	private DoubleProperty fromXProperty = new SimpleDoubleProperty(0f);
	
	/** Source point.
	 */
	private DoubleProperty fromYProperty = new SimpleDoubleProperty(0f);

	/** Target point.
	 */
	private DoubleProperty toXProperty = new SimpleDoubleProperty(0f);
	
	/** Target point.
	 */
	private DoubleProperty toYProperty = new SimpleDoubleProperty(0f);

	/** First control point.
	 */
	private DoubleProperty ctrlX1Property = new SimpleDoubleProperty(0f);
	
	/** First control point.
	 */
	private DoubleProperty ctrlY1Property = new SimpleDoubleProperty(0f);

	/** Second control point.
	 */
	private DoubleProperty ctrlX2Property = new SimpleDoubleProperty(0f);
	
	/** Second control point.
	 */
	private DoubleProperty ctrlY2Property = new SimpleDoubleProperty(0f);
	
	/** Create an instance of path element.
	 * 
	 * @param type is the type of the new element.
	 * @param lastX is the coordinate of the last point.
	 * @param lastY is the coordinate of the last point.
	 * @param coords are the coordinates.
	 * @return the instance of path element.
	 */
	public static AbstractPathElement2D newInstance(PathElementType type, double lastX, double lastY, double[] coords) {
		switch(type) {
		case MOVE_TO:
			return new MovePathElement2d(coords[0], coords[1]);
		case LINE_TO:
			return new LinePathElement2d(lastX, lastY, coords[0], coords[1]);
		case QUAD_TO:
			return new QuadPathElement2d(lastX, lastY, coords[0], coords[1], coords[2], coords[3]);
		case CURVE_TO:
			return new CurvePathElement2d(lastX, lastY, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
		case CLOSE:
			return new ClosePathElement2d(lastX, lastY, coords[0], coords[1]);
		default:
		}
		throw new IllegalArgumentException();
	}
	
	/** Type of the path element.
	 */
	protected final PathElementType type;
	
	
	/**
	 * @param type1 is the type of the element.
	 * @param fromx is the source point.
	 * @param fromy is the source point.
	 * @param ctrlx1 is the first control point.
	 * @param ctrly1 is the first control point.
	 * @param ctrlx2 is the first control point.
	 * @param ctrly2 is the first control point.
	 * @param tox is the target point.
	 * @param toy is the target point.
	 */
	public AbstractPathElement2D(PathElementType type1, double fromx, double fromy, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double tox, double toy) {
		assert(type1!=null);
		this.type = type1;
		this.setFromX(fromx);
		this.setFromY(fromy);
		this.setCtrlX1(ctrlx1);
		this.setCtrlY1(ctrly1);
		this.setCtrlX2(ctrlx2);
		this.setCtrlY2(ctrly2);
		this.setToX(tox);
		this.setToY(toy);
	}

	/** Copy the coords into the given array, except the source point.
	 * 
	 * @param array
	 */
	public abstract void toArray(double[] array);

	/** Copy the coords into an array, except the source point.
	 * 
	 * @return the array of the points, except the source point.
	 */
	public abstract double[] toArray();
	
	public double getFromX() {
		return this.fromXProperty.doubleValue();
	}

	public double getFromY() {
		return this.fromYProperty.doubleValue();
	}

	
	public double getToX() {
		return this.toXProperty.doubleValue();
	}

	
	public double getToY() {
		return this.toYProperty.doubleValue();
	}

	
	public double getCtrlX1() {
		return this.ctrlX1Property.doubleValue();
	}

	
	public double getCtrlY1() {
		return this.ctrlY1Property.doubleValue();
	}

	
	public double getCtrlX2() {
		return this.ctrlX2Property.doubleValue();
	}

	
	public double getCtrlY2() {
		return this.ctrlY2Property.doubleValue();
	}


	
	public void setFromX(double fromX1) {
		this.fromXProperty.set(fromX1);
	}

	
	public void setFromY(double fromY1) {
		this.fromYProperty.set(fromY1);
	}

	
	public void setToX(double toX1) {
		this.toXProperty.set(toX1);
	}

	
	public void setToY(double toY1) {
		this.toYProperty.set(toY1);
	}

	
	public void setCtrlX1(double ctrlX11) {
		this.ctrlX1Property.set(ctrlX11);
	}

	
	public void setCtrlY1(double ctrlY11) {
		this.ctrlY1Property.set(ctrlY11);
	}

	
	public void setCtrlX2(double ctrlX21) {
		this.ctrlX2Property.set(ctrlX21);
	}

	
	public void setCtrlY2(double ctrlY21) {
		this.ctrlY2Property.set(ctrlY21);
	
	}
	
	

	/** An element of the path that represents a <code>MOVE_TO</code>.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class MovePathElement2d extends AbstractPathElement2D {
		
		private static final long serialVersionUID = -5596181248741970433L;
		
		/**
		 * @param x
		 * @param y
		 */
		public MovePathElement2d(double x, double y) {
			super(PathElementType.MOVE_TO,
					Double.NaN, Double.NaN,
					Double.NaN, Double.NaN,
					Double.NaN, Double.NaN,
					x, y);
		}

		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY());
		}

		@Override
		public boolean isDrawable() {
			return false;
		}
		
		@Override
		public void toArray(double[] array) {
			array[0] = this.getToX();
			array[1] = this.getToY();
		}
		
		@Override
		public double[] toArray() {
			return new double[] {this.getToX(), this.getToY()};
		}

		@Override
		public String toString() {
			return "MOVE("+ //$NON-NLS-1$
					this.getToX()+"x"+ //$NON-NLS-1$
					this.getToY()+")"; //$NON-NLS-1$
		}

		@Override
		public final PathElementType getType() {
			return PathElementType.MOVE_TO;
		}


	}
	
	/** An element of the path that represents a <code>LINE_TO</code>.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class LinePathElement2d extends AbstractPathElement2D {
		
		private static final long serialVersionUID = -5878571187312098882L;
		
		/**
		 * @param fromx
		 * @param fromy
		 * @param tox
		 * @param toy
		 */
		public LinePathElement2d(double fromx, double fromy, double tox, double toy) {
			super(PathElementType.LINE_TO,
					fromx, fromy,
					Double.NaN, Double.NaN,
					Double.NaN, Double.NaN,
					tox, toy);
		}
		
		@Override
		public final PathElementType getType() {
			return PathElementType.LINE_TO;
		}

		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY());
		}

		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(double[] array) {
			array[0] = this.getToX();
			array[1] = this.getToY();
		}
		
		@Override
		public double[] toArray() {
			return new double[] {this.getToX(), this.getToY()};
		}

		@Override
		public String toString() {
			return "LINE("+ //$NON-NLS-1$
					this.getToX()+"x"+ //$NON-NLS-1$
					this.getToY()+")"; //$NON-NLS-1$
		}


	}
	
	/** An element of the path that represents a <code>QUAD_TO</code>.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class QuadPathElement2d extends AbstractPathElement2D {
		
		private static final long serialVersionUID = 5641358330446739160L;
		
		/**
		 * @param fromx
		 * @param fromy
		 * @param ctrlx
		 * @param ctrly
		 * @param tox
		 * @param toy
		 */
		public QuadPathElement2d(double fromx, double fromy, double ctrlx, double ctrly, double tox, double toy) {
			super(PathElementType.QUAD_TO,
					fromx, fromy,
					ctrlx, ctrly,
					Double.NaN, Double.NaN,
					tox, toy);
		}
		
		@Override
		public final PathElementType getType() {
			return PathElementType.QUAD_TO;
		}

		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY()) &&
					(this.getCtrlX1()==this.getToX()) && (this.getCtrlY1()==this.getToY());
		}

		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(double[] array) {
			array[0] = this.getCtrlX1();
			array[1] = this.getCtrlY1();
			array[2] = this.getToX();
			array[3] = this.getToY();
		}
		
		@Override
		public double[] toArray() {
			return new double[] {this.getCtrlX1(), this.getCtrlY1(), this.getToX(), this.getToY()};
		}
		
		@Override
		public String toString() {
			return "QUAD("+ //$NON-NLS-1$
					this.getCtrlX1()+"x"+ //$NON-NLS-1$
					this.getCtrlY1()+"|"+ //$NON-NLS-1$
					this.getToX()+"x"+ //$NON-NLS-1$
					this.getToY()+")"; //$NON-NLS-1$
		}

		

	}

	/** An element of the path that represents a <code>CURVE_TO</code>.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class CurvePathElement2d extends AbstractPathElement2D {
		
		private static final long serialVersionUID = -1449309552719221756L;

		
		
		/**
		 * @param fromx
		 * @param fromy
		 * @param ctrlx1
		 * @param ctrly1
		 * @param ctrlx2
		 * @param ctrly2
		 * @param tox
		 * @param toy
		 */
		public CurvePathElement2d(double fromx, double fromy, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double tox, double toy) {
			super(PathElementType.CURVE_TO,
					fromx, fromy,
					ctrlx1, ctrly1,
					ctrlx2, ctrly2,
					tox, toy);
		}
		
		@Override
		public final PathElementType getType() {
			return PathElementType.CURVE_TO;
		}

		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY()) &&
					(this.getCtrlX1()==this.getToX()) && (this.getCtrlY1()==this.getToY()) &&
					(this.getCtrlX2()==this.getToX()) && (this.getCtrlY2()==this.getToY());
		}

		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(double[] array) {
			array[0] = this.getCtrlX1();
			array[1] = this.getCtrlY1();
			array[2] = this.getCtrlX2();
			array[3] = this.getCtrlY2();
			array[4] = this.getToX();
			array[5] = this.getToY();
		}
		
		@Override
		public double[] toArray() {
			return new double[] {this.getCtrlX1(), this.getCtrlY1(), this.getCtrlX2(), this.getCtrlY2(), this.getToX(), this.getToY()};
		}

		@Override
		public String toString() {
			return "CURVE("+ //$NON-NLS-1$
					this.getCtrlX1()+"x"+ //$NON-NLS-1$
					this.getCtrlY1()+"|"+ //$NON-NLS-1$
					this.getCtrlX2()+"x"+ //$NON-NLS-1$
					this.getCtrlY2()+"|"+ //$NON-NLS-1$
					this.getToX()+"x"+ //$NON-NLS-1$
					this.getToY()+")"; //$NON-NLS-1$
		}

		

	}

	/** An element of the path that represents a <code>CLOSE</code>.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class ClosePathElement2d extends AbstractPathElement2D {
		
		private static final long serialVersionUID = 4643537091880303796L;

		/**
		 * @param fromx
		 * @param fromy
		 * @param tox
		 * @param toy
		 */
		public ClosePathElement2d(double fromx, double fromy, double tox, double toy) {
			super(PathElementType.CLOSE,
					fromx, fromy,
					Double.NaN, Double.NaN,
					Double.NaN, Double.NaN,
					tox, toy);
		}
		
		@Override
		public final PathElementType getType() {
			return PathElementType.CLOSE;
		}

		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY());
		}
		
		@Override
		public boolean isDrawable() {
			return false;
		}

		@Override
		public void toArray(double[] array) {
			//
		}
		
		@Override
		public double[] toArray() {
			return new double[0];
		}
		
		@Override
		public String toString() {
			return "CLOSE"; //$NON-NLS-1$
		}

	}

}
