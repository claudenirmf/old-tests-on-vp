package br.ufes.inf.nemo.ontol.vp.actions;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;

import br.ufes.inf.nemo.ontol.vp.OntoLPluginForVP;
import br.ufes.inf.nemo.ontol.vp.access.VPModelAccess;
import br.ufes.inf.nemo.ontol.vp.load.OntoLModelLoader;

public class ReloadPlugin implements VPActionController {

	@Override
	public void performAction(VPAction arg0) {
		VPModelAccess.clear();
		OntoLModelLoader.clear();
		ApplicationManager.instance().reloadPluginClasses(OntoLPluginForVP.PLUGIN_ID);
		System.out.println("Plugin reloaded.");
	}

	@Override
	public void update(VPAction arg0) {}

}
