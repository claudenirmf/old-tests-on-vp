package br.ufes.inf.nemo.ontol.vp;

import com.vp.plugin.VPPlugin;
import com.vp.plugin.VPPluginInfo;

public class OntoLPluginForVP implements VPPlugin {

	public static final String PLUGIN_ID = "br.ufes.inf.nemo.ontol.vp";
	
	@Override
	public void loaded(VPPluginInfo arg0) {
//		registerListeners();
		System.out.println("Plugin "+PLUGIN_ID+" loaded.");
	}

//	private void registerListeners() {
//		registerProjectListener();
//		registerModelListener();
//	}

//	private void registerProjectListener() {
//		ApplicationManager.instance().getProjectManager().getProject().addProjectListener(new IProjectListener(){
//			@Override public void projectSaved(IProject arg0) {
//				// TODO Implement a more efficient version of this method
//				if(!VPModelManager.isModelCopyEmpty())
//					VPModelManager.clearModelCopy();
//				VPModelManager.loadVPModel();
//			}
//			@Override public void projectRenamed(IProject arg0) {}
//			@Override public void projectPreSave(IProject arg0) {}
//			@Override public void projectOpened(IProject arg0) {
//				if(!VPModelManager.isModelCopyEmpty())
//					VPModelManager.clearModelCopy();
//				VPModelManager.loadVPModel();
//			}
//			@Override public void projectNewed(IProject arg0) {}
//			@Override public void projectAfterOpened(IProject arg0) {}
//		});
//	}

//	private void registerModelListener() {
//		ApplicationManager.instance().getProjectManager().getProject().addProjectModelListener(new IProjectModelListener() {
//			@Override public void modelAdded(IProject arg0, IModelElement arg1) {}
//			@Override public void modelRemoved(IProject arg0, IModelElement arg1) {
//				VPModelManager.unregisterModelElemWrap(VPModelElemWrap.getFullyQualifiedName(arg1));
//			}
//		});
//	}
	
	@Override
	public void unloaded() {
		System.out.println(PLUGIN_ID+" unloaded.");
//		VPModelManager.clearModelCopy();
//		OntoLModelManager.clearResourceSet();
	}

}
