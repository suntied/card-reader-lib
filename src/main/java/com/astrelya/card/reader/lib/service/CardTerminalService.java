package com.astrelya.card.reader.lib.service;


import com.astrelya.card.reader.lib.exception.ReaderException;

import javax.smartcardio.Card;

public interface CardTerminalService {
    void waitForCardPresent() throws ReaderException;
    void waitForCardAbsent() throws ReaderException;
    Card connectCard() throws ReaderException;
    byte[] getCardUID(Card card) throws ReaderException;
}
