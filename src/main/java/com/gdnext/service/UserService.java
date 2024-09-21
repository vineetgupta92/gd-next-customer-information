package com.gdnext.service;

import com.gdnext.entity.User;
import com.gdnext.enums.RequiredFieldsEnum;
import com.gdnext.feign.ExternalService;
import com.gdnext.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class UserService {

    public static final String BIRTHDATE = "birthdate";

    public static final String BIRTHPLACE = "birthplace";

    public static final String SEX = "sex";

    public static final String ADDRESS = "address";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExternalService externalService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Map<String, Boolean> getMissingFields(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        var missingFields = new HashMap<String, Boolean>();

        addMissingFieldsOnUser(missingFields, user);

        return missingFields;
    }

    private static void addMissingFieldsOnUser(Map<String, Boolean> missingFields, User user) {
        for (RequiredFieldsEnum requiredField : RequiredFieldsEnum.values()) {
            switch (requiredField.getFieldName()) {
                case BIRTHDATE:
                    missingFields.put(BIRTHDATE, user.getBirthdate() == null);
                    break;
                case BIRTHPLACE:
                    missingFields.put(BIRTHPLACE, user.getBirthplace() == null);
                    break;
                case SEX:
                    missingFields.put(SEX, user.getSex() == null);
                    break;
                case ADDRESS:
                    missingFields.put(ADDRESS, user.getAddress() == null);
                    break;
            }
        }
    }

    @Transactional
    public String submitForm(Long userId, Map<String, String> formData) {
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        var missingFields = new HashMap<String, Boolean>();
        addMissingFieldsOnUser(missingFields, user);

        for (Map.Entry<String, Boolean> entry : missingFields.entrySet()) {
            String field = entry.getKey();
            Boolean isMissing = entry.getValue();

            if (isMissing && (!formData.containsKey(field) || formData.get(field) == null || formData.get(field).isBlank())) {
                throw new RuntimeException("Missing required field in form data: " + field);
            }
        }

        Map<String, Consumer<String>> fieldUpdaters = new HashMap<>();
        fieldUpdaters.put(BIRTHDATE, value -> user.setBirthdate(LocalDate.parse(value, formatter)));
        fieldUpdaters.put(BIRTHPLACE, user::setBirthplace);
        fieldUpdaters.put(SEX, user::setSex);
        fieldUpdaters.put(ADDRESS, user::setAddress);

        formData.forEach((key, value) -> {
            if (fieldUpdaters.containsKey(key)) {
                fieldUpdaters.get(key).accept(value);
            }
        });

        userRepository.save(user);
        return externalService.callExternalService(user);
    }


    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

}
