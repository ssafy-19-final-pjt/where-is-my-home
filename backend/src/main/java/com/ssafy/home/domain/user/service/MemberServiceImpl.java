package com.ssafy.home.domain.user.service;

import com.ssafy.home.domain.user.domain.Member;
import com.ssafy.home.domain.user.domain.MemberSecret;
import com.ssafy.home.domain.user.dto.request.LoginRequest;
import com.ssafy.home.domain.user.dto.request.RegisterRequest;
import com.ssafy.home.domain.user.dto.response.TokenResponse;
import com.ssafy.home.domain.user.repository.MemberRepository;
import com.ssafy.home.domain.user.repository.MemberSecurityRepository;
import com.ssafy.home.global.auth.Encryption;
import com.ssafy.home.global.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    final private MemberRepository memberRepository;
    final private MemberSecurityRepository memberSecurityRepository;

    final private JwtTokenProvider jwtTokenProvider;
    private final Encryption encryption;

    @Override
    public void register(RegisterRequest registerRequest) {
        if(memberRepository.existsByEmail(registerRequest.getEmail())){
            System.out.println("이메일 중복 error 처리 요");
        };

        try{
            String salt  = encryption.getSalt();
            String password = encryption.Hashing(registerRequest.getPassword().getBytes(), salt);
            registerRequest.createPassword(password);

            Member member = memberRepository.save(registerRequest.toEntity());
            memberSecurityRepository.save(MemberSecret.builder().member(member).salt(salt).build());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow();

        String salt = memberSecurityRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("error 처리 요"))
                .getSalt();


        try {
            String enc_password = encryption.Hashing(loginRequest.getPassword().getBytes(), salt);

            if (!member.getUserEncPassword().equals(enc_password)) {
                new Exception("error 처리 요");
            }

            String accessToken = jwtTokenProvider.createAccessToken(member);
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public String reissue(String refreshToken) {

        if(jwtTokenProvider.validateToken(refreshToken)){

            JSONObject info = new JSONObject(jwtTokenProvider.getInformation(refreshToken));
            Long id = info.getLong("id");

            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("No member found with id: " + id));

            return jwtTokenProvider.createAccessToken(member);
        }

        return null;
    }


}
