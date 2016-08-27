package com.tradeengine.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message
{
    private String message;
    private Status status;

    public enum Status
    {
        SUCCESS, PARTIAL_SUCCESS, FAILURE
    }
}
