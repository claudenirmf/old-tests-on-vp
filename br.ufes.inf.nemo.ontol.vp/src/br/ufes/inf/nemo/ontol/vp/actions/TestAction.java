package br.ufes.inf.nemo.ontol.vp.actions;

import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;
import com.vp.plugin.model.IAssociation;
import com.vp.plugin.model.IAssociationEnd;
import com.vp.plugin.model.IClass;
import com.vp.plugin.model.IPackage;
import com.vp.plugin.model.factory.IModelElementFactory;

public class TestAction implements VPActionController {
	
//	private Set<IModelElement>	testElements = new HashSet<IModelElement>();

	@Override
	public void performAction(VPAction arg0) {
		IModelElementFactory factory = IModelElementFactory.instance();
		
		IPackage container = factory.createPackage();
		IClass ca = factory.createClass();	ca.setName("A");	container.addChild(ca);
		IClass cb = factory.createClass();	cb.setName("B");	container.addChild(cb);
		IAssociation atob = factory.createAssociation();	container.addChild(atob);
		
		atob.setName("A to B");	atob.setFrom(ca);	atob.setTo(cb);
		
		IAssociationEnd toEnd = (IAssociationEnd) atob.getToEnd();
		IAssociationEnd fromEnd = (IAssociationEnd) atob.getFromEnd();
		
		toEnd.setMultiplicity(IAssociationEnd.MULTIPLICITY_ONE_TO_MANY);
		fromEnd.setMultiplicity(IAssociationEnd.MULTIPLICITY_ZERO_TO_ONE);
		
		toEnd.setName("{someconstraint}");
		
		toEnd.setNavigable(IAssociationEnd.NAVIGABLE_NAVIGABLE);
		fromEnd.setNavigable(IAssociationEnd.NAVIGABLE_UNSPECIFIED);
		
//		testElements.add(container);
//		testElements.add(ca);
//		testElements.add(cb);
//		testElements.add(atob);
	}

	@Override
	public void update(VPAction arg0) {
//		for (IModelElement elem : testElements)	elem.delete();
//		testElements.clear();
	}

}
