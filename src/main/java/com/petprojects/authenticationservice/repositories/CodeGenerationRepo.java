package com.petprojects.authenticationservice.repositories;

import com.petprojects.authenticationservice.services.CodeGenerationService;
import org.springframework.stereotype.Repository;

@Repository
public class CodeGenerationRepo implements CodeGenerationService {
    @Override
    public String codeGeneration() {
        StringBuilder codeConfirmationStringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            double doubleNumber = Math.random() * 9;
            int intNumber = (int)doubleNumber;
            codeConfirmationStringBuilder.append(intNumber);
        }
        return String.valueOf(codeConfirmationStringBuilder);

    }
}
