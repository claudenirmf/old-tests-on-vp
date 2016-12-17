package br.ufes.inf.nemo.ontol.vp.access;

import com.vp.plugin.model.IAssociation;
import com.vp.plugin.model.IAssociationEnd;

import br.ufes.inf.nemo.ontol.model.ModelElement;
import br.ufes.inf.nemo.ontol.model.Reference;
import br.ufes.inf.nemo.ontol.vp.load.OntoLModelLoader;

public class VPAssociation extends VPModelElement {

	VPAssociation(IAssociation source) {
		super(source);
	}
	
	@Override
	public IAssociation getVPSource() {
		return (IAssociation) super.getVPSource();
	}
	
	public VPClass getFrom() {
		return (VPClass) VPModelAccess.getModelElement(getFullyQualifiedName(getVPSource().getFrom()));
	}
	
	public VPClass getTo() {
		return (VPClass) VPModelAccess.getModelElement(getFullyQualifiedName(getVPSource().getTo()));
	}
	
	public IAssociationEnd getToEnd(){
		return (IAssociationEnd) getVPSource().getToEnd();
	}
	
	public IAssociationEnd getFromEnd(){
		return (IAssociationEnd) getVPSource().getFromEnd();
	}
	
	@Override
	public boolean equals(Object obj) {
		Reference reference;
		if (obj instanceof Reference)
			reference = (Reference) obj;
		else
			return super.equals(obj);
		
		// Compare target end name and reference name
		if(!reference.getName().equals(getVPSource().getToEnd().getName()))
			return false;
		// Compare reference container and association source
		String fqn1 = OntoLModelLoader.getFullyQualifiedName((ModelElement) reference.eContainer());
		String fqn2 = getFrom().getFullyQualifiedName();
		if(!fqn1.equals(fqn2))
			return false;
		// Compare reference target and association target
		String fqn3 = OntoLModelLoader.getFullyQualifiedName(reference.getPropertyClass());
		String fqn4 = getTo().getFullyQualifiedName();
		if(!fqn3.equals(fqn4))
			return false;
		
		return true;
	}

	public void update(Reference reference) {
		String multiplicity = reference.getLowerBound()+".."+reference.getUpperBound();
		getToEnd().setMultiplicity(multiplicity);
		// TODO Add subsets and opposite
	}
	
	@Override
	public String toString() {
		String s = "[from " + getFromEnd().getName() + " to " + getToEnd().getName() + "]";
		return s;
	}

}
