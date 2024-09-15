package com.travel.global.scheduler;

import com.travel.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberDeleteScheduler {

    private final MemberService memberService;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void deleteOldMembers() {
        memberService.permanentlyDeleteOldMembers();
    }
}
