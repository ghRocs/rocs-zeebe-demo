package com.github.ghrocs.zeebe.demo.shipping.service;

/** @author Rocs Zhang */
public interface IShippingService {

  /**
   * 运送包裹
   *
   * @param address 运往地址
   */
  void shipParcel(String address);
}
