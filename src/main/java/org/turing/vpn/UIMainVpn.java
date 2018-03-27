package org.turing.vpn;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.SwingConstants;

public class UIMainVpn {
	private JFrame frmvpn;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIMainVpn window = new UIMainVpn();
					window.frmvpn.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public UIMainVpn() {
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmvpn = new JFrame();
		frmvpn.setTitle("盘古VPN管理器5.2");
		frmvpn.setBounds(100, 100, 1139, 938);
		frmvpn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmvpn.getContentPane().setLayout(null);
		
		JLabel lblipVpn = new JLabel("动态IP VPN");
		lblipVpn.setBounds(510, 13, 126, 21);
		frmvpn.getContentPane().add(lblipVpn);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(510, 49, 209, 188);
		frmvpn.getContentPane().add(scrollPane_3);
		
		JList dymanicVpnList = new JList();
		dymanicVpnList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				PanguVpnMng.getInstance().dynamicVpnValueChanged(e);
			}
		});
		scrollPane_3.setViewportView(dymanicVpnList);
		
		JLabel lblvpn = new JLabel("动态VPN详细信息");
		lblvpn.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				PanguVpnMng.getInstance().setHost();
			}
		});
		lblvpn.setBounds(801, 15, 141, 21);
		frmvpn.getContentPane().add(lblvpn);
		
		JLabel lblip_1 = new JLabel("当前IP:");
		lblip_1.setBounds(510, 252, 72, 21);
		frmvpn.getContentPane().add(lblip_1);
		
		JLabel curIP = new JLabel("0.0.0.0");
		curIP.setBackground(Color.GREEN);
		curIP.setBounds(611, 252, 157, 21);
		frmvpn.getContentPane().add(curIP);
		
		JLabel vpn = new JLabel("VPN类型:");
		vpn.setBounds(510, 335, 81, 21);
		frmvpn.getContentPane().add(vpn);
		
		JComboBox vpnType = new JComboBox();
		vpnType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PanguVpnMng.getInstance().activeVpnType();
			}
		});
		vpnType.setModel(new DefaultComboBoxModel(new String[] {"固态IP", "动态IP"}));
		vpnType.setSelectedIndex(0);
		vpnType.setBounds(610, 332, 221, 27);
		frmvpn.getContentPane().add(vpnType);
		
		JLabel kkk = new JLabel("VPN用途:");
		kkk.setBounds(515, 453, 92, 21);
		frmvpn.getContentPane().add(kkk);
		
		JComboBox vpnFunction = new JComboBox();
		vpnFunction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PanguVpnMng.getInstance().activeVpnFunction();
			}
		});
		vpnFunction.setModel(new DefaultComboBoxModel(new String[] {"赚钱", "水军"}));
		vpnFunction.setSelectedIndex(0);
		vpnFunction.setBounds(611, 450, 220, 27);
		frmvpn.getContentPane().add(vpnFunction);
		
		JLabel lblip = new JLabel("固态IP");
		lblip.setBounds(104, 13, 182, 21);
		frmvpn.getContentPane().add(lblip);
		
		JButton startConnectMoney = new JButton("开始连接(赚钱)");
		startConnectMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PanguVpnMng.getInstance().startConnectMoney();
			}
		});
		startConnectMoney.setBounds(510, 644, 251, 57);
		frmvpn.getContentPane().add(startConnectMoney);
		
		JButton closeConnect = new JButton("断开连接");
		closeConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PanguVpnMng.getInstance().closeConnect();
			}
		});
		closeConnect.setBounds(510, 799, 542, 57);
		frmvpn.getContentPane().add(closeConnect);
		
		JLabel label_4 = new JLabel("成功次数:");
		label_4.setBounds(852, 252, 81, 21);
		frmvpn.getContentPane().add(label_4);
		
		JLabel label_5 = new JLabel("失败次数:");
		label_5.setBounds(854, 288, 81, 21);
		frmvpn.getContentPane().add(label_5);
		
		JLabel okCount = new JLabel("0");
		okCount.setBounds(948, 252, 81, 21);
		frmvpn.getContentPane().add(okCount);
		
		JLabel noCount = new JLabel("0");
		noCount.setBounds(948, 288, 81, 21);
		frmvpn.getContentPane().add(noCount);
		
		JLabel vpnNameL = new JLabel("选择名称：");
		vpnNameL.setBounds(510, 397, 102, 21);
		frmvpn.getContentPane().add(vpnNameL);
		
		JComboBox vpnName = new JComboBox();
		vpnName.setModel(new DefaultComboBoxModel(new String[] {"请选择"}));
		vpnName.setSelectedIndex(0);
		vpnName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PanguVpnMng.getInstance().activeVpnName();
			}
		});
		vpnName.setBounds(610, 394, 221, 27);
		frmvpn.getContentPane().add(vpnName);
		
		JScrollPane scrollConnectState = new JScrollPane();
		scrollConnectState.setBounds(15, 49, 387, 188);
		frmvpn.getContentPane().add(scrollConnectState);
		
		JList staticVpnlist = new JList();
		staticVpnlist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				PanguVpnMng.getInstance().staticVpnValueChanged(arg0);
			}
		});
		scrollConnectState.setViewportView(staticVpnlist);
		
		JScrollPane scrollPane_5 = new JScrollPane();
		scrollPane_5.setBounds(785, 49, 251, 188);
		frmvpn.getContentPane().add(scrollPane_5);
		
		JList dymanicIpList = new JList();
		scrollPane_5.setViewportView(dymanicIpList);
		
		JButton btnFreshDymanicVpn = new JButton("刷新");
		btnFreshDymanicVpn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PanguVpnMng.getInstance().refreshDymanicVpn();
			}
		});
		btnFreshDymanicVpn.setForeground(Color.BLUE);
		btnFreshDymanicVpn.setBounds(633, 11, 86, 29);
		frmvpn.getContentPane().add(btnFreshDymanicVpn);

		
		JLabel lblip_2 = new JLabel("初始IP:");
		lblip_2.setBounds(510, 287, 80, 21);
		frmvpn.getContentPane().add(lblip_2);
		
		JLabel initIp = new JLabel("0.0.0.0");
		initIp.setBackground(Color.GREEN);
		initIp.setBounds(610, 287, 157, 21);
		frmvpn.getContentPane().add(initIp);
		
		
		JLabel label_1 = new JLabel("增量总任务:");
		label_1.setBounds(846, 383, 146, 21);
		frmvpn.getContentPane().add(label_1);
		
		JLabel increTotalCount = new JLabel("0");
		increTotalCount.setForeground(Color.RED);
		increTotalCount.setBackground(Color.GREEN);
		increTotalCount.setBounds(1030, 383, 72, 21);
		frmvpn.getContentPane().add(increTotalCount);
		
		JLabel label_8 = new JLabel("存量总任务:");
		label_8.setBounds(846, 418, 140, 21);
		frmvpn.getContentPane().add(label_8);
		
		JLabel stockTotalCount = new JLabel("0");
		stockTotalCount.setForeground(Color.RED);
		stockTotalCount.setBackground(Color.GREEN);
		stockTotalCount.setBounds(1030, 418, 60, 21);
		frmvpn.getContentPane().add(stockTotalCount);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 264, 387, 603);
		frmvpn.getContentPane().add(scrollPane);
		
		JList staticIplist = new JList();
		scrollPane.setViewportView(staticIplist);
		
		JLabel label = new JLabel("模拟器数:");
		label.setBounds(417, 533, 92, 21);
		frmvpn.getContentPane().add(label);
		
		JLabel label_2 = new JLabel("已分配增量任务:");
		label_2.setBounds(846, 454, 146, 21);
		frmvpn.getContentPane().add(label_2);
		
		JLabel increAllocCount = new JLabel("0");
		increAllocCount.setForeground(Color.RED);
		increAllocCount.setBackground(Color.GREEN);
		increAllocCount.setBounds(1030, 454, 53, 21);
		frmvpn.getContentPane().add(increAllocCount);
		
		JLabel label_7 = new JLabel("已分配存量任务:");
		label_7.setBounds(846, 489, 140, 21);
		frmvpn.getContentPane().add(label_7);
		
		JLabel stockAllocCount = new JLabel("0");
		stockAllocCount.setForeground(Color.RED);
		stockAllocCount.setBackground(Color.GREEN);
		stockAllocCount.setBounds(1030, 489, 60, 21);
		frmvpn.getContentPane().add(stockAllocCount);
		
		JLabel label_10 = new JLabel("已完任务(正常):");
		label_10.setBounds(846, 525, 146, 21);
		frmvpn.getContentPane().add(label_10);
		
		JLabel finishedOKCount = new JLabel("0");
		finishedOKCount.setForeground(Color.RED);
		finishedOKCount.setBackground(Color.GREEN);
		finishedOKCount.setBounds(1030, 525, 53, 21);
		frmvpn.getContentPane().add(finishedOKCount);
		
		JLabel label_12 = new JLabel("已完任务(非正常):");
		label_12.setBounds(846, 560, 164, 21);
		frmvpn.getContentPane().add(label_12);
		
		JLabel finishedFailCount = new JLabel("0");
		finishedFailCount.setForeground(Color.RED);
		finishedFailCount.setBackground(Color.GREEN);
		finishedFailCount.setBounds(1030, 560, 53, 21);
		frmvpn.getContentPane().add(finishedFailCount);
		
		JLabel label_14 = new JLabel("本机编号:");
		label_14.setHorizontalAlignment(SwingConstants.LEFT);
		label_14.setBounds(846, 335, 96, 21);
		frmvpn.getContentPane().add(label_14);
		
		JButton startConnectWaterAmy = new JButton("开始连接(水军)");
		startConnectWaterAmy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PanguVpnMng.getInstance().startConnectWaterAmy();
			}
		});
		startConnectWaterAmy.setBounds(801, 644, 251, 57);
		frmvpn.getContentPane().add(startConnectWaterAmy);
		
		JButton startConnect = new JButton("开始连接(通过选择)");
		startConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PanguVpnMng.getInstance().startConnect();
			}
		});
		startConnect.setBounds(510, 716, 542, 57);
		frmvpn.getContentPane().add(startConnect);
		
		JLabel pcNumber = new JLabel("0");
		pcNumber.setForeground(Color.RED);
		pcNumber.setBackground(Color.GREEN);
		pcNumber.setBounds(948, 335, 135, 21);
		frmvpn.getContentPane().add(pcNumber);
		
		JButton btnL2tp = new JButton("L2tp");
		btnL2tp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PanguVpnMng.getInstance().selectL2tp();
			}
		});
		btnL2tp.setBounds(510, 572, 126, 57);
		frmvpn.getContentPane().add(btnL2tp);
		
		JButton btnPptp = new JButton("Pptp");
		btnPptp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PanguVpnMng.getInstance().selectPptp();
			}
		});
		btnPptp.setBounds(670, 572, 126, 57);
		frmvpn.getContentPane().add(btnPptp);
		
		JButton NO1 = new JButton("一台");
		NO1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PanguVpnMng.getInstance().activeMonitorCount(1);
			}
		});
		NO1.setHorizontalAlignment(SwingConstants.LEADING);
		NO1.setBounds(510, 527, 72, 32);
		frmvpn.getContentPane().add(NO1);
		
		JButton NO2 = new JButton("两台");
		NO2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PanguVpnMng.getInstance().activeMonitorCount(2);
			}
		});
		NO2.setHorizontalAlignment(SwingConstants.LEADING);
		NO2.setBounds(594, 529, 72, 32);
		frmvpn.getContentPane().add(NO2);
		
		JButton NO3 = new JButton("三台");
		NO3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PanguVpnMng.getInstance().activeMonitorCount(3);
			}
		});
		NO3.setHorizontalAlignment(SwingConstants.LEADING);
		NO3.setBounds(675, 529, 72, 32);
		frmvpn.getContentPane().add(NO3);
		
		JButton NO4 = new JButton("四台");
		NO4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PanguVpnMng.getInstance().activeMonitorCount(4);
			}
		});
		NO4.setHorizontalAlignment(SwingConstants.LEADING);
		NO4.setBounds(759, 529, 72, 32);
		frmvpn.getContentPane().add(NO4);
		
		
		JLabel label_3 = new JLabel("选择结果:");
		label_3.setBounds(417, 489, 92, 21);
		frmvpn.getContentPane().add(label_3);
		
		JLabel choosedRet = new JLabel("88888");
		choosedRet.setForeground(Color.BLUE);
		choosedRet.setBounds(510, 489, 321, 21);
		frmvpn.getContentPane().add(choosedRet);
		
		PanguVpnMng.getInstance().init(choosedRet,btnL2tp, btnPptp, pcNumber, startConnect, startConnectWaterAmy, increAllocCount, stockAllocCount, finishedOKCount, finishedFailCount,  staticVpnlist, staticIplist, vpnFunction, startConnectMoney, closeConnect, dymanicVpnList, vpnType, vpnName, dymanicIpList, finishedOKCount, noCount, curIP, initIp, increTotalCount, stockTotalCount, scrollConnectState);

	}
}
