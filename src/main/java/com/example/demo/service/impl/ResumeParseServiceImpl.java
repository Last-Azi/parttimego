package com.example.demo.service.impl;

import com.example.demo.service.ResumeParseService;
import com.example.demo.service.ai.AiResumeParseClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeParseServiceImpl implements ResumeParseService {

    private static final List<String> SECTION_HEADINGS = List.of(
            "教育背景", "教育经历", "项目经历", "项目经验", "实习经历", "工作经历",
            "实践经历", "社会实践", "专业技能", "技能", "技术栈", "自我评价",
            "个人优势", "求职意向", "校园经历", "获奖经历"
    );

    private final AiResumeParseClient aiResumeParseClient;

    @Override
    public Map<String, String> parseResume(String fileUrl, String fileName) {
        Map<String, String> result = new HashMap<>();
        try {
            String text = extractText(fileUrl, fileName);
            if (!StringUtils.hasText(text)) {
                return result;
            }

            putIfNotBlank(result, "realName", extractName(text));
            putIfNotBlank(result, "phone", extractPhone(text));
            putIfNotBlank(result, "email", extractEmail(text));
            putIfNotBlank(result, "school", extractSchool(text));
            putIfNotBlank(result, "major", extractMajor(text));
            putIfNotBlank(result, "grade", extractGrade(text));
            putIfNotBlank(result, "skills", extractSkills(text));
            putIfNotBlank(result, "gender", extractGender(text));
            putIfNotBlank(result, "experience",
                    extractSection(text, "实践经历", "工作经历", "实习经历", "社会实践"));
            putIfNotBlank(result, "projectExperience",
                    extractSection(text, "项目经历", "项目经验", "项目背景", "项目实践"));
            result.put("parseMode", "rule");

            mergeAiResult(result, text);
        } catch (Exception e) {
            log.error("Resume parse failed: {}", e.getMessage());
        }
        return result;
    }

    private void mergeAiResult(Map<String, String> result, String text) {
        Map<String, String> aiResult = aiResumeParseClient.parse(text);
        if (aiResult.isEmpty()) {
            return;
        }
        aiResult.forEach((key, value) -> putIfNotBlank(result, key, value));
    }

    private void putIfNotBlank(Map<String, String> result, String key, String value) {
        if (StringUtils.hasText(value)) {
            result.put(key, value.trim());
        }
    }

    private String extractText(String fileUrl, String fileName) throws Exception {
        String lowerFileName = fileName.toLowerCase();
        try (InputStream inputStream = new URL(fileUrl).openStream()) {
            if (lowerFileName.endsWith(".pdf")) {
                byte[] bytes = readAllBytes(inputStream);
                try (PDDocument document = Loader.loadPDF(bytes)) {
                    String text = new PDFTextStripper().getText(document);
                    log.info("PDF resume text preview: {}", text.substring(0, Math.min(text.length(), 300)));
                    return text;
                }
            }
            if (lowerFileName.endsWith(".docx")) {
                try (XWPFDocument document = new XWPFDocument(inputStream);
                     XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
                    return extractor.getText();
                }
            }
        }
        return null;
    }

    private byte[] readAllBytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        return outputStream.toByteArray();
    }

    private String extractName(String text) {
        Pattern[] patterns = {
                Pattern.compile("(?:姓名|Name)[:：\\s]*([\\u4e00-\\u9fa5]{2,4})"),
                Pattern.compile("^\\s*([\\u4e00-\\u9fa5]{2,4})\\s*$", Pattern.MULTILINE)
        };
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String name = matcher.group(1).trim();
                if (!SECTION_HEADINGS.contains(name) && !name.contains("简历") && !name.contains("求职")) {
                    return name;
                }
            }
        }
        return null;
    }

    private String extractPhone(String text) {
        Matcher matcher = Pattern.compile("(?<!\\d)(1[3-9]\\d{9})(?!\\d)").matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String extractEmail(String text) {
        Matcher matcher = Pattern.compile("[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,}").matcher(text);
        return matcher.find() ? matcher.group() : null;
    }

    private String extractSchool(String text) {
        Matcher matcher = Pattern.compile("([\\u4e00-\\u9fa5]{2,}(?:大学|学院))").matcher(text);
        while (matcher.find()) {
            String school = matcher.group(1);
            if (!school.contains("课程") && !school.contains("软件工程")) {
                return school;
            }
        }
        return null;
    }

    private String extractMajor(String text) {
        Pattern[] patterns = {
                Pattern.compile("(?:专业|主修)[:：\\s]*([\\u4e00-\\u9fa5A-Za-z\\s]{2,30})"),
                Pattern.compile("([\\u4e00-\\u9fa5]{2,10})\\s*/\\s*(?:本科|硕士|专科)"),
                Pattern.compile("(软件工程|计算机科学与技术|网络工程|信息管理与信息系统|人工智能|数据科学与大数据技术)")
        };
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                String major = matcher.group(1).trim();
                if (!major.contains("大学") && !major.contains("学院") && !major.contains("课程")) {
                    return major.length() > 30 ? major.substring(0, 30) : major;
                }
            }
        }
        return null;
    }

    private String extractGrade(String text) {
        Matcher directMatcher = Pattern.compile("(大[一二三四]|研[一二三])").matcher(text);
        if (directMatcher.find()) {
            return directMatcher.group(1);
        }

        Matcher graduateMatcher = Pattern.compile("(20\\d{2})[.\\-/年]\\d{0,2}\\s*[-至~]\\s*(20\\d{2})").matcher(text);
        if (graduateMatcher.find()) {
            int enrollYear = Integer.parseInt(graduateMatcher.group(1));
            return calculateGrade(enrollYear);
        }

        Matcher yearMatcher = Pattern.compile("(20\\d{2}|\\d{2})级").matcher(text);
        if (yearMatcher.find()) {
            String year = yearMatcher.group(1);
            int enrollYear = year.length() == 2 ? 2000 + Integer.parseInt(year) : Integer.parseInt(year);
            return calculateGrade(enrollYear);
        }
        return null;
    }

    private String calculateGrade(int enrollYear) {
        LocalDate now = LocalDate.now();
        int grade = now.getMonthValue() >= 9
                ? now.getYear() - enrollYear + 1
                : now.getYear() - enrollYear;
        String[] names = {"", "大一", "大二", "大三", "大四"};
        if (grade >= 1 && grade <= 4) {
            return names[grade];
        }
        return grade > 4 ? "已毕业" : null;
    }

    private String extractSkills(String text) {
        String[] skillKeywords = {
                "Java", "Python", "JavaScript", "TypeScript", "Go", "C++", "C",
                "Vue", "React", "Element Plus", "Pinia", "Vite",
                "Spring Boot", "SpringBoot", "Spring MVC", "MyBatis", "MyBatis-Plus",
                "MySQL", "Redis", "MongoDB", "PostgreSQL",
                "Docker", "Kubernetes", "Kafka", "RabbitMQ", "Nginx",
                "Linux", "Git", "Maven", "Gradle", "JWT", "WebSocket", "STOMP",
                "HTML", "CSS"
        };
        StringBuilder skills = new StringBuilder();
        for (String skill : skillKeywords) {
            if (text.toLowerCase().contains(skill.toLowerCase())) {
                if (skills.length() > 0) {
                    skills.append(",");
                }
                skills.append(skill);
            }
        }
        return skills.length() > 0 ? skills.toString() : null;
    }

    private String extractGender(String text) {
        Matcher matcher = Pattern.compile("性别[:：\\s]*([男女])").matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String extractSection(String text, String... sectionNames) {
        for (String sectionName : sectionNames) {
            int start = text.indexOf(sectionName);
            if (start < 0) {
                continue;
            }
            start += sectionName.length();
            int end = findNextHeading(text, start);
            String content = text.substring(start, end < 0 ? text.length() : end)
                    .replaceAll("\\n{3,}", "\n\n")
                    .trim();
            if (StringUtils.hasText(content)) {
                return content.length() > 500 ? content.substring(0, 500) + "..." : content;
            }
        }
        return null;
    }

    private int findNextHeading(String text, int fromIndex) {
        int next = -1;
        for (String heading : SECTION_HEADINGS) {
            int index = text.indexOf(heading, fromIndex);
            if (index >= 0 && (next < 0 || index < next)) {
                next = index;
            }
        }
        return next;
    }
}
