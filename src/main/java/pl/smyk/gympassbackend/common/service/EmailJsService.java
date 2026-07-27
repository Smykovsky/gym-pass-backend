package pl.smyk.gympassbackend.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EmailJsService {

    @Value("${emailjs.service-id:contact_gympass}")
    private String serviceId;

    @Value("${emailjs.user-id:user_gympass_public_key}")
    private String userId;

    @Value("${emailjs.private-key:}")
    private String privateKey;

    @Value("${emailjs.template-id:contact-template}")
    private String defaultTemplateId;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String EMAILJS_API_URL = "https://api.emailjs.com/api/v1.0/email/send";

    @Async
    public void sendEmail(String templateId, Map<String, Object> templateParams) {
        if (userId == null || userId.isEmpty() || userId.contains("YOUR_EMAILJS") || userId.contains("user_gympass")) {
            log.warn("EmailJS: Pominięcie wysyłki e-mail – brak ustawionego Public Key (user_id). Podaj swój Public Key z https://dashboard.emailjs.com/admin/account w pliku application.yml (sekcja emailjs.user-id).");
            return;
        }

        try {
            sendHttpRequest(templateId, templateParams, true);
        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            log.warn("EmailJS API zgłosił błąd (Status: {}). Odpowiedź serwera: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            if (ex.getStatusCode().value() == 403 && privateKey != null && !privateKey.isEmpty()) {
                log.info("Ponawianie próby wysyłki EmailJS bez parametru accessToken...");
                try {
                    sendHttpRequest(templateId, templateParams, false);
                } catch (Exception retryEx) {
                    log.error("Ponowna próba bez accessToken nie powiodła się: {}", retryEx.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Błąd podczas wysyłania powiadomienia e-mail przez EmailJS: {}", e.getMessage());
        }
    }

    private void sendHttpRequest(String templateId, Map<String, Object> templateParams, boolean includeAccessToken) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("service_id", serviceId);
        requestBody.put("template_id", templateId != null ? templateId : defaultTemplateId);
        requestBody.put("user_id", userId);
        if (includeAccessToken && privateKey != null && !privateKey.isEmpty() && !privateKey.equals(userId)) {
            requestBody.put("accessToken", privateKey);
        }
        requestBody.put("template_params", templateParams);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        log.info("Wysyłanie wiadomości e-mail via EmailJS (service_id: {}, user_id: {}) do: {}", serviceId, userId, templateParams.get("to_email"));
        String response = restTemplate.postForObject(EMAILJS_API_URL, entity, String.class);
        log.info("Odpowiedź z EmailJS API: {}", response);
    }

    private Map<String, Object> createBaseParams(String toEmail, String userName) {
        Map<String, Object> params = new HashMap<>();
        params.put("to_email", toEmail);
        params.put("email", toEmail);
        params.put("user_email", toEmail);
        params.put("recipient", toEmail);
        if (userName != null) {
            params.put("to_name", userName);
            params.put("name", userName);
            params.put("user_name", userName);
        }
        return params;
    }

    public void sendOrderPaidEmail(String toEmail, String userName, String trainerName, String orderId, String goal) {
        Map<String, Object> params = createBaseParams(toEmail, userName);
        params.put("trainer_name", trainerName);
        params.put("order_id", orderId);
        params.put("goal", goal);
        params.put("subject", "GymPass: Potwierdzenie opłacenia zlecenia planu treningowego");
        params.put("message", "Twoja płatność BLIK została zaksięgowana. Trener " + trainerName + " rozpoczął przygotowywanie Twojego planu treningowego!");

        sendEmail(defaultTemplateId, params);
    }

    public void sendPlanFulfilledEmail(String toEmail, String userName, String planTitle) {
        Map<String, Object> params = createBaseParams(toEmail, userName);
        params.put("plan_title", planTitle);
        params.put("subject", "GymPass: Twój spersonalizowany plan treningowy jest gotowy!");
        params.put("message", "Trener ułożył dla Ciebie plan: '" + planTitle + "'. Jest on gotowy do użycia w Twojej zakładce 'Workout Plan'!");

        sendEmail(defaultTemplateId, params);
    }

    public void sendMembershipPurchasedEmail(String toEmail, String membershipType) {
        Map<String, Object> params = createBaseParams(toEmail, null);
        params.put("subject", "GymPass: Potwierdzenie zakupu karnetu");
        params.put("message", "Twój karnet " + membershipType + " został pomyślnie aktywowany. Życzymy udanych treningów!");

        sendEmail(defaultTemplateId, params);
    }

    public void sendWelcomeAccountCreatedEmail(String toEmail, String userName) {
        Map<String, Object> params = createBaseParams(toEmail, userName != null ? userName : "Klubowiczu");
        params.put("subject", "GymPass: Witamy na pokładzie! Twoje konto zostało pomyślnie utworzone");
        params.put("message", "Witaj " + (userName != null ? userName : "") + "! Twoje konto w serwisie GymPass zostało pomyślnie utworzone. Zaloguj się, wybierz karnet lub zamów spersonalizowany plan treningowy!");

        sendEmail(defaultTemplateId, params);
    }

    public void sendContestWonEmail(String toEmail, String userName, String contestTitle, int months) {
        Map<String, Object> params = createBaseParams(toEmail, userName != null ? userName : "Klubowiczu");
        params.put("subject", "GymPass: GRATULACJE! Wygrałeś w konkursie GymPass!");
        params.put("message", "Gratulacje " + (userName != null ? userName : "") + "! Zostałeś wylosowany jako zwycięzca w konkursie '" + contestTitle + "'. Twój karnet został automatycznie doładowany o " + months + " msc!");

        sendEmail(defaultTemplateId, params);
    }
}
