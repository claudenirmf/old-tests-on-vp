package br.ufes.inf.nemo.ontol.vp.access;

import java.util.ArrayList;
import java.util.List;

import com.vp.plugin.model.IGeneralization;
import com.vp.plugin.model.IGeneralizationSet;
import com.vp.plugin.model.IModelElement;

/** 
 * A concrete wrapper for manipulation of Visual Paradigm's IGeneralizationSet instances. 
 * @author Claudenir Fonseca
 * */
public class _VPGenSetWrap extends VPModelElement {

	_VPGenSetWrap(IGeneralizationSet source) {
		super(source);
	}
	
	@Override
	public IGeneralizationSet getVPSource(){
		return (IGeneralizationSet) super.getVPSource();
	}
	
	public String getPowerTypeId(){
		final IModelElement powerType = getVPSource().getPowerType();
		if (powerType!=null) {
			return powerType.getId();
		}
		return null;
	}
	
	public VPClass getPowerType() {
		return (VPClass) VPModelAccess.getModelElement(getPowerTypeId());
	}

	/*
	 * The method is null safe, because a generalization set will have at least one generalization
	 */
	public String getSuperTypeId(){
		return getVPSource().toGeneralizationArray()[0].getFrom().getId();
	}
	
	public VPClass getSuperType() {
		return (VPClass) VPModelAccess.getModelElement(getSuperTypeId());
	}

	/*
	 * The method is null safe, because a generalization set will have at least one generalization
	 */
	public String[] getSubTypesId(){
		final List<String> subTypesIds = new ArrayList<String>();
		for (IGeneralization generalization : getVPSource().toGeneralizationArray()) {
			subTypesIds.add(generalization.getTo().getId());
		}
		return subTypesIds.toArray(new String[0]);
	}
	
	public boolean isDisjoint(){
		return getVPSource().isDisjoint();
	}
	
	public boolean isCovering(){
		return getVPSource().isCovering();
	}
	
	@Override
	public String report() {
		return "SET, ID="+getId()
				+", SUPER_ID="+getSuperTypeId()
				+", N_SUBTYPES="+getSubTypesId().length
				+", POWERTTYPE="+getPowerTypeId();
	}
	
}
