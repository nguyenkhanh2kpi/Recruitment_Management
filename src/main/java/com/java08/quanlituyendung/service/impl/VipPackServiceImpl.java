package com.java08.quanlituyendung.service.impl;


import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.entity.vip.BillEntity;
import com.java08.quanlituyendung.entity.vip.VipPackReccerEntity;
import com.java08.quanlituyendung.repository.BillRepository;
import com.java08.quanlituyendung.repository.VipPackRepository;
import com.java08.quanlituyendung.service.IVipPackService;
import com.java08.quanlituyendung.utils.BillStatus;
import com.java08.quanlituyendung.utils.PackVipType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class VipPackServiceImpl implements IVipPackService {

    @Autowired
    private VipPackRepository vipPackRepository;
    @Autowired
    private BillRepository billRepository;

    @Override
    public List<VipPackReccerEntity> findAll() {
        return vipPackRepository.findAll();
    }

    @Override
    public Optional<VipPackReccerEntity> findById(Long id) {
        return vipPackRepository.findById(id);
    }

    @Override
    public VipPackReccerEntity save(VipPackReccerEntity vipPackReccerEntity) {
        return vipPackRepository.save(vipPackReccerEntity);
    }

    @Override
    public VipPackReccerEntity update(Long id, VipPackReccerEntity vipPackReccerEntity) {
        if (!vipPackRepository.existsById(id)) {
            throw new RuntimeException("VipPackReccerEntity not found with id " + id);
        }
        vipPackReccerEntity.setId(id);
        return vipPackRepository.save(vipPackReccerEntity);
    }

    @Override
    public void deleteById(Long id) {
        if (!vipPackRepository.existsById(id)) {
            throw new RuntimeException("VipPackReccerEntity not found with id " + id);
        }
        vipPackRepository.deleteById(id);
    }

    @Override
    public boolean isUserVip(UserAccountEntity userAccountEntity) {
        String userEmail = userAccountEntity.getEmail();
        List<BillEntity> billEntities = billRepository.findByEmail(userEmail);
        LocalDateTime now = LocalDateTime.now();

        for (BillEntity billEntity : billEntities) {
            if (billEntity.getIs_payed()
                    && LocalDateTime.parse(billEntity.getExpired_at(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).isAfter(now)) {
                return true;
            }
        }
        return false;
    }

}
