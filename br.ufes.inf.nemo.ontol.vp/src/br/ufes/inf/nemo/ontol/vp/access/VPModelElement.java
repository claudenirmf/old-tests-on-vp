package br.ufes.inf.nemo.ontol.vp.access;

import com.vp.plugin.model.IAssociation;
import com.vp.plugin.model.IClass;
import com.vp.plugin.model.IDependency;
import com.vp.plugin.model.IGeneralization;
import com.vp.plugin.model.IModelElement;
import com.vp.plugin.model.IPackage;

/** 
 * An abstract wrapper for manipulation of Visual Paradigm's IModelElement instances. 
 * @author Claudenir Fonseca
 * */
public abstract class VPModelElement {
	
	private final IModelElement sourceEntity;
	
	public static VPModelElement wrap(IModelElement sourceEntity){
		if(sourceEntity instanceof IClass){
			return new VPClass((IClass) sourceEntity);
		} else if(sourceEntity instanceof IGeneralization){
			return new VPGeneralization((IGeneralization) sourceEntity);
		} else if(sourceEntity instanceof IPackage){
			return new VPPackage((IPackage) sourceEntity);
		} else if(sourceEntity instanceof IDependency){
			return new VPDependency((IDependency) sourceEntity);
		} else if(sourceEntity instanceof IAssociation){
			return new VPAssociation((IAssociation) sourceEntity);
//		}  else if(sourceEntity instanceof IGeneralizationSet){
//			return new _VPGenSetWrap((IGeneralizationSet) sourceEntity);
		} else {
			return null;
		}
	}
	
	public static String getFullyQualifiedName(IModelElement e){
		String fqn = ""+e.getName();
		IModelElement it = e.getParent();
		while(it!=null){
			fqn = it.getName() + "." + fqn;
			it = it.getParent();
		};
		return fqn;
	}
	
	VPModelElement(IModelElement source){ 
		sourceEntity = source;
	}
	
	public IModelElement getVPSource(){	
		return sourceEntity;
	}
	
	public String getName(){
		return getVPSource().getName();
	}
	
	public String getFullyQualifiedName(){
		return getFullyQualifiedName(getVPSource());
	}
	
	public String getId(){
		return getVPSource().getId();
	}

	public String smallReport(){
		return getVPSource().getName()+", ID: "+getId();
	}

	public String report(){
		return "Model Element(NAME="
			+getVPSource().getName()
			+", ID="
			+getVPSource().getId()
			+")";
	}
	
	@Override
	public String toString() {
		return getFullyQualifiedName() + " ID:" + getId();
	}
	
}
