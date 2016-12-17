package br.ufes.inf.nemo.ontol.vp.access;

import com.vp.plugin.model.IClass;
import com.vp.plugin.model.IDependency;
import com.vp.plugin.model.IStereotype;

import br.ufes.inf.nemo.ontol.vp.load.OntoLModelLoader;

/**
 * Only works for dependencies between classes
 */
public class VPDependency extends VPModelElement {

	VPDependency(IDependency source) {
		super(source);
	}
	
	@Override
	public IDependency getVPSource() {
		return (IDependency) super.getVPSource();
	}
	
	public VPClass getTarget() {
		IClass target = (IClass) getVPSource().getTo();
		String fqn = VPModelElement.getFullyQualifiedName(target);
		return (VPClass) VPModelAccess.getModelElement(fqn);
	}
	
	public VPClass getSource() {
		IClass source = (IClass) getVPSource().getFrom();
		String fqn = VPModelElement.getFullyQualifiedName(source);
		return (VPClass) VPModelAccess.getModelElement(fqn);
	}

	public void setTarget(VPClass vpc) {
		getVPSource().setTo(vpc.getVPSource());
	}
	
	public void setSource(VPClass vpc) {
		getVPSource().setFrom(vpc.getVPSource());
	}

	public void addStereotype(String strName) {
		IStereotype str = OntoLModelLoader.getStereotypeByNameAndBaseType(strName, OntoLModelLoader.BASE_TYPE_DEPENDENCY);
		getVPSource().addStereotype(str);
	}
	
	public VPDependencyType getType(){
		String[] strs = getSource().getStereotypeList();
		if(strs == null)	return VPDependencyType.NONE;
		
		for (String str : strs) {
			if(str == VPDependencyType.INSTANTIATION.str())
				return VPDependencyType.INSTANTIATION;
			else if(str.equals(VPDependencyType.SUBORDINATION.str()))
				return VPDependencyType.SUBORDINATION;
			else if(str.equals(VPDependencyType.CATERGORIZATION.str()))
				return VPDependencyType.CATERGORIZATION;
			else if(str.equals(VPDependencyType.COMPLETE_CATEGORIZATION.str()))
				return VPDependencyType.COMPLETE_CATEGORIZATION;
			else if(str.equals(VPDependencyType.DISJOINT_CATEGORIZATION.str()))
				return VPDependencyType.DISJOINT_CATEGORIZATION;
			else if(str.equals(VPDependencyType.PARTITIONING.str()))
				return VPDependencyType.PARTITIONING;
			else if(str.equals(VPDependencyType.POWERTYPING.str()))
				return VPDependencyType.POWERTYPING;
		}
		return VPDependencyType.NONE;
	}
	
}
