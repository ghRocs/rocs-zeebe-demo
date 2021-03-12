package com.github.ghrocs.zeebe.demo.shipping.spi;

import com.github.ghrocs.zeebe.demo.common.exception.DefaultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.Random;

/** @author Rocs Zhang */
@Slf4j
@Controller
public class WMSApi {

  /**
   * 请求取货
   *
   * @param commodityCode 商品编码
   * @param count 数量
   */
  public void fetchCommodities(String commodityCode, int count) throws DefaultException {
    log.info("请求取出{}件{}商品", count, commodityCode);
    // TODO: 请求取货
    if (new Random().nextInt(5) == 0) {
      throw new DefaultException("从储管处取货失败");
    }
  }
}
