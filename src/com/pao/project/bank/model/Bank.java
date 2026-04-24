package com.pao.project.bank.model;

public class Bank {

    private final String name;
    private final String swiftCode;

    private Bank() {
        this.name = "THE BANK";
        this.swiftCode = "VIOLETA-RO-BANK";
    }

    private static class Holder {
        private static final Bank INSTANCE = new Bank();
    }

    public static Bank getInstance() {
        return Holder.INSTANCE;
    }

    public String getName() {
        return name;
    }

    public String getSwiftCode(){
        return swiftCode;
    }
}
