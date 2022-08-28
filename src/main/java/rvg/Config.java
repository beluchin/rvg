package rvg;

import lombok.Value;

@Value
public class Config {
    public static Config empty() { return new Config(); }
}
