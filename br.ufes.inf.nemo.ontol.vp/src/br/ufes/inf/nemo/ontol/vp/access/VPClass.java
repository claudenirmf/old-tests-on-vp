package br.ufes.inf.nemo.ontol.vp.access;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.vp.plugin.model.IClass;
import com.vp.plugin.model.IDependency;
import com.vp.plugin.model.IGeneralization;
import com.vp.plugin.model.IModelElement;
import com.vp.plugin.model.IReference;
import com.vp.plugin.model.IRelationship;
import com.vp.plugin.model.IStereotype;
import com.vp.plugin.model.ITaggedValue;
import com.vp.plugin.model.ITaggedValueContainer;
import com.vp.plugin.model.factory.IModelElementFactory;

import br.ufes.inf.nemo.ontol.model.EntityDeclaration;
import br.ufes.inf.nemo.ontol.model.HOClass;
import br.ufes.inf.nemo.ontol.model.OntoLClass;
import br.ufes.inf.nemo.ontol.vp.load.OntoLModelLoader;

/** 
 * A concrete wrapper for manipulation of Visual Paradigm's IClass instances. 
 * @author Claudenir Fonseca
 * */
public class VPClass extends VPModelElement {

	VPClass(IClass source){ 
		super(source); 
	}
	
	@Override
	public IClass getVPSource(){
		return (IClass) super.getVPSource();
	}
	
	public String[] getStereotypeList(){
		return getVPSource().toStereotypeArray();
	}
	
//	/*
//	 * This method is null-safe, at most returns a String[] of length 0.
//	 */
//	public String[] getIncomingAssociationsId(){
//		final List<String> inAssociationsIds = new ArrayList<String>();
//		for (IRelationshipEnd toEnd : getSource().toToRelationshipEndArray()) {
//			inAssociationsIds.add(toEnd.getEndRelationship().getId());
//		}
//		return inAssociationsIds.toArray(new String[0]);
//	}
//
//	/*
//	 * This method is null-safe, at most returns a AssociationWrapper[] of length 0.
//	 */
//	public _VPAssociationWrap[] getIncomingAssociations() {
//		final List<_VPAssociationWrap> inAssociations = new ArrayList<_VPAssociationWrap>();
//		for (String associationId : getIncomingAssociationsId()) {
//			final _VPAssociationWrap tmp = 
//					(_VPAssociationWrap) VPModelAccess.getModelElemWrap(associationId);
//			if (tmp != null) {
//				inAssociations.add(tmp);
//			}
//		}
//		return inAssociations.toArray(new _VPAssociationWrap[0]);
//	}
//
//	/*
//	 * This method is null-safe, at most returns a String[] of length 0.
//	 */
//	public String[] getOutcommingAssociationsId(){
//		final List<String> ret = new ArrayList<String>();
//		for (IRelationshipEnd fromEnd : getSource().toFromRelationshipEndArray()) {
//			ret.add(fromEnd.getEndRelationship().getId());
//		}
//		return ret.toArray(new String[0]);
//	}
//
//	/*
//	 * This method is null-safe, at most returns a AssociationWrapper[] of length 0.
//	 */
//	public _VPAssociationWrap[] getOutgoingAssociations() {
//		final List<_VPAssociationWrap> outgoingRelations = new ArrayList<_VPAssociationWrap>();
//		for (String associationId : getOutcommingAssociationsId()) {
//			_VPAssociationWrap tmp = (_VPAssociationWrap) VPModelAccess.getModelElemWrap(associationId);
//			if (tmp != null) {
//				outgoingRelations.add(tmp);
//			}
//		}
//		return outgoingRelations.toArray(new _VPAssociationWrap[0]);
//	}

	// TODO REVIEW THE USE OF VPModelManager.getModelElemWrap() BASED ON IDS
	public Set<VPClass> getSuperClasses(){
		Set<VPClass> superClasses = new LinkedHashSet<VPClass>();
		if(getVPSource().toReferenceArray()==null){ return superClasses; }
		
		for (IReference reference : getVPSource().toReferenceArray()) {
			VPModelElement g = VPModelElement.wrap(reference);
			if(g instanceof VPGeneralization){
				superClasses.add(((VPGeneralization) g).getSuperClass());
			}
		};
		return superClasses;
	}
	
	/**
	 * Returns all super types of a class.
	 * 
	 * @param classHierarchy - an empty set
	 * @return the classHierarchy set with all super types added to it
	 * @author Claudenir Fonseca
	 */
	public Set<VPClass> getClassHierarchy(Set<VPClass> classHierarchy){
		for (VPClass superClasses : getSuperClasses()) {
			if(!classHierarchy.contains(superClasses)){
				classHierarchy.add(superClasses);
				classHierarchy.addAll(superClasses.getSuperClasses());
			}
		}
		return classHierarchy;
	}

	public void addSuper(OntoLClass ontoLClass) {
		VPClass superVPC = (VPClass) VPModelAccess.getModelElement(OntoLModelLoader.getFullyQualifiedName(ontoLClass));
		IGeneralization gen = IModelElementFactory.instance().createGeneralization();
		// Set ontolClass's wrapper as superclasss
		gen.setFrom(superVPC.getVPSource());
		// Set itself as subclass
		gen.setTo(getVPSource());
	}
	
	public void removeSuper(VPClass vpClass) throws Exception {
		throw new Exception("Removal of old super classes not implemented yet.");
		// TODO Auto-generated method stub
	}

	private Set<VPDependency> getDependenciesByStereotype(String strName) {
		Set<VPDependency> iofs = new HashSet<VPDependency>();
		IClass source = getVPSource();
		if(source.toReferenceArray()==null) { return iofs; }
		
		@SuppressWarnings("rawtypes")
		Iterator iter = source.toRelationshipIterator();
		while(iter.hasNext()){
			IRelationship r = (IRelationship) iter.next();
			if(r instanceof IDependency){
				String[] strNames = ((IDependency) r).toStereotypeArray();
				if(strNames==null || strNames.length==0)	continue;
				for(String name : strNames)
					if(name ==  strName)
						iofs.add((VPDependency) VPModelElement.wrap(r));
			}
		}
		return iofs;
	}
	
	public Set<VPClass> getInstantiatedClasses() {
		Set<VPClass> iofs = new HashSet<VPClass>();
		for (VPDependency vpd : getDependenciesByStereotype(OntoLModelLoader.STR_INSTANTIATION)) {
			VPClass vpc = vpd.getTarget();
			if(vpc!=null)
				iofs.add(vpc);
		}
		return iofs;
	}

	public Set<IRelationship> getDepartingRelationshipTo(VPClass other) {
		Set<IRelationship> set = new HashSet<IRelationship>();
		@SuppressWarnings("rawtypes")
		Iterator iter = getVPSource().toRelationshipIterator();
		while(iter.hasNext()){
			IRelationship r = (IRelationship) iter.next();
			if(r.getFrom()==other.getVPSource())
				set.add(r);
		}
		return set;
	}
	
	public Set<IRelationship> getIncomingRelationshipFrom(VPClass other) {
		Set<IRelationship> set = new HashSet<IRelationship>();
		@SuppressWarnings("rawtypes")
		Iterator iter = getVPSource().fromRelationshipIterator();
		while(iter.hasNext()){
			IRelationship r = (IRelationship) iter.next();
			if(r.getTo()==other.getVPSource())
				set.add(r);
		}
		return set;
	}
	
	public void setOrder(HOClass ho) {
		IClass source = this.getVPSource();
		ITaggedValueContainer container = source.getTaggedValues();
		ITaggedValue[] tvalues = container.toTaggedValueArray();
		int tvcount = tvalues == null ? 0 : tvalues.length;
		for (int i=0; i < tvcount; i++) {
			if(tvalues[i].getName()==OntoLModelLoader.TG_VALLUE_ORDER){
				tvalues[i].setValue(ho.getOrder());
				tvalues[i].setMultiplicity(ITaggedValue.MULTIPLICITY_ONE);
				return ;
			}
		}
	}

	public void addInstantiatedClass(OntoLClass c) {
		IDependency d = IModelElementFactory.instance().createDependency();
		VPDependency vpd = (VPDependency) VPModelElement.wrap(d);
		VPClass target = (VPClass) VPModelAccess.getModelElement(OntoLModelLoader.getFullyQualifiedName(c));
		vpd.setTarget(target);
		vpd.setSource(this);
		vpd.addStereotype(OntoLModelLoader.STR_INSTANTIATION);
		System.out.println("SOURCE "+this+" and TARGET "+target);
	}
	
}
