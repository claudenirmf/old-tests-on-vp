package br.ufes.inf.nemo.ontol.vp.access;

import com.vp.plugin.model.IClass;
import com.vp.plugin.model.IDependency;
import com.vp.plugin.model.IStereotype;

import br.ufes.inf.nemo.ontol.model.CategorizationType;
import br.ufes.inf.nemo.ontol.model.ModelPackage;
import br.ufes.inf.nemo.ontol.model.OntoLClass;
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
		String[] strs = getVPSource().toStereotypeArray();
		if(strs == null)	return null;
		
		for (String str : strs) {
			if(str.equals(VPDependencyType.INSTANTIATION.str()))
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
		return null;
	}

	public void delete() {
		getVPSource().delete();
	}

	public boolean categorizes(OntoLClass cat_base, CategorizationType cat_type) {
		if(!isCategorization())
			return false;
		else if(!getTarget().equals(cat_base))
			return false;
		else if(getType()==null || getType().convert() != cat_type)
			return false;
		else
			return true;
	}

	public boolean isCategorization() {
		VPDependencyType type = getType();
		return type==VPDependencyType.CATERGORIZATION ||
			type==VPDependencyType.COMPLETE_CATEGORIZATION ||
			type==VPDependencyType.DISJOINT_CATEGORIZATION ||
			type==VPDependencyType.PARTITIONING;
	}
	
	@Override
	public String toString() {
		return getSource().getFullyQualifiedName() + " " + getType() + 
				" " + getTarget().getFullyQualifiedName() + " " + getId();
	}

	public boolean isPowertypeOf(OntoLClass pwt_base, OntoLClass oc) {
		if(!isPowertype())
			return false;
		else if(!getTarget().equals(pwt_base))
			return false;
		else if (getType() == null || 
				oc.eIsSet(ModelPackage.eINSTANCE.getOntoLClass_CategorizationType()))
			return false;
		else
			return true;
	}

	private boolean isPowertype() {
		return getType()==VPDependencyType.POWERTYPING;
	}
	
}
