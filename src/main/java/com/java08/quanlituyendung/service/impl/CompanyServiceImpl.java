package com.java08.quanlituyendung.service.impl;

import com.java08.quanlituyendung.auth.UserAccountRetriever;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.dto.company.CompanyDTO;
import com.java08.quanlituyendung.entity.CompanyEntity;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.repository.CompanyRepository;
import com.java08.quanlituyendung.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public class CompanyServiceImpl implements ICompanyService {

    UserAccountRetriever userAccountRetriever;

    @Autowired
    CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository, UserAccountRetriever userAccountRetriever) {
        this.companyRepository = companyRepository;
        this.userAccountRetriever = userAccountRetriever;
    }

    @Override
    public ResponseEntity<ResponseObject> init(Authentication authentication) {
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        if (user != null) {

            var old = companyRepository.findByUserId(user.getId());

            if (old.isEmpty()) {
                CompanyEntity companyEntity = CompanyEntity.builder()
                        .avatar("")
                        .name(user.getUsernameReal())
                        .info("")
                        .website("")
                        .address("")
                        .phone("")
                        .userId(user.getId())
                        .build();
                return ResponseEntity.ok(ResponseObject.builder()
                        .message("Success")
                        .status("200 OK")
                        .data(companyRepository.save(companyEntity))
                        .build());
            }
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Company have init")
                    .status("200")
                    .build());
        }
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Can not find your company")
                .status("403")
                .build());
    }

    @Override
    public ResponseEntity<ResponseObject> getAllCompany() {
        try {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Success")
                    .status("200 OK")
                    .data(companyRepository.findAll())
                    .build());

        } catch (Exception e) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Internal error")
                    .status("405")
                    .build());
        }

    }

    @Override
    public ResponseEntity<ResponseObject> getById(Long id) {
        try {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Success")
                    .status("200 OK")
                    .data(companyRepository.findById(id))
                    .build());

        } catch (Exception e) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Internal error")
                    .status("405")
                    .build());
        }
    }

    @Override
    public ResponseEntity<ResponseObject> update(CompanyDTO companyDTO, Authentication authentication) {
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        if (user != null) {
            var old = companyRepository.findByUserId(user.getId());
            if (old.isEmpty()) {
                CompanyEntity companyEntity = CompanyEntity.builder()
                        .avatar(companyDTO.getAvatar())
                        .name(companyDTO.getName())
                        .info(companyDTO.getInfo())
                        .website(companyDTO.getWebsite())
                        .address(companyDTO.getAddress())
                        .phone(companyDTO.getPhone())
                        .userId(user.getId())
                        .build();
                return ResponseEntity.ok(ResponseObject.builder()
                        .message("Success create")
                        .status("200 OK")
                        .data(companyRepository.save(companyEntity))
                        .build());
            } else {
                var company = old.get();
                company.setAvatar(companyDTO.getAvatar());
                company.setName(companyDTO.getName());
                company.setWebsite(companyDTO.getWebsite());
                company.setInfo(companyDTO.getInfo());
                company.setAddress(companyDTO.getAddress());
                company.setPhone(companyDTO.getPhone());
                return ResponseEntity.ok(ResponseObject.builder()
                        .message("Success update")
                        .status("200")
                        .data(companyRepository.save(company))
                        .build());
            }
        }
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Can not find your company")
                .status("403")
                .build());
    }

    @Override
    public ResponseEntity<ResponseObject> getMyCompany(Authentication authentication) {
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        if (user != null) {
            var company = companyRepository.findByUserId(user.getId());
            if (company.isEmpty()) {
                return init(authentication);
            } else {
                return ResponseEntity.ok(ResponseObject.builder()
                        .message("Success get")
                        .status("200 OK")
                        .data(company.get())
                        .build());
            }
        }
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Internal error")
                .status("403")
                .build());
    }
}
