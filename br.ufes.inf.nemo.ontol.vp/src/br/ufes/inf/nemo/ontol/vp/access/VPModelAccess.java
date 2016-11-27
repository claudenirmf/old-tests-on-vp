package br.ufes.inf.nemo.ontol.vp.access;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.model.IAssociationEnd;
import com.vp.plugin.model.IClass;
import com.vp.plugin.model.IModelElement;
import com.vp.plugin.model.IPackage;

public class VPModelAccess {
	
	private static ConcurrentHashMap<String, VPModelElement> vpModelCopy;
	
	private static ConcurrentHashMap<String, VPModelElement> getModelCopy(){
		if(vpModelCopy == null){
			vpModelCopy = new ConcurrentHashMap<String, VPModelElement>();
		}
		return vpModelCopy;
	}

	public synchronized static VPModelElement getModelElement(String fqn) {
		return getModelCopy().get(fqn);
	}
	
	public synchronized static VPModelElement register(VPModelElement wrapper) {
//		System.out.println("Registered " + wrapper.getFullyQualifiedName());
		return getModelCopy().put(wrapper.getFullyQualifiedName(), wrapper);
	}
	
	public synchronized static VPModelElement remove(String fqn){
		return getModelCopy().remove(fqn);
	}
	
	public synchronized static void clear() {
		getModelCopy().clear();
	}
	
	public synchronized static Iterable<VPModelElement> iterator(){
		return getModelCopy().values();
	}
	
	@SuppressWarnings("unchecked")
	public static void load() {
		final Iterator<IModelElement> iterator = ApplicationManager.instance().getProjectManager().getProject()
				.allLevelModelElementIterator();
		while (iterator.hasNext()) {
			IModelElement e = iterator.next();
			if (e instanceof IClass || e instanceof IPackage) 
				register(VPModelElement.wrap(e));
		}
	}
	
	public static void main(String[] args) {
		System.out.println("ONE "+IAssociationEnd.MULTIPLICITY_ONE);
		System.out.println("ONE_TO_MANY "+IAssociationEnd.MULTIPLICITY_ONE_TO_MANY);
		System.out.println("UNSPECIFIED "+IAssociationEnd.MULTIPLICITY_UNSPECIFIED);
		System.out.println("Z_TO_MANY "+IAssociationEnd.MULTIPLICITY_ZERO_TO_MANY);
	}
	
}
