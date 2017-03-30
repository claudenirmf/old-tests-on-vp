package br.ufes.inf.nemo.ontol.vp.access;

import com.vp.plugin.model.IAttribute;

import br.ufes.inf.nemo.ontol.model.Attribute;
import br.ufes.inf.nemo.ontol.model.ModelElement;
import br.ufes.inf.nemo.ontol.vp.load.OntoLModelLoader;

public class VPAttribute extends VPModelElement {

	VPAttribute(IAttribute att) {
		super(att);
	}
	
	@Override
	public IAttribute getVPSource() {
		return (IAttribute) super.getVPSource();
	}
	
	@Override
	public boolean equals(Object obj) {
		Attribute attribute;
		if (obj instanceof Attribute)	attribute = (Attribute) obj;
		else 							return super.equals(obj);
		
		// Compare VP's attribute name and attribute name
		if(!attribute.getName().equals(getVPSource().getName()))
			return false;
		// Compare parent class name and container ontol class name
		String fqn1 = OntoLModelLoader.getFullyQualifiedName((ModelElement) attribute.eContainer());
		String fqn2 = getParent().getFullyQualifiedName();
		if(!fqn1.equals(fqn2))
			return false;
		// Compare attribute type name and property type name
		String fqn3 = OntoLModelLoader.getFullyQualifiedName((ModelElement) attribute.getPropertyType());
		String fqn4 = getType().getFullyQualifiedName();
		if(!fqn3.equals(fqn4))
			return false;

		return true;
	}

	public VPClass getType() {
		String fqn = getFullyQualifiedName(getVPSource().getTypeAsElement());
		VPClass vpc = (VPClass) VPModelAccess.getModelElement(fqn);
		return vpc;
	}

	public VPClass getParent() {
		String fqn = VPModelElement.getFullyQualifiedName(getVPSource().getParent());
		return (VPClass) VPModelAccess.getModelElement(fqn);
	}

	public void update(Attribute attribute) {
		String upperBound = attribute.getUpperBound()==-1 ? "*" : attribute.getUpperBound().toString() ;
		String multiplicity = attribute.getLowerBound()+".."+upperBound;
		getVPSource().setMultiplicity(multiplicity);
	}

}
