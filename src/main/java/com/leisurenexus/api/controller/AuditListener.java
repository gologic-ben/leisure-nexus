package com.leisurenexus.api.controller;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdDate", "modifiedDate", "createdUserId", "modifiedUserId"}, allowGetters = true)
public abstract class AuditListener implements Serializable {
  private static final long serialVersionUID = 1L;

  private @CreatedBy Long createdUserId;
  private @LastModifiedBy Long modifiedUserId;
  private @CreatedDate Date createdDate;
  private @LastModifiedDate Date modifiedDate;

}
