package com.github.aint.octopus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DependencyJson {

    private String name;

    private Map<String, String> dependencies; // use enum with type

}
