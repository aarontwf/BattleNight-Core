package me.limebyte.battlenight.core.old.Other;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import me.limebyte.battlenight.core.old.BattleNight;

public class GUI {
	private JFrame frmBattleNight;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GUI window = new GUI();
					window.frmBattleNight.setVisible(true);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
 
	public GUI() {
		initialize();
	}

	private void initialize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		this.frmBattleNight = new JFrame();
		this.frmBattleNight.setAlwaysOnTop(true);
		this.frmBattleNight.setResizable(false);
		this.frmBattleNight.setIconImage(Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("/me/limebyte/battlenight/media/Warning.gif")));
		this.frmBattleNight.setTitle("BattleNight " + BattleNight.Version);
		this.frmBattleNight.setBounds(100, 100, 345, 168);
		this.frmBattleNight.setLocation(screenWidth / 2 - 172, screenHeight / 2 - 94);
		this.frmBattleNight.setDefaultCloseOperation(3);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(GUI.class.getResource("/me/limebyte/battlenight/media/Frame.gif")));
		this.frmBattleNight.getContentPane().add(label, "North");
	}
}