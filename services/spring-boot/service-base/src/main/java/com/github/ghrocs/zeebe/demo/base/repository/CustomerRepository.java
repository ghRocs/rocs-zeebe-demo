package com.github.ghrocs.zeebe.demo.base.repository;

import com.github.ghrocs.zeebe.demo.base.domain.CustomerDO;

import java.util.concurrent.ConcurrentHashMap;

/** @author: Rocs Zhang */
public class CustomerRepository {

  // For no-null key value
  private static final ConcurrentHashMap<Long, CustomerDO> CACHE = new ConcurrentHashMap<>();

  static { // TODO: 初始化顾客基本数据放于本地缓存中
    saveOrUpdate(new CustomerDO(2L, "Han Meimei", "Chengdu, Sichuan, China"));
    saveOrUpdate(new CustomerDO(1L, "Li Lei", "Beijing, China"));
  }

  // Private for possible consistency
  private static void saveOrUpdate(CustomerDO customerDO) {
    CACHE.put(customerDO.getId(), customerDO);
  }

  /**
   * 获取顾客基本信息
   *
   * @param customerId 顾客ID
   * @return CustomerDO
   */
  public static CustomerDO getById(Long customerId) {
    return CACHE.get(customerId);
  }
}
