package io.vizit.vpoc.jvm.model;

public interface JvmConfig {
    int SurvivorRatio = 8; // survivor:eden = 1:8
    int NewRatio = 2;// young:old = 1:2
}
