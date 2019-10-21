package com.citi.group2.simpletps.mapper;

import com.citi.group2.simpletps.entity.SalesLeg;
import com.citi.group2.simpletps.entity.SalesLegKey;

public interface SalesLegMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sales_leg
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(SalesLegKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sales_leg
     *
     * @mbggenerated
     */
    int insert(SalesLeg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sales_leg
     *
     * @mbggenerated
     */
    int insertSelective(SalesLeg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sales_leg
     *
     * @mbggenerated
     */
    SalesLeg selectByPrimaryKey(SalesLegKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sales_leg
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SalesLeg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sales_leg
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(SalesLeg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sales_leg
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SalesLeg record);
}