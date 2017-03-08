package br.ufes.inf.nemo.ontol.vp.actions;

import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;
import com.vp.plugin.model.IAssociation;
import com.vp.plugin.model.IAssociationEnd;
import com.vp.plugin.model.IAttribute;
import com.vp.plugin.model.IClass;
import com.vp.plugin.model.IPackage;
import com.vp.plugin.model.factory.IModelElementFactory;

public class TestAction implements VPActionController {
	
//	private Set<IModelElement>	testElements = new HashSet<IModelElement>();

	@Override
	public void performAction(VPAction arg0) {
		//testAssociation();
		testAttribute();
	}

	@Override
	public void update(VPAction arg0) {
//		for (IModelElement elem : testElements)	elem.delete();
//		testElements.clear();
	}
	
	private void testAttribute() {
		IModelElementFactory factory = IModelElementFactory.instance();
		IPackage container = factory.createPackage();	container.setName("Test");
		IClass ca = factory.createClass();	ca.setName("A");	container.addChild(ca);
		IClass cb = factory.createClass();	cb.setName("B");	container.addChild(cb);
		
		IAttribute att = factory.createAttribute();
		ca.addAttribute(att);
		att.setName("att");
		att.setMultiplicity(IAttribute.MULTIPLICITY_ZERO_TO_MANY);
		att.setType(cb);
		
		IAttribute att2 = factory.createAttribute();
		ca.addAttribute(att2);
		att2.setName("att2");
		att2.setMultiplicity(IAttribute.MULTIPLICITY_ZERO_TO_ONE);
		att2.setType(att.getTypeAsElement());
	}
	
	private void testAssociation() {
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

}
