package com.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

public class MenuFrame extends JFrame {

    private JPanel contentPane;
    private JList<String> menuList;

    public MenuFrame() {
        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 380, 250);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        DefaultListModel<String> model = new DefaultListModel<String>();
        model.addElement("MainFrame (CRUD画面)");
        model.addElement("FinanceSimulationInputFrame (ローンシミュレーター)");

        menuList = new JList<String>(model);
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openSelectedMenu();
                }
            }
        });

        contentPane.add(new JScrollPane(menuList), BorderLayout.CENTER);

        JButton btnOpen = new JButton("Open");
        btnOpen.addActionListener(e -> openSelectedMenu());
        contentPane.add(btnOpen, BorderLayout.SOUTH);
    }

    private void openSelectedMenu() {
        int selectedIndex = menuList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "メニューを選択してください。");
            return;
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    if (selectedIndex == 0) {
                        MainFrame frame = new MainFrame();
                        frame.setTitle("Main Frame");
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    } else if (selectedIndex == 1) {
                        FinanceSimulationInputFrame frame = new FinanceSimulationInputFrame();
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MenuFrame.this, "画面起動中にエラーが発生しました: " + ex.getMessage());
                }
            }
        });
    }
}
