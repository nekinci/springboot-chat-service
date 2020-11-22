package com.spotify.api.util;

import com.sun.org.apache.xalan.internal.lib.Extensions;
import lombok.experimental.ExtensionMethod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StringOperations {

    public static Map<String, String> toMap(String query){
        HashMap<String, String> params = new HashMap<>();
        if(Objects.nonNull(query)){
            String _arr[] = query.split("&");
            if(_arr.length > 0){
                Arrays.stream(_arr).forEach(x -> {
                   String _inArr[] = x.split("=");
                   if(Objects.nonNull(_inArr) && _inArr.length == 2){
                       params.put(_inArr[0], _inArr[1]);
                   }
                });
            }
        }
        return params;
    }
}
