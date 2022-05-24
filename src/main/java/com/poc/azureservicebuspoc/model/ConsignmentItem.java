package com.poc.azureservicebuspoc.model;

import java.io.Serializable;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class ConsignmentItem implements Serializable {

    private UUID id;
    private UUID consignmentId;
}
