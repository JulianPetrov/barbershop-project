package com.example.barbershopproject.service.email;

import com.example.barbershopproject.model.Appointment;
import com.example.barbershopproject.model.Employee;
import com.example.barbershopproject.model.SalonServiceEntity;
import com.example.barbershopproject.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
public class EmailService {

  private final JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String sender;

  public void sendSimpleMail(EmailDetails details) {
    try {
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      mimeMessage.setFrom(sender);
      mimeMessage.setRecipient(
          Message.RecipientType.TO, new InternetAddress(details.getRecipient()));
      mimeMessage.setSubject(details.getSubject());
      mimeMessage.setContent(details.getMsgBody(), "text/html; charset=utf-8");
      javaMailSender.send(mimeMessage);
    } catch (MessagingException e) {
      throw new IllegalStateException("failed to send email");
    }
  }

  public void sendAppointmentConfirmationEmail(Appointment appointment, boolean toOwner) {
    User customer = appointment.getCustomer();
    Employee employee = appointment.getEmployee();
    SalonServiceEntity salonServiceEntity = appointment.getSalonServiceEntity();
    User owner = appointment.getSalonServiceEntity().getSalon().getOwner();
    sendSimpleMail(
        new EmailDetails(
            toOwner ? owner.getEmail() : customer.getEmail(),
            String.format(
                !toOwner
                    ? "Hello %s %s,<br>"
                        + "You have successfully booked an appointment at %s with the barber %s %s for %s for the following service - %s (%.2f BGN).<br>"
                        + "Thank you for using our platform!<br>"
                        + "Best regards,<br>"
                        + "haircut.place"
                    : "Hello %s %s,<br>"
                        + "An appointment has been arranged at your salon %s with the barber %s %s for %s for the following service - %s (%.2f BGN).<br>"
                        + "Best regards,<br>"
                        + "haircut.place",
                toOwner ? owner.getFirstName() : customer.getFirstName(),
                toOwner ? owner.getLastName() : customer.getLastName(),
                salonServiceEntity.getSalon().getName(),
                employee.getFirstName(),
                employee.getLastName(),
                appointment
                    .getAppointmentStart()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")),
                salonServiceEntity.getService().getName(),
                salonServiceEntity.getPrice()),
            "Appointment confirmation - haircut.place",
            null));
  }

  public void sendAppointmentCancellationEmail(Appointment appointment, boolean toOwner) {
    User customer = appointment.getCustomer();
    Employee employee = appointment.getEmployee();
    SalonServiceEntity salonServiceEntity = appointment.getSalonServiceEntity();
    User owner = appointment.getSalonServiceEntity().getSalon().getOwner();
    sendSimpleMail(
        new EmailDetails(
            toOwner ? owner.getEmail() : customer.getEmail(),
            String.format(
                !toOwner
                    ? "Hello %s %s,<br>"
                        + "Your appointment at %s with the barber %s %s for %s for the following service - %s (%.2f BGN), has been cancelled.<br>"
                        + "Thank you for using our platform!<br>"
                        + "Best regards,<br>"
                        + "haircut.place"
                    : "Hello %s %s,<br>"
                        + "An appointment at your salon %s with the barber %s %s for %s for the following service - %s (%.2f BGN), has been cancelled.<br>"
                        + "Best regards,<br>"
                        + "haircut.place",
                toOwner ? owner.getFirstName() : customer.getFirstName(),
                toOwner ? owner.getLastName() : customer.getLastName(),
                salonServiceEntity.getSalon().getName(),
                employee.getFirstName(),
                employee.getLastName(),
                appointment
                    .getAppointmentStart()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")),
                salonServiceEntity.getService().getName(),
                salonServiceEntity.getPrice()),
            "Appointment cancelled - haircut.place",
            null));
  }
}
