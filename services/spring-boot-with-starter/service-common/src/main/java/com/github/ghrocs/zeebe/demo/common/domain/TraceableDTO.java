package com.github.ghrocs.zeebe.demo.common.domain;

import lombok.Data;

import java.io.Serializable;

/** @author Rocs Zhang */
@Data
public abstract class TraceableDTO implements Serializable {

  private String traceId;
}
