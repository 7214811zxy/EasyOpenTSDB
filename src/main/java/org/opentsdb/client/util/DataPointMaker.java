package org.opentsdb.client.util;

import org.opentsdb.client.request.DataPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021-07-21 3:16 下午
 * @description
 */
public class DataPointMaker {

    /**
     * 生成一个数据点，数据点的时间戳由用户指定
     * @param tagName 数据点的metrics
     * @param value 数据点的值
     * @param time 数据点的时间戳
     * @param tags 数据点的tags
     * @return List<DataPoint>
     */
    public static List<DataPoint> makeOne(
            String tagName,
            Long time,
            Double value,
            Map<String, String> tags
    ){
        List<DataPoint> dataPoints = new ArrayList<>();
        DataPoint dataPoint = new DataPoint(tagName, time, value, tags);
        dataPoints.add(dataPoint);
        return dataPoints;
    }

    /**
     * 生成一个数据点,数据点的时间戳等于函数调用时的系统时间
     * @param tagName 数据点的metrics
     * @param value 数据点的值
     * @param tags 数据点的tags
     * @return List<DataPoint>
     */
    public static List<DataPoint> makeOne(
            String tagName,
            Double value,
            Map<String, String> tags
    ){
        return makeOne(tagName, System.currentTimeMillis() / 1000, value, tags);
    }

    /**
     * 生成一批数据点,第一个数据点的时间戳等于函数调用时的系统时间
     * @param tagName 对应metrics
     * @param value 数据点的值
     * @param tags 数据点的tags
     * @param timeStep 每个数据点的时间戳增量
     * @param dataNum 数据点的总量
     * @return List<DataPoint>
     */
    public static List<DataPoint> makeBatch(
            String tagName,
            Double value,
            Map<String, String> tags,
            Integer timeStep,
            Integer dataNum
    ){
        return makeBatch(tagName, value, tags, System.currentTimeMillis() / 1000, timeStep, dataNum);
    }

    /**
     * 生成一批数据点,第一个数据点的时间戳由用户指定
     * @param tagName 对应metrics
     * @param value 数据点的值
     * @param tags 数据点的tags
     * @param startTime 第一个数据点的时间戳
     * @param timeStep 每个数据点的时间戳增量
     * @param dataNum 数据点的总量
     * @return List<DataPoint>
     */
    public static List<DataPoint> makeBatch(
            String tagName,
            Double value,
            Map<String, String> tags,
            Long startTime,
            Integer timeStep,
            Integer dataNum
    ){
        List<DataPoint> dataPoints = new ArrayList<>();
        for(int i=0; i<dataNum; i++){
            DataPoint dataPoint = new DataPoint(tagName, startTime, value, tags);
            dataPoints.add(dataPoint);
            startTime = startTime + timeStep;
        }
        return dataPoints;
    }


    /**
     * 生成一批数据点, 数据点的值是随机的，第一个数据点的时间戳由用户指定
     * @param tagName 对应metrics
     * @param tags 数据点的tags
     * @param startTime 第一个数据点的时间戳
     * @param timeStep 每个数据点的时间戳增量
     * @param dataNum 数据点的总量
     * @return List<DataPoint>
     */
    public static List<DataPoint> makeBatchRandomValue(
            String tagName,
            Map<String, String> tags,
            Long startTime,
            Integer timeStep,
            Integer dataNum
    ){
        List<DataPoint> dataPoints = new ArrayList<>();
        Random rand = new Random();
        for(int i=0; i<dataNum; i++){
            DataPoint dataPoint = new DataPoint(tagName, startTime, rand.nextDouble(), tags);
            dataPoints.add(dataPoint);
            startTime = startTime + timeStep;
        }
        return dataPoints;
    }

    /**
     * 生成一批数据点, 数据点的值是随机的，第一个数据点的时间戳等于函数调用时的系统时间
     * @param tagName 对应metrics
     * @param tags 数据点的tags
     * @param timeStep 每个数据点的时间戳增量
     * @param dataNum 数据点的总量
     * @return List<DataPoint>
     */
    public static List<DataPoint> makeBatchRandomValue(
            String tagName,
            Map<String, String> tags,
            Integer timeStep,
            Integer dataNum
    ){
        return makeBatchRandomValue(tagName, tags, System.currentTimeMillis() / 1000, timeStep, dataNum);
    }

    /**
     * 生成一批数据点, 数据点的值是递增的，第一个数据点的时间戳由用户指定
     * @param tagName 对应metrics
     * @param tags 数据点的tags
     * @param timeStep 每个数据点的时间戳增量
     * @param startValue 起始数据点的数值
     * @param valueStep 数据点的数值增量
     * @param dataNum 数据点的总量
     * @return List<DataPoint>
     */
    public static List<DataPoint> makeBatchStepValue(
            String tagName,
            Map<String, String> tags,
            Long startTime,
            Integer timeStep,
            Double startValue,
            Double valueStep,
            Integer dataNum
    ){
        List<DataPoint> dataPoints = new ArrayList<>();
        for(int i=0; i<dataNum; i++){
            DataPoint dataPoint = new DataPoint(tagName, startTime, startValue, tags);
            dataPoints.add(dataPoint);
            startTime = startTime + timeStep;
            startValue = startValue + valueStep;
        }
        return dataPoints;
    }
}
