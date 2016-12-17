package br.ufes.inf.nemo.ontol.vp.actions;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;
import com.vp.plugin.view.IDialogHandler;

import br.ufes.inf.nemo.ontol.vp.view.LoadModelView;

public class LoadModel implements VPActionController {

	@Override
	public void performAction(VPAction action) {
		ViewManager vm =  ApplicationManager.instance().getViewManager();
		IDialogHandler d = new LoadModelView();
		vm.showDialog(d);
	}

	@Override
	public void update(VPAction action) {}

}
