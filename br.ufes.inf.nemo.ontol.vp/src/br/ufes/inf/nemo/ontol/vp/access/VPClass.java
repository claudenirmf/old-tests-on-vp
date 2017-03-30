package br.ufes.inf.nemo.ontol.vp.access;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.vp.plugin.model.IAssociation;
import com.vp.plugin.model.IAssociationEnd;
import com.vp.plugin.model.IAttribute;
import com.vp.plugin.model.IClass;
import com.vp.plugin.model.IDependency;
import com.vp.plugin.model.IGeneralization;
import com.vp.plugin.model.IRelationship;
import com.vp.plugin.model.IRelationshipEnd;
import com.vp.plugin.model.ITaggedValue;
import com.vp.plugin.model.ITaggedValueContainer;
import com.vp.plugin.model.factory.IModelElementFactory;

import br.ufes.inf.nemo.ontol.model.Attribute;
import br.ufes.inf.nemo.ontol.model.CategorizationType;
import br.ufes.inf.nemo.ontol.model.EntityDeclaration;
import br.ufes.inf.nemo.ontol.model.HOClass;
import br.ufes.inf.nemo.ontol.model.OntoLClass;
import br.ufes.inf.nemo.ontol.model.Reference;
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
	
	private Set<VPGeneralization> getGeneralizationsToSuperClasses(){
		Set<VPGeneralization> generalizations = new LinkedHashSet<VPGeneralization>();
		if(getVPSource().toRelationshipCount()==0)	return generalizations;
		@SuppressWarnings("rawtypes")
		Iterator iter = getVPSource().toRelationshipIterator();
		while(iter.hasNext()){
			IRelationship next = (IRelationship) iter.next();
			VPModelElement g = VPModelElement.wrap(next);
			if(g instanceof VPGeneralization){
				generalizations.add((VPGeneralization) g);
			}
		}
		
		return generalizations;
	}

	// TODO REVIEW THE USE OF VPModelManager.getModelElemWrap() BASED ON IDS
	public Set<VPClass> getSuperClasses(){
		Set<VPClass> superClasses = new LinkedHashSet<VPClass>();
		for (VPGeneralization g : getGeneralizationsToSuperClasses()) {
			superClasses.add(g.getSuperClass());
		}
		return superClasses;
//		if(getVPSource().toRelationshipCount()==0)	return superClasses;
//		@SuppressWarnings("rawtypes")
//		Iterator iter = getVPSource().toRelationshipIterator();
//		while(iter.hasNext()){
//			IRelationship next = (IRelationship) iter.next();
//			VPModelElement g = VPModelElement.wrap(next);
//			if(g instanceof VPGeneralization){
//				superClasses.add(((VPGeneralization) g).getSuperClass());
//			}
//		}
//		
//		if(getVPSource().toReferenceArray()==null){ return superClasses; }
//		
//		for (IReference reference : getVPSource().toReferenceArray()) {
//			VPModelElement g = VPModelElement.wrap(reference);
//			if(g instanceof VPGeneralization){
//				superClasses.add(((VPGeneralization) g).getSuperClass());
//			}
//		};
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
		for (VPGeneralization g : getGeneralizationsToSuperClasses()) {
			if(g.getSuperClass()==vpClass)
				g.getVPSource().delete();
		}
	}

	private Set<VPDependency> getDependenciesByType(VPDependencyType type) {
		Set<VPDependency> dependencies = new HashSet<VPDependency>();
		IClass source = getVPSource();
		
		@SuppressWarnings("rawtypes")
		Iterator iter = source.fromRelationshipIterator();
		while(iter!=null && iter.hasNext()){
			IRelationship r = (IRelationship) iter.next();
			if(r instanceof IDependency){
				VPDependency vpd = (VPDependency) VPModelElement.wrap(r);
				if(vpd.getType()==type)
					dependencies.add(vpd);
			}
		}
		return dependencies;
	}
	
	public Set<VPClass> getInstantiatedClasses() {
		Set<VPClass> iofs = new HashSet<VPClass>();
		for (VPDependency vpd : getDependenciesByType(VPDependencyType.INSTANTIATION)) {
			VPClass vpc = vpd.getTarget();
			if(vpc!=null)
				iofs.add(vpc);
		}
		return iofs;
	}
	
	public Set<VPClass> getSubordinatorClasses() {
		Set<VPClass> subordinators = new HashSet<VPClass>();
		for (VPDependency vpd : getDependenciesByType(VPDependencyType.SUBORDINATION)) {
			VPClass vpc = vpd.getTarget();
			if(vpc!=null)
				subordinators.add(vpc);
		}
		return subordinators;
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
			if(tvalues[i].getName().equals(OntoLModelLoader.TG_VALLUE_ORDER)){
				tvalues[i].setValue(ho.getOrder());
				tvalues[i].setMultiplicity(ITaggedValue.MULTIPLICITY_ONE);
				return ;
			}
		}
	}

	public void addDependencyTo(OntoLClass c, String dependencyStereotype) {
		IDependency d = IModelElementFactory.instance().createDependency();
		VPDependency vpd = (VPDependency) VPModelElement.wrap(d);
		VPClass target = (VPClass) VPModelAccess.getModelElement(OntoLModelLoader.getFullyQualifiedName(c));
		vpd.setTarget(target);
		vpd.setSource(this);
		vpd.addStereotype(dependencyStereotype);
	}
	
	public void addInstantiationTo(OntoLClass c) {
		addDependencyTo(c, OntoLModelLoader.STR_INSTANTIATION);
	}

	public void addSubordinatorClass(OntoLClass c) {
		addDependencyTo(c, OntoLModelLoader.STR_SUBORDINATION);
	}

	public VPDependency getDependencyToBasetype() {
		Set<VPDependency> dependencies = getDependencies();
		for (VPDependency vpd : dependencies) {
			switch (vpd.getType()) {
			case CATERGORIZATION:
			case COMPLETE_CATEGORIZATION:
			case DISJOINT_CATEGORIZATION:
			case PARTITIONING:
				return vpd;
			default:
				break;
			}
		}
		
		return null;
	}
	
	public VPDependency getDependencyToPowertypeOf() {
		Set<VPDependency> dependencies = getDependencies();
		for (VPDependency vpd : dependencies) {
			switch (vpd.getType()) {
			case POWERTYPING:
				return vpd;
			default:
				break;
			}
		}
		
		return null;
	}
	
//	public VPDependency getCategorization() {
//		Set<VPDependency> dependencies = getDependencies();
//		for (VPDependency vpd : dependencies)
//			if(vpd.isCategorization())
//				return vpd;
//		return null;
//	}
	
	private Set<VPDependency> getDependencies(){
		Set<VPDependency> dependencies = new HashSet<VPDependency>();
		IClass source = getVPSource();
		
		@SuppressWarnings("rawtypes")
		Iterator iter = source.fromRelationshipIterator();
		while(iter!=null && iter.hasNext()){
			IRelationship r = (IRelationship) iter.next();
			if(r instanceof IDependency)
				dependencies.add((VPDependency) VPModelElement.wrap(r));
		}
		return dependencies;
	}

	public void addBasetype(OntoLClass c, CategorizationType catType) {
		switch (catType) {
		case CATEGORIZER:
			addDependencyTo(c,OntoLModelLoader.STR_CATEGORIZATION);
			break;
		case COMPLETE_CATEGORIZER:
			addDependencyTo(c,OntoLModelLoader.STR_COMPL_CATEGORZATION);
			break;
		case DISJOINT_CATEGORIZER:
			addDependencyTo(c,OntoLModelLoader.STR_DISJ_CATEGORIZATION);
			break;
		case PARTITIONER:
			addDependencyTo(c,OntoLModelLoader.STR_PARTITIONS);
			break;
		default:
			break;
		}
	}

	public void addPowertypeOf(OntoLClass c) {
		addDependencyTo(c,OntoLModelLoader.STR_POWER_TYPE);
	}
	
	public Set<VPAssociation> getToAssociations(){
		Set<VPAssociation> associations = new HashSet<VPAssociation>();
		IClass source = getVPSource();
		if(source.fromRelationshipEndCount()==0) { return associations; }
		
		@SuppressWarnings("rawtypes")
		Iterator iter = source.fromRelationshipEndIterator();
		while(iter.hasNext()){
			IRelationshipEnd end = (IRelationshipEnd) iter.next();
			if(end instanceof IAssociationEnd)
				associations.add((VPAssociation) VPModelElement.wrap(end.getEndRelationship()));
		}
		return associations;
	}

	public void addToAssociation(Reference reference) {
		IAssociation associationSource = IModelElementFactory.instance().createAssociation();
		VPAssociation association = (VPAssociation) VPModelElement.wrap(associationSource);

		getVPSource().getParent().addChild(associationSource);
		VPClass to = (VPClass) VPModelAccess.getModelElement(OntoLModelLoader.getFullyQualifiedName(reference.getPropertyType()));
		associationSource.setFrom(getVPSource());
		associationSource.setTo(to.getVPSource());
		
		association.getToEnd().setName(reference.getName());
		String upperBound = reference.getUpperBound()==-1 ? "*" : reference.getUpperBound().toString();
		association.getToEnd().setMultiplicity(reference.getLowerBound()+".."+upperBound);
		association.getToEnd().setNavigable(IAssociationEnd.NAVIGABLE_NAVIGABLE);
		association.getFromEnd().setNavigable(IAssociationEnd.NAVIGABLE_UNSPECIFIED);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof EntityDeclaration){
			EntityDeclaration entity = (EntityDeclaration) obj;
			String str1 = getFullyQualifiedName();
			String str2 = OntoLModelLoader.getFullyQualifiedName(entity);
			return str1.equals(str2);
		}
		return super.equals(obj);
	}

	public void removeInstantiationTo(VPClass todelete) {
		// TODO Implement deletion
	}

	public Set<VPAttribute> getAttributes() {
		Set<VPAttribute> attSet = new HashSet<VPAttribute>();
		IAttribute[] atts = getVPSource().toAttributeArray();
		
		if(atts==null || atts.length==0)	return attSet;

		for (IAttribute att : atts)
			attSet.add((VPAttribute) VPModelElement.wrap(att));
		return attSet;
	}

	public void addAttribute(Attribute att) {
		IModelElementFactory factory = IModelElementFactory.instance();
		VPAttribute vp_att = (VPAttribute) VPModelElement.wrap(factory.createAttribute());

		getVPSource().addAttribute(vp_att.getVPSource());
		vp_att.getVPSource().setName(att.getName());
		String upperBound = att.getUpperBound()==-1 ? "*" : att.getUpperBound().toString();
		vp_att.getVPSource().setMultiplicity(att.getLowerBound()+".."+upperBound);
		
		String fqn = OntoLModelLoader.getFullyQualifiedName(att.getPropertyType());
		VPClass type = (VPClass) VPModelAccess.getModelElement(fqn);
		vp_att.getVPSource().setType(type.getVPSource());
	}
	
}
