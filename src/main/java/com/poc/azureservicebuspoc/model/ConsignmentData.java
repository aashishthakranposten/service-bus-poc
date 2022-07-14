package com.poc.azureservicebuspoc.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@ToString
@Table(name = "consignment_data")
public class ConsignmentData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID batchId;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Consignment> consignments;
}
