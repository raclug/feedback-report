package br.com.feedbacks.services.impl;

import br.com.feedbacks.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${email.from}")
    private String from;

    @Value("${email.to}")
    private String to;

    private final SesClient ses;

    public EmailServiceImpl(SesClient ses) {
        this.ses = ses;
    }

    @Override
    public void sendSimpleEmail(String subject, String body) {
        Destination destination = Destination.builder().toAddresses(to).build();
        Content content = Content.builder().data(body).build();
        Content subjectContent = Content.builder().data(subject).build();
        Body msgBody = Body.builder().text(content).build();
        Message message = Message.builder().subject(subjectContent).body(msgBody).build();

        SendEmailRequest request = SendEmailRequest.builder()
                .destination(destination)
                .message(message)
                .source(from)
                .build();

        ses.sendEmail(request);
    }
}
