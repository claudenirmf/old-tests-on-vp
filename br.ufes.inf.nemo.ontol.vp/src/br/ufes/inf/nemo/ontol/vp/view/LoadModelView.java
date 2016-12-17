package br.ufes.inf.nemo.ontol.vp.view;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.view.IDialog;
import com.vp.plugin.view.IDialogHandler;

import br.ufes.inf.nemo.ontol.vp.access.VPModelAccess;
import br.ufes.inf.nemo.ontol.vp.load.OntoLModelLoader;

public class LoadModelView implements IDialogHandler {
	
	private IDialog _dialog;
	private Component _component;
	private JTextField _path =  new JTextField("C:\\Users\\claud\\Documents\\Workspaces\\runtime-EclipseApplication\\Linus Project\\src-gen\\models\\ufo-a.xmi");
	private JButton _load = new JButton();
	private boolean CAN_CLOSE = true;
	
	@Override
	public Component getComponent() {
		JPanel _menu = new JPanel(new FlowLayout(FlowLayout.CENTER));
		_menu.add(_path);
		_path.setColumns(50);
		_menu.add(_load);
		_load.setText("Load Model");

		ActionListener loadAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_dialog.close();
				String fpath = _path.getText();
				String name = fpath.substring(fpath.lastIndexOf('\\'));
				String path = fpath.substring(0,fpath.lastIndexOf('\\'));
				VPModelAccess.load();
				OntoLModelLoader.clear();
				OntoLModelLoader.loadModel(name,path);
				OntoLModelLoader.feed();
				OntoLModelLoader.update();
				ApplicationManager.instance().getViewManager().showMessageDialog(_component,"Models loaded.","Load",JOptionPane.INFORMATION_MESSAGE);
			}
		};
		
		_path.addActionListener(loadAction);
		_load.addActionListener(loadAction);

		_component = _menu;
		return _menu;
	}

	@Override
	public void prepare(IDialog dialog) {
		_dialog = dialog;
		_dialog.setModal(true);
		_dialog.setTitle("Load Model");
		_dialog.setSize(220, _dialog.getHeight());
		_dialog.setResizable(false);
		_dialog.pack();
	}

	@Override
	public void shown() {}

	@Override
	public boolean canClosed() { return CAN_CLOSE; }
	
}

