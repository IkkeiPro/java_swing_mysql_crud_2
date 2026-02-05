package com.main;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class FinanceSimulationChartFrame extends JFrame {

    public FinanceSimulationChartFrame(List<FinanceSimulator.SimulationPoint> points) {
        setTitle("Finance Simulation Chart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(120, 120, 900, 620);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        contentPane.add(new LineChartPanel(points), BorderLayout.CENTER);
    }

    private static class LineChartPanel extends JPanel {
        private final List<FinanceSimulator.SimulationPoint> points;
        private final DecimalFormat decimalFormat = new DecimalFormat("#,##0");

        public LineChartPanel(List<FinanceSimulator.SimulationPoint> points) {
            this.points = points;
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(860, 560));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (points == null || points.isEmpty()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int left = 70;
            int right = 30;
            int top = 30;
            int bottom = 50;

            double minValue = Double.MAX_VALUE;
            double maxValue = -Double.MAX_VALUE;

            for (FinanceSimulator.SimulationPoint point : points) {
                minValue = Math.min(minValue, point.getCash());
                minValue = Math.min(minValue, point.getLoan());
                minValue = Math.min(minValue, point.getInvestment());
                minValue = Math.min(minValue, point.getNetWorth());

                maxValue = Math.max(maxValue, point.getCash());
                maxValue = Math.max(maxValue, point.getLoan());
                maxValue = Math.max(maxValue, point.getInvestment());
                maxValue = Math.max(maxValue, point.getNetWorth());
            }

            if (Math.abs(maxValue - minValue) < 1e-9) {
                maxValue += 1;
                minValue -= 1;
            }

            g2.setColor(Color.DARK_GRAY);
            g2.drawLine(left, height - bottom, width - right, height - bottom);
            g2.drawLine(left, top, left, height - bottom);

            drawSeries(g2, left, right, top, bottom, width, height, minValue, maxValue, Color.BLUE, "cash", 0);
            drawSeries(g2, left, right, top, bottom, width, height, minValue, maxValue, Color.RED, "loan", 1);
            drawSeries(g2, left, right, top, bottom, width, height, minValue, maxValue, new Color(0, 140, 0), "investment", 2);
            drawSeries(g2, left, right, top, bottom, width, height, minValue, maxValue, new Color(140, 0, 140), "cash+investment-loan", 3);

            int tickCount = Math.min(6, points.size());
            g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
            g2.setColor(Color.GRAY);
            for (int i = 0; i < tickCount; i++) {
                int index = (int) Math.round((points.size() - 1) * (i / (double) (tickCount - 1)));
                FinanceSimulator.SimulationPoint point = points.get(index);
                int x = toX(index, left, width - right, points.size());
                g2.drawLine(x, height - bottom, x, height - bottom + 4);
                g2.drawString("Y" + point.getYear(), x - 12, height - bottom + 18);
            }

            for (int i = 0; i <= 5; i++) {
                double value = minValue + (maxValue - minValue) * i / 5.0;
                int y = toY(value, top, height - bottom, minValue, maxValue);
                g2.setColor(new Color(230, 230, 230));
                g2.drawLine(left, y, width - right, y);
                g2.setColor(Color.GRAY);
                g2.drawString(decimalFormat.format(value), 6, y + 4);
            }

            g2.dispose();
        }

        private void drawSeries(Graphics2D g2, int left, int right, int top, int bottom, int width, int height,
                                double minValue, double maxValue, Color color, String label, int seriesType) {
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2f));

            for (int i = 0; i < points.size() - 1; i++) {
                double v1 = valueByType(points.get(i), seriesType);
                double v2 = valueByType(points.get(i + 1), seriesType);
                int x1 = toX(i, left, width - right, points.size());
                int y1 = toY(v1, top, height - bottom, minValue, maxValue);
                int x2 = toX(i + 1, left, width - right, points.size());
                int y2 = toY(v2, top, height - bottom, minValue, maxValue);
                g2.drawLine(x1, y1, x2, y2);
            }

            int legendX = left + 15;
            int legendY = top + 15 + 20 * seriesType;
            g2.drawLine(legendX, legendY, legendX + 26, legendY);
            g2.drawString(label, legendX + 30, legendY + 5);
        }

        private double valueByType(FinanceSimulator.SimulationPoint point, int type) {
            if (type == 0) {
                return point.getCash();
            }
            if (type == 1) {
                return point.getLoan();
            }
            if (type == 2) {
                return point.getInvestment();
            }
            return point.getNetWorth();
        }

        private int toX(int index, int left, int chartRight, int size) {
            if (size <= 1) {
                return left;
            }
            double ratio = index / (double) (size - 1);
            return (int) Math.round(left + (chartRight - left) * ratio);
        }

        private int toY(double value, int top, int chartBottom, double minValue, double maxValue) {
            double ratio = (value - minValue) / (maxValue - minValue);
            return (int) Math.round(chartBottom - (chartBottom - top) * ratio);
        }
    }
}
