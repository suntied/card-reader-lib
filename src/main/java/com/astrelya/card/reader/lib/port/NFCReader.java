package com.astrelya.card.reader.lib.port;

import com.astrelya.card.reader.lib.data.BadgeData;
import com.astrelya.card.reader.lib.exception.ReaderException;
import com.astrelya.card.reader.lib.service.CardTerminalService;

import javax.smartcardio.Card;
import java.util.Date;

public class NFCReader {
    private final CardTerminalService terminalService;

    public NFCReader(CardTerminalService terminalService) {
        this.terminalService = terminalService;
    }

    public BadgeData readBadge() throws ReaderException {
        terminalService.waitForCardPresent();
        Card card = terminalService.connectCard();
        byte[] uid = terminalService.getCardUID(card);
        Date badgeTime = new Date();
        return new BadgeData(bytesToHex(uid), badgeTime);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
