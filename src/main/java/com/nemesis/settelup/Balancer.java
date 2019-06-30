package com.nemesis.settelup;

import java.math.BigDecimal;
import java.util.*;

public class Balancer {

    private static Map<String, Amount> status = new HashMap<>();

    static {
        status.put("A", new Amount(BigDecimal.ZERO));
        status.put("B", new Amount(BigDecimal.ZERO));
        status.put("C", new Amount(BigDecimal.ZERO));
        status.put("D", new Amount(BigDecimal.ZERO));
    }

    public void addTransaction(String creditor, String debtor, Integer amount) {
        this.addTransaction(creditor, debtor, BigDecimal.valueOf(amount));
    }

    public void addTransaction(String creditor, String debtor, BigDecimal amount) {
        status.get(creditor).subtract(amount);
        status.get(debtor).add(amount);
    }

    public List<BalancerTransaction> getBalancerTransactions() {

        Map<String, Amount> statusToBalance = new HashMap<>();
        for (Map.Entry<String, Amount> statusEntry : status.entrySet()) {
            statusToBalance.put(statusEntry.getKey(), new Amount(statusEntry.getValue()));
        }

        List<BalancerTransaction> balancerTransactions = new ArrayList<>();

        while (!this.statusIsBalanced(statusToBalance)) { // while status amount is ZERO for all

            Map<String, Amount> creditors = new HashMap<>();
            Map<String, Amount> debtors = new HashMap<>();
            statusToBalance.forEach((key, value) -> {
                if (value.isPositive()) {
                    debtors.put(key, value);
                } else if (value.isNegative()) {
                    creditors.put(key, value);
                }
            });

            BalancerTransaction balancerTransaction = this.getBalancerTransaction(creditors, debtors);
            balancerTransactions.add(balancerTransaction);
            String payer = balancerTransaction.payer.getKey();
            String payee = balancerTransaction.payee.getKey();
            statusToBalance.get(payee).add(balancerTransaction.amount);
            statusToBalance.get(payer).subtract(balancerTransaction.amount);

            if (Amount.isZero(statusToBalance.get(payee))) {
                statusToBalance.remove(payee);
            }

            if (Amount.isZero(statusToBalance.get(payer))) {
                statusToBalance.remove(payer);
            }
        }

        return balancerTransactions;
    }

    private BalancerTransaction getBalancerTransaction(Map<String, Amount> creditors, Map<String, Amount> debtors) {

        BalancerTransaction balancerTransaction = new BalancerTransaction();

        creditors.entrySet().stream().min(Map.Entry.comparingByValue()).ifPresent(entry ->
                balancerTransaction.payee = new AbstractMap.SimpleEntry<>(entry)
        );

        debtors.entrySet().stream().min(Map.Entry.comparingByValue()).ifPresent(entry ->
                balancerTransaction.payer = new AbstractMap.SimpleEntry<>(entry)
        );

        Amount payeeAbsValue = Amount.getAbs(balancerTransaction.payee.getValue());
        Amount payerAbsValue = Amount.getAbs(balancerTransaction.payer.getValue());
        balancerTransaction.amount = Amount.getMin(payeeAbsValue, payerAbsValue);

        return balancerTransaction;
    }

    private boolean statusIsBalanced(Map<String, Amount> statusToBalance) {
        return statusToBalance.isEmpty();
//        return statusToBalance.values().stream().allMatch(Amount::isZero);
    }

    static class BalancerTransaction {
        Map.Entry<String, Amount> payee;
        Map.Entry<String, Amount> payer;
        Amount amount;

        @Override
        public String toString() {
            return String.format("[%s] --(%s)--> [%s]", payer.getKey(), amount, payee.getKey());
        }
    }

}
