package com.pao.project.bank.model;

public class CurrencyConverter {

    public static double convert(double amount, Currency from, Currency to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Currency cannot be null.");
        }
        if (from == to) return amount;

        // simple rates
        double rateRONtoEUR = 0.20;
        double rateRONtoUSD = 0.22;
        double rateEURtoRON = 5.00;
        double rateUSDtoRON = 4.50;

        return switch (from) {
            case RON -> switch (to) {
                case EUR -> amount * rateRONtoEUR;
                case USD -> amount * rateRONtoUSD;
                default -> amount;
            };
            case EUR -> switch (to) {
                case RON -> amount * rateEURtoRON;
                case USD -> amount * 1.10;
                default -> amount;
            };
            case USD -> switch (to) {
                case RON -> amount * rateUSDtoRON;
                case EUR -> amount * 0.90;
                default -> amount;
            };
        };
    }
}
