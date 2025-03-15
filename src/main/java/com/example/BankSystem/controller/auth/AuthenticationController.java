package com.example.BankSystem.controller.auth;


import com.example.BankSystem.dto.JwtAuthenticationResponse;
import com.example.BankSystem.dto.SignInRequest;
import com.example.BankSystem.dto.SignUpRequest;
import com.example.BankSystem.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/sign-up")
  public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
    return authenticationService.signUp(request);
  }


  @PostMapping("/sign-in")
  public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
    return authenticationService.signIn(request);
  }
}
