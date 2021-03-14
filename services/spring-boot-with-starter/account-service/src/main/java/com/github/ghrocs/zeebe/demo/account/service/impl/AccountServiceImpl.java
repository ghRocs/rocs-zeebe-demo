package com.github.ghrocs.zeebe.demo.account.service.impl;

import com.github.ghrocs.zeebe.demo.account.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/** @author Rocs Zhang */
@Slf4j
@Service
public class AccountServiceImpl implements IAccountService {

  @Override
  public void debit(long userId, BigDecimal amount) {
    log.info("用户ID为{}的账户余额扣去{}元", userId, amount);
    // TODO: 从账户扣钱
  }
}
