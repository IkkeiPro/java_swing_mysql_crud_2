package com.main;

import java.util.ArrayList;
import java.util.List;

public class FinanceSimulator {

    public static class SimulationPoint {
        private final int year;
        private final double cash;
        private final double loan;
        private final double investment;
        private final double netWorth;

        public SimulationPoint(int year, double cash, double loan, double investment) {
            this.year = year;
            this.cash = cash;
            this.loan = loan;
            this.investment = investment;
            this.netWorth = cash + investment - loan;
        }

        public int getYear() {
            return year;
        }

        public double getCash() {
            return cash;
        }

        public double getLoan() {
            return loan;
        }

        public double getInvestment() {
            return investment;
        }

        public double getNetWorth() {
            return netWorth;
        }
    }

    public static List<SimulationPoint> simulate(
            double initialCash,
            double initialLoan,
            double annualIncomeIncrease,
            double loanInterestRate,
            double investmentYieldAnnual,
            int years,
            int loanTermMonths,
            double payAtFirstMonth,
            double monthlyInvestmentAmount
    ) {
        final double cashFloor = 1_000_000;

        double cash = initialCash;
        double loan = initialLoan;
        double investment = 0.0;

        double monthlyIncome = annualIncomeIncrease / 12.0;
        double loanInterestMonthly = loanInterestRate / 12.0;
        double investmentYieldMonthly = investmentYieldAnnual / 12.0;

        int totalMonths = years * 12;

        int normalizedLoanTermMonths = Math.max(1, loanTermMonths);
        double principalPayment = Math.ceil(initialLoan / normalizedLoanTermMonths);

        List<SimulationPoint> points = new ArrayList<SimulationPoint>();

        for (int month = 1; month <= totalMonths; month++) {
            if (month == 1) {
                double payAmount = Math.min(Math.max(payAtFirstMonth, 0.0), loan);
                cash -= payAmount;
                loan -= payAmount;
            }

            cash += monthlyIncome;

            double interest = 0;
            if (loan > 0) {
                interest = loan * loanInterestMonthly;
            }

            if (loan > 0) {
                double principal = Math.min(principalPayment, loan);
                double totalPayment = principal + interest;

                cash -= totalPayment;
                loan -= principal;
            }

            investment *= (1 + investmentYieldMonthly);

            if (monthlyInvestmentAmount > 0) {
                if (cash - monthlyInvestmentAmount >= cashFloor) {
                    cash -= monthlyInvestmentAmount;
                    investment += monthlyInvestmentAmount;
                }
            }

            if (month % 12 == 0) {
                points.add(new SimulationPoint(month / 12, cash, loan, investment));
            }
        }

        return points;
    }
}
