package com.nemesis.settelup;


public class Main {

    public static void main(String[] args) {

        Balancer balancer = new Balancer();
        balancer.addTransaction("A", "B", 15);
        balancer.addTransaction("A", "C", 25);
        balancer.addTransaction("C", "B", 11);
        balancer.addTransaction("D", "A", 9);

        for (Balancer.BalancerTransaction balancerTransaction : balancer.getBalancerTransactions()) {
            System.out.println(balancerTransaction);
        }

    }

}

