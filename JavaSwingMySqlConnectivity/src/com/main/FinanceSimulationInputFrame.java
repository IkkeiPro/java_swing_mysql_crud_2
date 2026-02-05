package com.main;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class FinanceSimulationInputFrame extends JFrame {

    private JPanel contentPane;
    private JTextField txtInitialCash;
    private JTextField txtInitialLoan;
    private JTextField txtAnnualIncomeIncrease;
    private JTextField txtLoanInterestRate;
    private JTextField txtInvestmentYieldAnnual;
    private JTextField txtMonthlyInvestmentAmount;
    private JTextField txtYears;
    private JTextField txtPayAtFirstMonth;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FinanceSimulationInputFrame frame = new FinanceSimulationInputFrame();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public FinanceSimulationInputFrame() {
        setTitle("Finance Simulation Input");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 520, 360);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(9, 2, 8, 8));

        txtInitialCash = createInputRow("initialCash", "1000000");
        txtInitialLoan = createInputRow("initialLoan", "1000000");
        txtAnnualIncomeIncrease = createInputRow("annualIncomeIncrease", "500000");
        txtLoanInterestRate = createInputRow("loanInterestRate", "0.004");
        txtInvestmentYieldAnnual = createInputRow("investmentYieldAnnual", "0.04");
        txtMonthlyInvestmentAmount = createInputRow("monthlyLoanPayment", "100000");
        txtYears = createInputRow("years", "10");
        txtPayAtFirstMonth = createInputRow("payAtFirstMonth", "0");

        contentPane.add(new JLabel(""));
        JButton btnRun = new JButton("Run Simulation");
        btnRun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runSimulation();
            }
        });
        contentPane.add(btnRun);
    }

    private JTextField createInputRow(String label, String defaultValue) {
        JLabel jLabel = new JLabel(label);
        JTextField field = new JTextField(defaultValue);
        contentPane.add(jLabel);
        contentPane.add(field);
        return field;
    }

    private void runSimulation() {
        try {
            double initialCash = Double.parseDouble(txtInitialCash.getText().trim());
            double initialLoan = Double.parseDouble(txtInitialLoan.getText().trim());
            double annualIncomeIncrease = Double.parseDouble(txtAnnualIncomeIncrease.getText().trim());
            double loanInterestRate = Double.parseDouble(txtLoanInterestRate.getText().trim());
            double investmentYieldAnnual = Double.parseDouble(txtInvestmentYieldAnnual.getText().trim());
            double monthlyInvestmentAmount = Double.parseDouble(txtMonthlyInvestmentAmount.getText().trim());
            int years = Integer.parseInt(txtYears.getText().trim());
            double payAtFirstMonth = Double.parseDouble(txtPayAtFirstMonth.getText().trim());

            List<FinanceSimulator.SimulationPoint> points = FinanceSimulator.simulate(
                    initialCash,
                    initialLoan,
                    annualIncomeIncrease,
                    loanInterestRate,
                    investmentYieldAnnual,
                    years,
                    payAtFirstMonth,
                    monthlyInvestmentAmount
            );

            FinanceSimulationChartFrame chartFrame = new FinanceSimulationChartFrame(points);
            chartFrame.setLocationRelativeTo(null);
            chartFrame.setVisible(true);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "入力値は数値で指定してください。", "入力エラー", JOptionPane.ERROR_MESSAGE);
        }
    }
}
