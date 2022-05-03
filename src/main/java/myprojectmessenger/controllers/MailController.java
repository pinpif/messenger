package myprojectmessenger.controllers;

import myprojectmessenger.entity.Account;
import myprojectmessenger.entity.RecoveryPassword;
import myprojectmessenger.service.RecoveryPasswordService;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Controller
public class MailController {
    private final RecoveryPasswordService recoveryPasswordService;
    public MailController(RecoveryPasswordService recoveryPasswordService) {

        this.recoveryPasswordService = recoveryPasswordService;
    }

    @GetMapping("recoveryPassword")
    @ResponseBody
    public void sendSimpleEmail(@RequestHeader(value = "mail") String mail) throws MessagingException {
      recoveryPasswordService.sendSimpleEmail(mail);
    }

    @GetMapping("newPassword/{id}")
    public String newPassword(Model model, @PathVariable("id") String id) {
        return "newPass";
    }

    @PostMapping("newPassword/{code}")
    public String addNewPassword(Model model, @PathVariable(value = "{code}") String uuid, @ModelAttribute("password") String password) {
        recoveryPasswordService.addNewPassword(uuid, password);
        return null;
    }
}

