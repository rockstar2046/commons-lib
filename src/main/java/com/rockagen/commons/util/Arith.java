/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rockagen.commons.util;

import java.math.BigDecimal;

/**
 * 
 * High-precision calculation 
 * 
 * @author AGEN
 * @since JDK1.6
 */
public class Arith {

	
    
    //~ Instance fields ==================================================
    
    /**
     * default precision
     */
    private static final int DIV_SCALE = 10;
    /**
     * default rounding Mode
     */
    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;
    
    
    //~ Constructors ==================================================
    
    private Arith(){
    }
 
    //~ Methods ==================================================
    
    /**
     * @param v1
     * @param v2
     * @return v1+v2
     */
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
    /**
     * add 
     * @param v1 
     * @param v2 
     * @return v1+v2
     */
    public static float addF(float v1,float v2){
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.add(b2).floatValue();
    }
    /**
     * sub
     * @param v1 
     * @param v2 
     * @return v1-v2
     */
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    } 
    /**
     * sub
     * @param v1 
     * @param v2 
     * @return v1-v2
     */
    public static float subF(float v1,float v2){
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.subtract(b2).floatValue();
    } 
    
    /**
     * mul
     * @param v1 
     * @param v2 
     * @return v1*v2
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    /**
     * mul
     * @param v1 
     * @param v2 
     * @return v1*v2
     */
    public static float mul(float v1,float v2){
    	BigDecimal b1 = new BigDecimal(Float.toString(v1));
    	BigDecimal b2 = new BigDecimal(Float.toString(v2));
    	return b1.multiply(b2).floatValue();
    }
 
    /**
     * div
     * @param v1
     * @param v2
     * @return v1/v2
     */
    public static double div(double v1,double v2){
        return div(v1,v2,DIV_SCALE);
    }
    /**
     * div
     * @param v1
     * @param v2
     * @return v1/v2
     */
    public static double div(double v1,double v2,int scale){
    	return div(v1,v2,scale,ROUNDING_MODE);
    }
    
    /**
     * div
     * @param v1
     * @param v2
     * @return v1/v2
     */
    public static float div(float v1,float v2){
    	return div(v1,v2,DIV_SCALE);
    }
    /**
     * div
     * @param v1
     * @param v2
     * @return v1/v2
     */
    public static float div(float v1,float v2,int scale){
    	return div(v1,v2,scale);
    }
 
    /**
     * div 
     * @param v1
     * @param v2
     * @param scale precision default {@link #DIV_SCALE}
     * @param roundingMode default {@link #ROUNDING_MODE} 
     * @return v1/v2
     */
    public static double div(double v1,double v2,int scale,int roundingMode){
    	
        if(scale<0){
            scale=DIV_SCALE;
        }
        if(roundingMode<0){
        	roundingMode=ROUNDING_MODE;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,roundingMode).doubleValue();
    }
    
    /**
     * div 
     * @param v1
     * @param v2
     * @param scale precision default {@link #DIV_SCALE}
     * @param roundingMode default {@link #ROUNDING_MODE} 
     * @return v1/v2
     */
    public static float div(float v1,float v2,int scale,int roundingMode){
    	
        if(scale<0){
            scale=DIV_SCALE;
        }
        if(roundingMode<0){
        	roundingMode=ROUNDING_MODE;
        }
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.divide(b2,scale,roundingMode).floatValue();
    }
 
    /**
     * random
     * @param v
     * @param scale
     * @return double
     */
    public static double round(double v,int scale){
    	return round(v,scale,ROUNDING_MODE);
    }
    /**
     * random
     * @param v
     * @return double
     */
    public static double round(double v){
    	return round(v,DIV_SCALE,ROUNDING_MODE);
    }
    
    /**
     * random
     * @param v
     * @param scale
     * @param roundingMode
     * @return double
     */
    public static double round(double v,int scale,int roundingMode){
    	   if(scale<0){
               scale=DIV_SCALE;
           }
           if(roundingMode<0){
           	roundingMode=ROUNDING_MODE;
           }
    	BigDecimal b = new BigDecimal(Double.toString(v));
    	BigDecimal one = new BigDecimal("1");
    	return b.divide(one,scale,roundingMode).doubleValue();
    }
    
    /**
     * random
     * @param v
     * @param scale
     * @return round float 
     */
    public static float round(float v,int scale){
    	return round(v,scale,ROUNDING_MODE);
    }
    /**
     * random
     * @param v
     * @return round float
     */
    public static float round(float v){
    	return round(v,DIV_SCALE,ROUNDING_MODE);
    }
    
    /**
     * random
     * @param v
     * @param scale
     * @param roundingMode
     * @return round float
     */
    public static float round(float v,int scale,int roundingMode){
    	if(scale<0){
    		scale=DIV_SCALE;
    	}
    	if(roundingMode<0){
    		roundingMode=ROUNDING_MODE;
    	}
    	BigDecimal b = new BigDecimal(Float.toString(v));
    	BigDecimal one = new BigDecimal("1");
    	return b.divide(one,scale,roundingMode).floatValue();
    }
    
    
    /**
     * to double
     * @param d
     * @return double
     */
    public static float toFloat(double d){
    	BigDecimal b = new BigDecimal(Double.toString(d));
    	return b.floatValue();
    }
    
    
    
    /**
     * to float
     * @param f
     * @return double
     */
    public static double toDouble(float f){
    	BigDecimal b = new BigDecimal(Float.toString(f));
    	return b.doubleValue();
    }
}
