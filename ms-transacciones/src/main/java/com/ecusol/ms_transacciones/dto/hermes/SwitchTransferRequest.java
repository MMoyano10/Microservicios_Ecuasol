package com.ecusol.ms_transacciones.dto.hermes;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class SwitchTransferRequest {

    private Header header;
    private Body body;

    @Override
    public String toString() {
        return "SwitchTransferRequest{header=" + header + ", body=" + body + "}";
    }

    @Getter
    @Setter
    public static class Header {
        private String messageId;
        private String creationDateTime; // ISO 8601
        private String originatingBankId;

        @Override
        public String toString() {
            return "Header{messageId='" + messageId + "', creationDateTime='" + creationDateTime + "', originatingBankId='" + originatingBankId + "'}";
        }
    }

    @Getter
    @Setter
    public static class Body {
        private String instructionId; // UUID V4 (Idempotencia)
        private String endToEndId;
        private Amount amount;
        private Debtor debtor;
        private Creditor creditor;
        private String remittanceInformation;

        @Override
        public String toString() {
            return "Body{instructionId='" + instructionId + "', endToEndId='" + endToEndId + "', amount=" + amount + ", debtor=" + debtor + ", creditor=" + creditor + ", remittanceInformation='" + remittanceInformation + "'}";
        }
    }

    @Getter
    @Setter
    public static class Amount {
        private String currency;
        private BigDecimal value;

        @Override
        public String toString() {
            return "Amount{currency='" + currency + "', value=" + value + "}";
        }
    }

    @Getter
    @Setter
    public static class Debtor {
        private String name;
        private String accountId;
        private String accountType;

        @Override
        public String toString() {
            return "Debtor{name='" + name + "', accountId='" + accountId + "', accountType='" + accountType + "'}";
        }
    }

    @Getter
    @Setter
    public static class Creditor {
        private String name;
        private String targetBankId; // BIC del destino
        private String accountId;
        private String accountType;

        @Override
        public String toString() {
            return "Creditor{name='" + name + "', targetBankId='" + targetBankId + "', accountId='" + accountId + "', accountType='" + accountType + "'}";
        }
    }
}