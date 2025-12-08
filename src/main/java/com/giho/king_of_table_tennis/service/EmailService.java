package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.SendVerificationCodeResponseDTO;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

@RequiredArgsConstructor
@Service
public class EmailService {

  private final SesV2Client sesV2Client;

  private final SpringTemplateEngine templateEngine;

  @Value("${mail.from}")
  private String FROM;

  // 이메일 전송 메서드
  public SendVerificationCodeResponseDTO sendVerificationEmail (String type, String email, HttpServletRequest request) {
    try {
      HttpSession session = request.getSession(true);

      // 인증 코드 생성
      String verificationCode = generateVerificationCode();

      // 제목 & 본문 생성
      String subject = getSubjectByType(type);
      String htmlBody = buildEmailContent(verificationCode, type);

      // SES destination
      Destination destination = Destination.builder()
        .toAddresses(email)
        .build();

      Message message = Message.builder()
        .subject(
          Content.builder().data(subject).charset("UTF-8").build()
        )
        .body(
          Body.builder()
            .html(Content.builder().data(htmlBody).charset("UTF-8").build())
            .build()
        )
        .build();

      // SES 요청 만들기
      SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
        .fromEmailAddress(FROM)
        .destination(destination)
        .content(EmailContent.builder().simple(message).build())
        .build();

      // 이메일 발송
      sesV2Client.sendEmail(sendEmailRequest);

      // 세션에 인증번호 저장
      boolean isSaveCode = saveCodeToSession(verificationCode, 3, session);

      if (!isSaveCode) {
        throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
      }

      return new SendVerificationCodeResponseDTO(session.getId());
    } catch (SesV2Exception e) {
      throw new CustomException(ErrorCode.EMAIL_SEND_FAILED, "이메일 전송 실패(SES): " + e.awsErrorDetails().errorMessage());
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
