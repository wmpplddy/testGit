package org.mvc.conversion;



public interface ConversionStretagy<L,R> {

    R convert(L source) throws Exception;
}
