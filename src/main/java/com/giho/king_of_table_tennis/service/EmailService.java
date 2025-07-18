package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.SendVerificationCodeResponseDTO;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@RequiredArgsConstructor
@Service
public class EmailService {

  private final JavaMailSender javaMailSender;

  private final SpringTemplateEngine templateEngine;

  // 이메일 전송 메서드
  public SendVerificationCodeResponseDTO sendVerificationEmail (String type, String email, HttpServletRequest request) {
    try {
      HttpSession session = request.getSession(true);
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

      String verificationCode = generateVerificationCode();

      mimeMessageHelper.setTo(email); // 수신자 설정
      mimeMessageHelper.setSubject(getSubjectByType(type)); // 이메일 제목 설정
      mimeMessageHelper.setText(buildEmailContent(verificationCode, type), true); // 이메일 내용, HTML 형식으로 설정

      // 이메일 발송
      javaMailSender.send(mimeMessage);

      // 세션에 인증번호 저장
      boolean isSaveCode = saveCodeToSession(verificationCode, 3, session);

      if (!isSaveCode) {
        throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
      }
      return new SendVerificationCodeResponseDTO(session.getId());
    } catch (MessagingException messagingException) {
      throw new CustomException(ErrorCode.EMAIL_SEND_FAILED, "이메일 전송 실패: " + messagingException.getMessage());
    } catch (Exception e) {
      throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  // 인증번호 확인 메서드
  public boolean checkVerificationCode(String sessionId, String userCode, HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null || !sessionId.equals(session.getId())) {
      throw new CustomException(ErrorCode.INVALID_SESSION);
    }

    String sessionCode = (String) session.getAttribute("verificationCode");

    if (sessionCode == null) {
      throw new CustomException(ErrorCode.VERIFICATION_CODE_MISMATCH, "저장된 인증번호가 없습니다.");
    }

    if (!sessionCode.equals(userCode)) {
      throw new CustomException(ErrorCode.VERIFICATION_CODE_MISMATCH);
    }

    session.removeAttribute("verificationCode");
    return true;
  }

  // 이메일 타입에 맞는 제목 반환
  private String getSubjectByType(String type) {
    return switch (type) {
      case "register" -> "[탁구왕] 회원가입 인증번호";
      case "find-id" -> "[탁구왕] 아이디 찾기 인증번호";
      case "find-password" -> "[탁구왕] 비밀번호 찾기 인증번호";
      default -> "[탁구왕] 이메일";
    };
  }

  // 이메일 내용 구성 (HTML)
  private String buildEmailContent(String code, String type) {
    Context context = new Context();
    context.setVariable("code", code);
    return templateEngine.process(getTemplateName(type), context); // 적합한 템플릿 반환
  }

  // 이메일 타입에 맞는 템플릿 이름 반환
  private String getTemplateName(String type) {
    return switch (type) {
      case "register" -> "sendCode_register"; // 회원가입 템플릿
      case "find-id" -> "sendCode_findId"; // 아이디 찾기 템플릿
      case "find-password" -> "sendCode_findPassword"; // 비밀번호 찾기 템플릿
      default -> "sendCode_default"; // 기본 템플릿
    };
  }

  // 6자리 인증번호 생성하는 메서드
  private String generateVerificationCode() {
    return String.format("%06d", (int)(Math.random() * 900000) + 100000); // 1000 ~ 9999 사이의 6자리 인증번호 생성
  }

  // 인증번호를 세션에 저장하는 메서드
  private boolean saveCodeToSession(String verificationCode, int expirationTime, HttpSession session) {
    try {
      session.setAttribute("verificationCode", verificationCode);
      session.setMaxInactiveInterval(expirationTime * 60); // 3분

      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
