package org.jpetto.meetpickback.group.group.controller;

import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.group.group.service.GroupService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

}
