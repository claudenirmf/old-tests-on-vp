package br.ufes.inf.nemo.ontol.vp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.view.IDialog;
import com.vp.plugin.view.IDialogHandler;

import br.ufes.inf.nemo.ontol.vp.OntoLPluginForVP;
//import br.ufes.inf.nemo.ontol.vp.ontol_model_manager.OntoLModelManager;

public class SynchronizationMenuDialog implements IDialogHandler {

//	private IDialog _dialog;
	private Component _component;
	private JPanel _menu, _north;
	
	private JLabel _northText = new JLabel("OntoL Project Path");
	
	private JTextField _projectPath = new JTextField(15);
	
	private JButton _loadButton = new JButton("Load");
	private JButton _clearButton = new JButton("Remove Non-mapping Entries");
	
	@Override
	public Component getComponent() {
		_menu = new JPanel(new BorderLayout());
//		_menu.setSize(new Dimension(400,400));
		
		_north = new JPanel(new FlowLayout(FlowLayout.CENTER,4,4));
		_menu.add(_north,BorderLayout.NORTH);
		_north.add(_northText);
		_north.add(_projectPath);
		_projectPath.setText("C:\\Users\\claud\\git\\OntoL_private\\br.ufes.inf.nemo.ontol.lib\\src-gen\\models\\UFO-A.xmi");
		_projectPath.setColumns(40);
		_north.add(_loadButton);
		_loadButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				try {
////					final Path dirPath = Paths.get(_projectPath.getText());
////					if(!dirPath.toFile().isDirectory())
////						throw new InvalidPathException(_projectPath.getText(), "The path must be of a valid directory");
////					OntoLModelManager.loadModels(dirPath);
//					OntoLModelManager.loadModel(_projectPath.getText());
//					OntoLModelManager.feedProject();
//					ApplicationManager.instance().getViewManager().showMessageDialog(_component,"Models loaded.","Load",JOptionPane.INFORMATION_MESSAGE);
				} catch (InvalidPathException ipe) {
					ApplicationManager.instance().getViewManager().showMessageDialog(_component,ipe.getReason(),"Load",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		_north.add(_clearButton);
		_clearButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
//				OntoLModelManager.clearNonMappingEntries();
			}
		});
		
//		JPanel _buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
//		_menu.add(_buttons,BorderLayout.CENTER);
		
		_component = _menu;
		return _menu;
	}

	@Override
	public void prepare(IDialog dialog) {
//		_dialog = dialog;
		dialog.setModal(true);
		dialog.setTitle("OntoL Synchronization");
		dialog.setResizable(true);
		dialog.pack();
	}

	@Override
	public void shown() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean canClosed() {
		return true;
	}

}
