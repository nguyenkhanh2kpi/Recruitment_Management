package com.java08.quanlituyendung.controller;

import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.dto.proposal.AddProposalDTO;
import com.java08.quanlituyendung.entity.ProposalEntity;
import com.java08.quanlituyendung.service.IUserService;
import com.java08.quanlituyendung.service.impl.ProposalServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proposal")
@RequiredArgsConstructor
@Tag(name = "Proposal")
public class ProposalController {

    @Autowired
    private ProposalServiceImpl proposalService;

//    nguoi gui get
    @GetMapping
    public ResponseEntity<ResponseObject> getMyProposals(Authentication authentication)  {
        return proposalService.getMyProposal(authentication);
    }


//    nguoi nhan get
    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllMyProposal(Authentication authentication)  {
        return proposalService.getAllMyProposal(authentication);
    }

//    accept reject
    @PutMapping("/{proposalId}/{state}")
    public ResponseEntity<ResponseObject> addProposal(@PathVariable String state, @PathVariable Long proposalId) {
        return proposalService.sendState(state,proposalId);
    }

    @PostMapping
    public ResponseEntity<ResponseObject> postAPropoal(@RequestBody AddProposalDTO request, Authentication authentication)  {
        return proposalService.addNewProposal(request,authentication);
    }




}
