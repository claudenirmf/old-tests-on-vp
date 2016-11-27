package br.ufes.inf.nemo.ontol.vp.access;

import com.vp.plugin.model.IPackage;

/** 
 * A concrete wrapper for manipulation of Visual Paradigm's IPackage instances. 
 * @author Claudenir Fonseca
 * */
public class VPPackage extends VPModelElement {

	VPPackage(IPackage source) {
		super(source);
	}
	
	@Override
	public IPackage getVPSource() {
		return (IPackage) super.getVPSource();
	}

}
