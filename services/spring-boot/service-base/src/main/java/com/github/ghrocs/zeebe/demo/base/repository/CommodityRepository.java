package com.github.ghrocs.zeebe.demo.base.repository;

import com.github.ghrocs.zeebe.demo.base.domain.CommodityDO;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

/** @author: Rocs Zhang */
public class CommodityRepository {

  // For no-null key value
  private static final ConcurrentHashMap<String, CommodityDO> CACHE = new ConcurrentHashMap<>();

  static { // TODO: 初始化商品基本数据放于本地缓存中
    saveOrUpdate(
        new CommodityDO(
            "ISBN-7560013473",
            "New Concept English 2 (Chinese Edition)",
            false,
            new BigDecimal(26.39)));
    saveOrUpdate(
        new CommodityDO(
            "ISBN-7560013465", "Longman New Concept English 1", false, new BigDecimal(21.98)));
    saveOrUpdate(
        new CommodityDO(
            "ASIN-B012JIOKM4",
            "World of Warcraft 60 Day Game Time [Digital Code]",
            true,
            new BigDecimal(29.99)));
  }

  // Private for possible consistency
  private static void saveOrUpdate(CommodityDO commodityDO) {
    CACHE.put(commodityDO.getCode(), commodityDO);
  }

  /**
   * 获取商品基本信息
   *
   * @param commodityCode 商品编码
   * @return CommodityDO
   */
  public static CommodityDO getByCode(String commodityCode) {
    return CACHE.get(commodityCode);
  }
}
