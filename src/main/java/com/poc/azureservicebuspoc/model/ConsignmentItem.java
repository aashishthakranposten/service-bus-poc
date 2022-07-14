package com.poc.azureservicebuspoc.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@ToString
@Table(name = "consignment_item")
public class ConsignmentItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @Column(name = "consignment_id", length = 40)
    private UUID consignmentId;
}
