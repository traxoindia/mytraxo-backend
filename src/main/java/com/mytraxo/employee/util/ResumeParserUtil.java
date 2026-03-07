package com.mytraxo.employee.util;

import com.mytraxo.employee.dto.ResumeParseResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ResumeParserUtil {

    public ResumeParseResponse parseResume(MultipartFile file) {
        String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "resume.pdf";

        // Placeholder parsing logic
        // Later replace with PDF/DOC parsing
        String sampleText = fileName.replace("_", " ").replace(".pdf", "");

        return ResumeParseResponse.builder()
                .fullName(extractName(sampleText))
                .phoneNumber(extractPhone(sampleText))
                .emailAddress(extractEmail(sampleText))
                .educationQualification("")
                .previousWorkExperience("")
                .resume(fileName)
                .build();
    }

    private String extractEmail(String text) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group() : "";
    }

    private String extractPhone(String text) {
        Pattern pattern = Pattern.compile("\\b\\d{10}\\b");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group() : "";
    }

    private String extractName(String text) {
        return text == null ? "" : text;
    }
}