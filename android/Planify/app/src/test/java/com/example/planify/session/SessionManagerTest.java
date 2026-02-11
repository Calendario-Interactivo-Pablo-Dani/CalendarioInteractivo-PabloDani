package com.example.planify.session;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.planify.data.session.SessionManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SessionManagerTest {

    @Mock
    Context mockContext;

    @Mock
    SharedPreferences mockPrefs;

    @Mock
    SharedPreferences.Editor mockEditor;

    private SessionManager sessionManager;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockContext.getSharedPreferences(anyString(), eq(Context.MODE_PRIVATE)))
                .thenReturn(mockPrefs);

        when(mockPrefs.edit()).thenReturn(mockEditor);

        sessionManager = new SessionManager(mockContext);
    }

    @Test
    public void saveSession_shouldStoreAllValues() {

        sessionManager.saveSession(
                1,
                "Daniel",
                "daniel123",
                "daniel@gmail.com",
                "600000000"
        );

        verify(mockEditor).putBoolean("is_logged", true);
        verify(mockEditor).putInt("id_user", 1);
        verify(mockEditor).putString("username", "daniel123");
        verify(mockEditor).putString("name", "Daniel");
        verify(mockEditor).putString("email", "daniel@gmail.com");
        verify(mockEditor).putString("telefono", "600000000");
        verify(mockEditor).apply();
    }

    @Test
    public void isLogged_shouldReturnTrue_whenPreferenceIsTrue() {
        when(mockPrefs.getBoolean("is_logged", false)).thenReturn(true);

        boolean result = sessionManager.isLogged();

        assertTrue(result);
    }

    @Test
    public void getIdUser_shouldReturnStoredId() {
        when(mockPrefs.getInt("id_user", -1)).thenReturn(5);

        int result = sessionManager.getIdUser();

        assertEquals(5, result);
    }

    @Test
    public void logout_shouldClearPreferences() {
        sessionManager.logout();

        verify(mockEditor).clear();
        verify(mockEditor).apply();
    }
}