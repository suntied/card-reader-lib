package com.astrelya.card.reader.lib;


import com.astrelya.card.reader.lib.data.BadgeData;
import com.astrelya.card.reader.lib.exception.ReaderException;
import com.astrelya.card.reader.lib.port.NFCReader;
import com.astrelya.card.reader.lib.service.CardTerminalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.smartcardio.Card;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NFCReaderTest {

    private CardTerminalService terminalServiceMock;
    private NFCReader nfcReader;
    private Card cardMock;

    @BeforeEach
    public void setUp() {
        terminalServiceMock = mock(CardTerminalService.class);
        nfcReader = new NFCReader(terminalServiceMock);
        cardMock = mock(Card.class);
    }

    @Test
    void testReadBadgeSuccessfully() throws Exception {
        byte[] fakeUID = new byte[]{(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78};

        doNothing().when(terminalServiceMock).waitForCardPresent();
        when(terminalServiceMock.connectCard()).thenReturn(cardMock);
        when(terminalServiceMock.getCardUID(cardMock)).thenReturn(fakeUID);

        BadgeData badgeData = nfcReader.readBadge();

        verify(terminalServiceMock).waitForCardPresent();
        verify(terminalServiceMock).connectCard();
        verify(terminalServiceMock).getCardUID(cardMock);

        assertEquals("12345678", badgeData.id());
        assertNotNull(badgeData.timestamp());
    }

    @Test
    void testReadBadgeThrowsException() throws Exception {
        doThrow(new ReaderException("Error waiting", new Exception()))
                .when(terminalServiceMock).waitForCardPresent();

        assertThrows(ReaderException.class, () -> nfcReader.readBadge());

        verify(terminalServiceMock, never()).connectCard();
        verify(terminalServiceMock, never()).getCardUID(any(Card.class));
    }
}
