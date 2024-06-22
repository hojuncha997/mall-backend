package com.test.mallapi.service;

import com.test.mallapi.dto.MemberDTO;
import jakarta.transaction.Transactional;

@Transactional
public interface MemberService {
    MemberDTO getKakaoMember(String accessToken);

}
