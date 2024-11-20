package com.astrelya.card.reader.lib.impl;

import com.astrelya.card.reader.lib.exception.ReaderException;
import com.astrelya.card.reader.lib.service.CardTerminalService;

import javax.smartcardio.*;

public class CardTerminalServiceImpl implements CardTerminalService {

    @Override
    public void waitForCardPresent() throws ReaderException {
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            CardTerminals terminals = factory.terminals();
            CardTerminal terminal = terminals.list().get(0);
            terminal.waitForCardPresent(0);
        } catch (Exception e) {
            throw new ReaderException("Error waiting card", e);
        }
    }

    @Override
    public void waitForCardAbsent() throws ReaderException {
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            CardTerminals terminals = factory.terminals();
            CardTerminal terminal = terminals.list().get(0);
            while (terminal.isCardPresent()) {
                Thread.sleep(100);
            }
        } catch (Exception e) {
            throw new ReaderException("Error while waiting for card removal", e);
        }
    }

    @Override
    public Card connectCard() throws ReaderException {
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            CardTerminals terminals = factory.terminals();
            CardTerminal terminal = terminals.list().get(0);
            return terminal.connect("*");
        } catch (Exception e) {
            throw new ReaderException("Error while reading the card", e);
        }
    }

    @Override
    public byte[] getCardUID(Card card) throws ReaderException {
        try {
            CommandAPDU command = new CommandAPDU(new byte[]{(byte) 0xFF, (byte) 0xCA, 0x00, 0x00, 0x00});
            ResponseAPDU response = card.getBasicChannel().transmit(command);
            return response.getData();
        } catch (CardException e) {
            throw new ReaderException("Error while reading id", e);
        }
    }
}
