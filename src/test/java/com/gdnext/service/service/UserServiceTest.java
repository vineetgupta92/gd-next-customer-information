package com.gdnext.service.service;

import com.gdnext.entity.User;
import com.gdnext.feign.ExternalService;
import com.gdnext.repository.UserRepository;
import com.gdnext.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExternalService externalService;

    @InjectMocks
    private UserService userService;

    private User user;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthdate(null);
        user.setBirthplace("New York");
        user.setSex("Male");
        user.setAddress(null);
    }

    @Test
    void testGetMissingFields() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Map<String, Boolean> missingFields = userService.getMissingFields(1L);

        assertNotNull(missingFields);
        assertEquals(4, missingFields.size());
        assertTrue(missingFields.get(UserService.BIRTHDATE));
        assertFalse(missingFields.get(UserService.BIRTHPLACE));
        assertFalse(missingFields.get(UserService.SEX));
        assertTrue(missingFields.get(UserService.ADDRESS));

        verify(userRepository).findById(1L);
    }

    @Test
    void submitFormWithMissingRequiredFieldAndShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Map<String, String> formData = new HashMap<>();

        assertThatThrownBy(() -> userService.submitForm(1L, formData))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Missing required field in form data");

        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository, externalService);
    }

    @Test
    void submitFormWithValidFormDataAndShouldUpdateAndSaveUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Map<String, String> formData = new HashMap<>();
        formData.put(UserService.BIRTHDATE, "1990-01-01");
        formData.put(UserService.BIRTHPLACE, "New York");
        formData.put(UserService.SEX, "Male");
        formData.put(UserService.ADDRESS, "123 Main St");

        when(externalService.callExternalService(user)).thenReturn("Success");

        String result = userService.submitForm(1L, formData);

        assertEquals("Success", result);
        assertEquals(LocalDate.parse("1990-01-01", formatter), user.getBirthdate());
        assertEquals("New York", user.getBirthplace());
        assertEquals("Male", user.getSex());
        assertEquals("123 Main St", user.getAddress());

        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
        verify(externalService).callExternalService(user);
    }

    @Test
    void submitFormWithPartialFormDataAndShouldThrowException() {
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Map<String, String> formData = new HashMap<>();
        formData.put(UserService.BIRTHDATE, "1990-01-01"); // Missing birthplace

        assertThatThrownBy(() -> userService.submitForm(1L, formData))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Missing required field in form data");

        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository, externalService);
    }

    @Test
    void testGetMissingFieldsUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getMissingFields(1L));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testSubmitFormUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Map<String, String> formData = new HashMap<>();
        formData.put(UserService.BIRTHDATE, "1990-01-01");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.submitForm(1L, formData));
        assertEquals("User not found", exception.getMessage());
    }
}
