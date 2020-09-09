package com.voidbean.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@SuppressWarnings("all")
public class RibbonDefinition {

    
    String serviceName;
    
    String ribbonClass;
    
    String ribbonName;

}
