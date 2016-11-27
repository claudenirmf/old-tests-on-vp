package br.ufes.inf.nemo.ontol.vp.access;

import com.vp.plugin.model.IGeneralization;
import com.vp.plugin.model.IModelElement;

/** 
 * A concrete wrapper for manipulation of Visual Paradigm's IGeneralization instances. 
 * @author Claudenir Fonseca
 * */
public class VPGeneralization extends VPModelElement {

	VPGeneralization(IGeneralization sourceEntity) {
		super(sourceEntity);
	}
	
	@Override
	public IGeneralization getVPSource() {
		return (IGeneralization) super.getVPSource();
	}
	
	public VPClass getSuperClass() {
		IModelElement e = getVPSource().getFrom();
		String eName = VPModelElement.getFullyQualifiedName(e);
		return (VPClass) VPModelAccess.getModelElement(eName);
	}

	private VPClass getSubClass() {
		IModelElement e = getVPSource().getTo();
		String eName = VPModelElement.getFullyQualifiedName(e);
		return (VPClass) VPModelAccess.getModelElement(eName);
	}
	
	@Override
	public String toString() {
		return super.toString() + ", [" + getSuperClass() 
				+ "] <- [" + getSubClass() + "]";
	}

}
