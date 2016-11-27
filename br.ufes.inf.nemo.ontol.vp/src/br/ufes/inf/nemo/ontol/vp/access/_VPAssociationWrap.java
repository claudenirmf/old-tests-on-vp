package br.ufes.inf.nemo.ontol.vp.access;

import com.vp.plugin.model.IAssociation;
import com.vp.plugin.model.IAssociationEnd;
import com.vp.plugin.model.IModelElement;

/** 
 * A concrete wrapper for manipulation of Visual Paradigm's IAssociation instances. 
 * @author Claudenir Fonseca
 * */
public class _VPAssociationWrap extends VPModelElement {
	
	public _VPAssociationWrap(IAssociation source) {
		super(source);
	}
	
	@Override
	public IAssociation getVPSource(){
		return (IAssociation) super.getVPSource();
	}

	public String getTargetElementId() {
		final IModelElement ret = getVPSource().getTo();
		if(ret!=null){
			return ret.getId();
		}
		return null;
	}

	public VPClass getTargetElement() {
		return (VPClass) VPModelAccess.getModelElement(getTargetElementId());
	}

	public String getTargetEndCardinality() {
		final IAssociationEnd ret = (IAssociationEnd) getVPSource().getToEnd();
		return ret.getMultiplicity();
	}

	public String getSourceElementId() {
		final IModelElement ret = getVPSource().getFrom();
		if (ret!=null) {
			return ret.getId();
		}
		return null;
	}

	public VPClass getSourceElement() {
		return (VPClass) VPModelAccess.getModelElement(getSourceElementId());
	}

	public String getSourceEndCardinality() {
		final IAssociationEnd ret = (IAssociationEnd) getVPSource().getFromEnd();
		if (ret!=null) {
			return ret.getMultiplicity();
		}
		return null;
	}

	public String[] getStereotypeList() {
		return getVPSource().toStereotypeArray();
	}
	
	@Override
	public String report(){
		return "ASSOCIATION, NAME="+getName()
				+", ID="+getId()
				+", N_STR="+getStereotypeList().length
				+", TARGET_ID="+getTargetElementId()
				+", SOURCE_ID="+getSourceElementId();
	}
	
}