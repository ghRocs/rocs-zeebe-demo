package com.github.ghrocs.zeebe.demo.account.service;

import java.math.BigDecimal;

/** @author Rocs Zhang */
public interface IAccountService {

  /**
   * 余额扣款
   *
   * @param userId 用户ID
   * @param amount 扣款金额
   */
  void debit(long userId, BigDecimal amount);
}
