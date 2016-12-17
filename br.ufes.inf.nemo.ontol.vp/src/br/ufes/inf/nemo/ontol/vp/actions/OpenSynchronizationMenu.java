package br.ufes.inf.nemo.ontol.vp.actions;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;

import br.ufes.inf.nemo.ontol.vp.OntoLPluginForVP;
import br.ufes.inf.nemo.ontol.vp.access.VPModelAccess;
import br.ufes.inf.nemo.ontol.vp.load.OntoLModelLoader;

public class OpenSynchronizationMenu implements VPActionController {

	@Override
	public void performAction(VPAction arg0) {
		ViewManager vm = ApplicationManager.instance().getViewManager();
		vm.clearMessages(OntoLPluginForVP.PLUGIN_ID);
		System.out.println("Open synchronization menu button pressed.");
		
		VPModelAccess.load();
		OntoLModelLoader.clear();
		String path = "C:\\Users\\claud\\git\\OntoL-Maven\\br.ufes.inf.nemo.ontol.parent\\br.ufes.inf.nemo.ontol.lib\\src-gen\\models\\";
		String name = "ufo-a.xmi";
		OntoLModelLoader.loadModel(name,path);
		OntoLModelLoader.feed();
		OntoLModelLoader.update();
		
		System.out.println("Open synchronization action performed.");
	}

	@Override
	public void update(VPAction arg0) {}

}
