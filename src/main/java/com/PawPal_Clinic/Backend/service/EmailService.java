package com.PawPal_Clinic.Backend.service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.hibernate.sql.results.graph.collection.internal.MapInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.PawPal_Clinic.Backend.dto.RendezVousDto;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Method;
import biweekly.util.Duration;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("PawPal@gmail.com"); // Set from address
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

public void sendRendezVousInvite(RendezVousDto rendezVousDto,String  email) throws IOException,MessagingException{
    MimeMessage mimeMessage=emailSender.createMimeMessage();
    mimeMessage.setRecipients(Message.RecipientType.TO, email);
    mimeMessage.setSubject(rendezVousDto.getMotif());
    MimeMultipart mimeMultipart=new MimeMultipart("mixed");
    
    mimeMultipart.addBodyPart(createCalenderMimeBody(rendezVousDto,email));
    mimeMessage.setContent(mimeMultipart);
    emailSender.send(mimeMessage);

}

private BodyPart createCalenderMimeBody(RendezVousDto rendezVousDto,String email)throws IOException,MessagingException
{
MimeBodyPart calenderBody=new MimeBodyPart();
final DataSource source =new ByteArrayDataSource(createCal(rendezVousDto,email), "text/calender;charset=UTF-8");
calenderBody.setDataHandler(new DataHandler(source));
calenderBody.setHeader("Content-Type", "text/calendar;charset=UTF-8;method=REQUEST");
return calenderBody;

}


private String createCal(RendezVousDto rendezVousDto,String email){
    ICalendar ical=new ICalendar();
    ical.addProperty(new Method(Method.REQUEST));
    VEvent event=new VEvent();
    event.setSummary(rendezVousDto.getMotif());
    event.setDescription(rendezVousDto.getStatut());
    event.setDateStart(getStartDate(rendezVousDto.getCreeLe()));
    event.setDuration(new Duration.Builder()
    .hours(1)
    .build());
    event.setOrganizer("PawPal@gmail.com");
    event.addAttendee(email);
    ical.addEvent(event);
    return Biweekly.write(ical).go();

}

private Date getStartDate(Instant eventDateTime){
Instant instant=eventDateTime.atZone(ZoneId.systemDefault()).toInstant();
return Date.from(instant);
}
}
