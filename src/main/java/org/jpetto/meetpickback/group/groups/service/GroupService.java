package org.jpetto.meetpickback.group.groups.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.account.accounts.entity.Account;
import org.jpetto.meetpickback.group.groups.dto.GroupDto;
import org.jpetto.meetpickback.group.groups.entity.Group;
import org.jpetto.meetpickback.group.groups.repository.GroupRepository;
import org.jpetto.meetpickback.group.members.dto.MemberDto;
import org.jpetto.meetpickback.group.members.entity.Member;
import org.jpetto.meetpickback.group.members.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberService memberService;

    // 그룹 생성
    @Transactional
    public GroupDto.createGroupResponse createGroup(Account loginUser, GroupDto.createGroupRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        Group group = Group.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        groupRepository.save(group);

        memberService.addOwner(group, loginUser.getId());

        List<MemberDto.MemberInfo> failMembers = new ArrayList<>();
        if (request.getMembers() != null) {
            failMembers = memberService.addMembers(group, request.getMembers());
        }

        return GroupDto.createGroupResponse.builder().failMembers(failMembers).message("모임 생성 완료").build();
    }

    // 그룹 수정
    @Transactional
    public GroupDto.updateGroupResponse updateGroup(Account loginUser, long groupId, GroupDto.@Valid updateGroupRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (request.getName() != null) {
            group.setName(request.getName());
        }

        if (request.getDescription() != null) {
            group.setDescription(request.getDescription());
        }

        return GroupDto.updateGroupResponse.builder().message("모임 업데이트 완료").build();
    }

    // 그룹 삭제
    @Transactional
    public GroupDto.deleteGroupResponse deleteGroup(Account loginUser, long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (!memberService.isOwner(group, loginUser)) {
            throw new IllegalArgumentException("member can't delete this group");
        }

        groupRepository.delete(group);

        return GroupDto.deleteGroupResponse.builder().message("그룹 삭제 완료했습니다.").build();
    }

    // 그룹 조회
    public List<GroupDto.getGroupResponse> getGroup(Account loginUser) {
        List<Group> groups = groupRepository.findByMembersMemberAccountId(loginUser.getId());
        List<GroupDto.getGroupResponse> groupResponses = new ArrayList<>();
        for (Group group : groups) {
            groupResponses.add(
                    GroupDto.getGroupResponse.builder()
                            .id(group.getId())
                            .name(group.getName())
                            .description(group.getDescription())
                            .build()
            );
        }

        return groupResponses;
    }

    // 그룹 멤버 추가
    @Transactional
    public MemberDto.addMemberResponse addMemberToGroup(Account loginUser, long groupId, MemberDto.addMemberRequest request) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (!memberService.isInGroup(group, loginUser)) {
            throw new IllegalArgumentException("You cannot invite them unless they are included in the group.");
        }

        List<MemberDto.MemberInfo> failMembers = memberService.addMembers(group, request.getMemberIds());

        return MemberDto.addMemberResponse.builder().failMember(failMembers).message("멤버 추가 완료").build();
    }

    // 그룹 멤버 삭제(나가기)
    @Transactional
    public MemberDto.deleteMemberResponse deleteMemberToGroup(Account loginUser, long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (!memberService.isInGroup(group, loginUser)) {
            throw new IllegalArgumentException("You cannot delete them unless they are included in the group.");
        }

        memberService.deleteMember(group, loginUser);

        return MemberDto.deleteMemberResponse.builder().message("그룹에서 나갔습니다.").build();
    }

    // 그룹 방장 임명
    @Transactional
    public MemberDto.transferOwnershipResponse changeOwnership(Account loginUser, long groupId, long memberId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (!memberService.isOwner(group, loginUser)) {
            throw new IllegalArgumentException("You cannot change owner of this group.");
        }

        memberService.changOwner(loginUser, group, memberId);

        return MemberDto.transferOwnershipResponse.builder().message("방장 위임이 완료되었습니다.").build();
    }

    public Group getGroupById(long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
    }
}
