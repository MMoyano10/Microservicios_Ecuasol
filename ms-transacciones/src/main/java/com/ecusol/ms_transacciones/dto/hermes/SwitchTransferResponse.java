package com.ecusol.ms_transacciones.dto.hermes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SwitchTransferResponse {

    private Header header;
    private Body body;

    @Override
    public String toString() {
        return "SwitchTransferResponse{header=" + header + ", body=" + body + "}";
    }

    @Getter
    @Setter
    public static class Header {
        private String messageId;
        private String relatedMessageId;
        private String timestamp;

        @Override
        public String toString() {
            return "Header{messageId='" + messageId + "', relatedMessageId='" + relatedMessageId + "', timestamp='" + timestamp + "'}";
        }
    }

    @Getter
    @Setter
    public static class Body {
        private String instructionId;
        private String status; // COMPLETED, FAILED, PENDING
        private String networkReference;
        private String settlementDate;

        @Override
        public String toString() {
            return "Body{instructionId='" + instructionId + "', status='" + status + "', networkReference='" + networkReference + "', settlementDate='" + settlementDate + "'}";
        }
    }
}