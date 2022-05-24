package com.poc.azureservicebuspoc.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class ConsignmentData implements Serializable {

    private UUID batchId;
    private List<Consignment> consignments;
}
