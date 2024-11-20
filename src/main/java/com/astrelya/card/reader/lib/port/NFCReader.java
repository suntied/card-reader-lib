package com.astrelya.card.reader.lib.port;

import com.astrelya.card.reader.lib.data.BadgeData;
import com.astrelya.card.reader.lib.exception.ReaderException;
import com.astrelya.card.reader.lib.service.CardTerminalService;
import java.util.Date;
import javax.smartcardio.Card;

public class NFCReader {
    private final CardTerminalService terminalService;

    public NFCReader(CardTerminalService terminalService) {
        this.terminalService = terminalService;
    }

    public BadgeData readBadge() throws ReaderException {
        try {
            this.terminalService.waitForCardPresent();
            Card card = this.terminalService.connectCard();
            byte[] uid = this.terminalService.getCardUID(card);
            if (uid == null || uid.length == 0) {
                return null;
            }
            Date badgeTime = new Date();
            this.terminalService.waitForCardAbsent();
            return new BadgeData(this.bytesToHex(uid), badgeTime);
        } catch (ReaderException e) {
            throw e;
        } catch (Exception e) {
            throw new ReaderException("Unexpected error during badge reading", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
