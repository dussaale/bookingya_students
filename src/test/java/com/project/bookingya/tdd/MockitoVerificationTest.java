package com.project.bookingya.tdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MockitoVerificationTest {

    @Mock
    private List<String> listaMockeada;

    @Test
    public void testMockitoFunciona() {
        when(listaMockeada.size()).thenReturn(100);
        assertEquals(100, listaMockeada.size());
        System.out.println("🎉 MOCKITO ESTÁ FUNCIONANDO CORRECTAMENTE");
    }
}