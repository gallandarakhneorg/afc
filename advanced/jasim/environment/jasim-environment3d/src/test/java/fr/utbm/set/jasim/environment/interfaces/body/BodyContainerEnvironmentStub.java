/**
 * 
 */
package fr.utbm.set.jasim.environment.interfaces.body;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.vecmath.Point3d;

import fr.utbm.set.collection.CollectionSizedIterator;
import fr.utbm.set.collection.CollectionUtil;
import fr.utbm.set.collection.EmptyIterator;
import fr.utbm.set.collection.SizedIterable;
import fr.utbm.set.collection.SizedIterator;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.geom.object.EuclidianDirection;
import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.jasim.environment.interfaces.body.factory.AgentBodyFactory;
import fr.utbm.set.jasim.environment.interfaces.body.factory.BodyDescription;
import fr.utbm.set.jasim.environment.interfaces.body.factory.FrustumDescription;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencable;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencer;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.GroundPerception;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceivable;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceiver;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception3D;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceptions;
import fr.utbm.set.jasim.environment.model.influences.InfluenceCollector;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList3D3D;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGenerator;
import fr.utbm.set.jasim.environment.time.Clock;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class BodyContainerEnvironmentStub implements BodyContainerEnvironment {

	private Clock clock = null;
	
	/** Influences.
	 */
	public final Collection<Influence> influences = new ArrayList<Influence>();
	
	/** Perceptions.
	 */
	public final Map<Perceiver, Collection<? extends Perception>> perceptions = new HashMap<Perceiver,Collection<? extends Perception>>();

	/**
	 */
	public BodyContainerEnvironmentStub() {
		//
	}

	/**
	 * @param clock
	 */
	public BodyContainerEnvironmentStub(Clock clock) {
		this.clock = clock;
	}

	/**
	 * @param perceivers
	 */
	public BodyContainerEnvironmentStub(Perceiver... perceivers) {
		for(Perceiver p : perceivers) {
			Collection<Perception3D> percepts = new PerceptionsStub();
			int count = (int)(Math.random() * 100)+2;
			for(int i=0; i<count; ++i) {
				percepts.add(new Perception3DStub());
			}
			this.perceptions.put(p, percepts);
		}
	}
	
	@Override
	public InfluenceCollector getInfluenceCollector() {
		return new InfluenceCollectorStub();
	}

	@Override
	public PerceptionGenerator getPerceptionGenerator() {
		return new PerceptionGeneratorStub();
	}

	@Override
	public Clock getSimulationClock() {
		return this.clock;
	}
	
	@Override
	public AgentBodyFactory getAgentBodyFactory(
			EuclidianPoint position, 
			EuclidianDirection orientation,
			BodyDescription body,
			Iterable<FrustumDescription> frustums) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class InfluenceCollectorStub implements InfluenceCollector {

		/**
		 */
		public InfluenceCollectorStub() {
			//
		}

		@Override
		public SizedIterable<? extends Influence> consumeStandardInfluences() {
			return CollectionUtil.toSizedIterable(BodyContainerEnvironmentStub.this.influences);
		}

		@Override
		public SizedIterable<? extends Influence> consumePopulationInfluences() {
			return CollectionUtil.toSizedIterable(Collections.<Influence>emptyList());
		}

		@Override
		public void registerInfluence(Influencer defaultInfluencer,
				Influencable defaultInfluencedObject, Influence... influence) {
			BodyContainerEnvironmentStub.this.influences.addAll(Arrays.asList(influence));
		}
		
	}
	
	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class PerceptionGeneratorStub implements PerceptionGenerator {

		/**
		 */
		public PerceptionGeneratorStub() {
			//
		}

		@Override
		public void computeAgentPerceptions() {
			//
		}

		@SuppressWarnings("unchecked")
		@Override
		public <PT extends Perception> Perceptions<PT> getPerceptions(Perceiver perceiver, Class<PT> perceptionType) {
			Perceptions<PT> p = (Perceptions<PT>)BodyContainerEnvironmentStub.this.perceptions.get(perceiver);
			if (p==null) return (Perceptions<PT>)new PerceptionList3D3D<CombinableBounds3D,CombinableBounds3D>();
			return p;
		}
		
	}
	
	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class Perception3DStub implements Perception3D {

		/**
		 */
		public Perception3DStub() {
			//
		}

		@Override
		public Point3d getPerceivedObjectPosition() {
			return null;
		}

		@Override
		public IntersectionType getClassification() {
			return null;
		}

		@Override
		public UUID getFrustum() {
			return UUID.randomUUID();
		}

		@Override
		public Perceivable getPerceivedObject() {
			return null;
		}
		
	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class PerceptionsStub extends ArrayList<Perception3D> implements Perceptions<Perception3D> {
		
		private static final long serialVersionUID = 5616908477243955888L;

		/**
		 */
		public PerceptionsStub() {
			//
		}

		@Override
		public int getDynamicPerceptCount() {
			return 0;
		}

		@Override
		public SizedIterator<Perception3D> getDynamicPercepts() {
			return new EmptyIterator<Perception3D>();
		}

		@Override
		public GroundPerception getGroundPerception() {
			return null;
		}

		@Override
		public int getObjectPerceptCount() {
			return size();
		}

		@Override
		public int getStaticPerceptCount() {
			return size();
		}

		@Override
		public SizedIterator<Perception3D> getStaticPercepts() {
			return new CollectionSizedIterator<Perception3D>(this);
		}

		@Override
		public boolean hasGroundPercept() {
			return false;
		}

		@Override
		public boolean hasObjectPercept() {
			return true;
		}
		
	}

}
